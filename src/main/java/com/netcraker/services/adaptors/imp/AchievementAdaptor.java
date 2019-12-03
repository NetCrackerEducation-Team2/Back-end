package com.netcraker.services.adaptors.imp;

import com.netcraker.exceptions.CreationException;
import com.netcraker.model.Achievement;
import com.netcraker.model.constants.Parameter;
import com.netcraker.model.constants.TableName;
import com.netcraker.model.constants.Verb;
import com.netcraker.model.vo.AchievementReq;
import com.netcraker.services.adaptors.Adaptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@RequiredArgsConstructor
@PropertySource("classpath:achievement-templates.properties")
public class AchievementAdaptor implements Adaptor<AchievementReq, Achievement> {

    @Value("${common.template.count}")
    private String templateCount;
    @Value("${common.template.read}")
    private String templateRead;
    @Value("${common.template.publish}")
    private String templatePublish;

    private final Environment env;

    @Override
    public Achievement adapt(AchievementReq achievementReq) {
        final Verb verb = achievementReq.getVerb();
        final TableName subject = achievementReq.getSubject();
        final Map<Parameter, List<String>> extraParams = achievementReq.getExtraParams();
        final String name = achievementReq.getName();
        final int count = achievementReq.getCount();

        if (!AchievementUtils.isValidVerb(verb, subject)) {
            throw new CreationException("Invalid values for achievement (wrong verb value)");
        }

        String query;

        if (extraParams != null) {
            query = createCommonParameterizedQuery(subject, verb, extraParams, count);
        } else {
            switch (verb) {
                case HAS:
                    query = insertParams(templateCount, count, subject.name());
                    break;
                case READ:
                    query = insertParams(templateRead, count, subject.name());
                    break;
                case PUBLISH:
                    query = insertParams(templatePublish, count, subject.name());
                    break;
                default:
                    throw new CreationException("Cannot create achievement (only `HAS`, `READ`, `PUBLISH` verbs are allowed)");
            }
        }
        String description = achievementReq.getDescription() != null ? achievementReq.getDescription() : AchievementUtils.DEFAULT_DESCRIPTION;
        return Achievement.builder().name(name).sqlQuery(query).tableName(subject).description(description).build();
    }

    private String insertParams(String template, Object... params) {
        return String.format(template, params);
    }

    private String createCommonParameterizedQuery(TableName subject, Verb verb, Map<Parameter, List<String>> extraParams, int count) {
        final StringJoiner query = new StringJoiner(" ");

        if (verb == Verb.READ) {
            query.add(createBookParameterizedQuery(extraParams, count));
        } else if (verb == Verb.HAS) {
            query.add(insertParams(env.getProperty("common.template.count"), count, subject));
        } else {
            throw new IllegalArgumentException("Invalid verb value (additional parameters are applied only to `HAS`, `READ` verbs)");
        }

        // Common query
        final List<String> creationTimeParams = extraParams.get(Parameter.CREATION_TIME);

        if (ObjectUtils.isEmpty(creationTimeParams) || creationTimeParams.get(0) == null || creationTimeParams.get(1) == null) {
            return query.toString();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date parsedLowBound, parsedHighBound;

        try {
            parsedLowBound = dateFormat.parse(creationTimeParams.get(0));
            parsedHighBound = dateFormat.parse(creationTimeParams.get(1));

            final Timestamp lowBound = new Timestamp(parsedLowBound.getTime());
            final Timestamp highBound = new Timestamp(parsedHighBound.getTime());

            query.add(insertParams(env.getProperty("common.condition.creation_time"), subject, lowBound, highBound));
        } catch (Exception e) {
            e.printStackTrace();
            throw new CreationException(e);
        }
        return query.toString();
    }

    private String createBookParameterizedQuery(Map<Parameter, List<String>> extraParams, int count) {
        final Set<Parameter> parameters = extraParams.keySet();
        final StringJoiner query = new StringJoiner(" ");
        final StringJoiner conditionalBuffer = new StringJoiner(" ");

        query.add(env.getProperty("books.template.count"));
        conditionalBuffer.add(env.getProperty("books.condition.read"));

        for (Parameter p : parameters) {

            // Joins
            if (p == Parameter.BOOK_GENRE) {
                final List<String> selectedGenres = extraParams.get(Parameter.BOOK_GENRE);

                if (ObjectUtils.isEmpty(selectedGenres) && ObjectUtils.isEmpty(selectedGenres.get(0))) {
                    continue;
                }

                query.add(env.getProperty("books.join.genre"));

                String genreName = selectedGenres.get(0);
                if (!AchievementUtils.isValidString(genreName)) {
                    throw new CreationException("Invalid value for genre name");
                }

                // Begin genre condition
                String tempCondition = insertParams(env.getProperty("books.condition.genre.name"), genreName);

                conditionalBuffer.add(tempCondition);

                for (int i = 1; i < selectedGenres.size(); i++) {
                    genreName = selectedGenres.get(i);
                    if (!AchievementUtils.isValidString(genreName)) {
                        throw new CreationException("Invalid value for genre name");
                    }
                    tempCondition = insertParams(env.getProperty("books.condition.add.genre.name"), genreName);
                    conditionalBuffer.add(tempCondition);
                }

                // End genre condition
                conditionalBuffer.add(")");
            }

            // Book queries
            if (p == Parameter.BOOK_PAGES) {
                final List<String> pageRange = extraParams.get(Parameter.BOOK_PAGES);

                if (ObjectUtils.isEmpty(pageRange)) {
                    continue;
                }

                final int startPage = Integer.parseInt(pageRange.get(0));
                final int finishPage = Integer.parseInt(pageRange.get(1));

                conditionalBuffer.add(insertParams(env.getProperty("books.condition.pages"), startPage, finishPage));
            }

            if (p == Parameter.BOOK_RATE_SUM) {
                final List<String> rateSumParam = extraParams.get(Parameter.BOOK_RATE_SUM);

                if (ObjectUtils.isEmpty(rateSumParam)) {
                    continue;
                }

                final int firstBound = Integer.parseInt(rateSumParam.get(0));
                final int secondBound = Integer.parseInt(rateSumParam.get(1));

                conditionalBuffer.add(insertParams(env.getProperty("books.condition.rate_sum"), firstBound, secondBound));
            }

            if (p == Parameter.BOOK_RELEASE) {
                final List<String> releaseParam = extraParams.get(Parameter.BOOK_RELEASE);

                if (ObjectUtils.isEmpty(releaseParam)) {
                    continue;
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date lowBound, highBound;

                try {
                    lowBound = dateFormat.parse(releaseParam.get(0));
                    highBound = dateFormat.parse(releaseParam.get(1));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new CreationException(e);
                }

                if (lowBound != null && highBound != null) {
                    conditionalBuffer.add(insertParams(env.getProperty("books.condition.release"), releaseParam.get(0), releaseParam.get(1)));
                }
            }

            if (p == Parameter.BOOK_PUBLISHING_HOUSE) {
                final List<String> bookPubHouseParam = extraParams.get(Parameter.BOOK_PUBLISHING_HOUSE);

                if (ObjectUtils.isEmpty(bookPubHouseParam)) {
                    continue;
                }

                final String pubHouse = bookPubHouseParam.get(0);

                if (pubHouse != null) {
                    conditionalBuffer.add(insertParams(env.getProperty("books.condition.publishing_house"), pubHouse));
                }
            }

            // Reserved book queries
            final int limit = AchievementUtils.extractLimit(p, extraParams);

            if (p == Parameter.RESERVED_BOOK_NEWEST) {
                conditionalBuffer.add(insertParams(env.getProperty("books.condition.newest"), limit));
            }

            if (p == Parameter.RESERVED_BOOK_LARGEST) {
                conditionalBuffer.add(insertParams(env.getProperty("books.condition.largest"), limit));
            }
            if (p == Parameter.RESERVED_BOOK_OLDER) {
                conditionalBuffer.add(insertParams(env.getProperty("books.condition.older"), limit));
            }

            if (p == Parameter.RESERVED_BOOK_RATED) {
                conditionalBuffer.add(insertParams(env.getProperty("books.condition.rated"), limit));
            }
        }

        query.add(conditionalBuffer.toString());

        System.out.println("Prepared book query : " + insertParams(query.toString(), count));

        return insertParams(query.toString(), count);
    }

    private static class AchievementUtils {

        private static final int DEFAULT_LIMIT_VALUE = 10;
        static final String DEFAULT_DESCRIPTION = "No description present";

        static boolean isValidString(String str) {
            return !str.contains("'") && !str.contains("\"");
        }


        static int extractLimit(Parameter parameter, Map<Parameter, List<String>> extraParams) {
            final List<String> limitParam = extraParams.get(parameter);

            if (ObjectUtils.isEmpty(limitParam) || ObjectUtils.isEmpty(limitParam.get(0))) {
                return DEFAULT_LIMIT_VALUE;
            }

            switch (parameter) {
                case BOOK_PAGES:
                case BOOK_RATE_SUM:
                case BOOK_PUBLISHING_HOUSE:
                case BOOK_GENRE:
                case BOOK_VOTERS_COUNT:
                case BOOK_RELEASE:
                case CREATION_TIME:
                    return DEFAULT_LIMIT_VALUE;
            }

            return Integer.parseInt(limitParam.get(0));
        }

        static boolean isValidVerb(Verb verb, TableName tableName) {
            switch (verb) {
                case HAS:
                    switch (tableName) {
                        case BOOK_REVIEWS:
                        case BOOK_OVERVIEWS:
                        case ANNOUNCEMENTS:
                        case BOOKS:
                            return false;
                        default:
                            return true;
                    }
                case PUBLISH:
                    switch (tableName) {
                        case BOOK_REVIEWS:
                        case BOOK_OVERVIEWS:
                        case ANNOUNCEMENTS:
                            return true;
                        default:
                            return false;
                    }
                case READ:
                    return TableName.BOOKS == tableName;
                default:
                    throw new IllegalArgumentException("Invalid verb value (only `HAS`, `READ`, `PUBLISH` are allowed)");
            }
        }
    }
}