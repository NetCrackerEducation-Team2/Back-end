package com.netcraker.services.builders.imp;

import com.netcraker.exceptions.CreationException;
import com.netcraker.model.Achievement;
import com.netcraker.model.constants.Parameter;
import com.netcraker.model.constants.TableName;
import com.netcraker.model.constants.Verb;
import com.netcraker.model.vo.AchievementReq;
import com.netcraker.services.builders.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;


@Service
@RequiredArgsConstructor
@PropertySource("classpath:achievement-templates.properties")
public class AchievementBuilder implements Builder<AchievementReq, Achievement> {

    @Value("${common.template.count}")
    private String templateCount;
    @Value("${common.template.read}")
    private String templateRead;
    @Value("${common.template.publish}")
    private String templatePublish;

    private final Environment env;

    @Override
    public Achievement build(AchievementReq achievementReq) {
        final Verb verb = achievementReq.getVerb();
        final TableName subject = achievementReq.getSubject();
        final Map<Parameter, List<String>> extraParams = achievementReq.getExtraParams();
        final String name = achievementReq.getName();
        final int count = achievementReq.getCount();

        if (!isValidVerb(verb, subject)) {
            throw new CreationException("Invalid values for achievement");
        }

        String query;

        switch (verb) {
            case HAS:
                query = extraParams == null ? fulfillParams(templateCount, count, subject.name())
                        : createParameterizedQuery(extraParams, count);
                break;
            case READ:
                query = extraParams == null ? fulfillParams(templateRead, count, subject.name())
                        : createParameterizedQuery(extraParams, count);
                break;
            case PUBLISH:
                query = extraParams == null ? fulfillParams(templatePublish, count, subject.name())
                        : createParameterizedQuery(extraParams, count);
                break;
            default:
                throw new CreationException("Cannot create achievement");
        }

        String requirement = "User must " + verb.getVerb() + " " + count + " " + subject.getRepresentingName().toLowerCase();

        return Achievement.builder().name(name).sqlQuery(query).tableName(subject).requirement(requirement).build();
    }

    private String fulfillParams(String template, Object... params) {
        return String.format(template, params);
    }

    private String createParameterizedQuery(Map<Parameter, List<String>> extraParams, int count) {
        final StringJoiner query = new StringJoiner(" ");
        final StringJoiner conditionalBuffer = new StringJoiner(" ");

        final Set<Parameter> parameters = extraParams.keySet();
        query.add(env.getProperty("books.template.count"));
        conditionalBuffer.add(env.getProperty("books.condition.read")); // if verb == READ

        for (Parameter p : parameters) {
            String cond;

            // Joins
            if (p == Parameter.BOOK_GENRE) {
                final List<String> selectedGenres = extraParams.get(Parameter.BOOK_GENRE);

                if (ObjectUtils.isEmpty(selectedGenres) && ObjectUtils.isEmpty(selectedGenres.get(0))) {
                    continue;
                }

                query.add(env.getProperty("books.join.genre"));

                if (selectedGenres.size() == 1) {
                    cond = fulfillParams(env.getProperty("books.condition.genre.name"), selectedGenres.get(0));
                } else {
                    cond = fulfillParams(env.getProperty("books.condition.multiple.genre.name"), selectedGenres.get(0));
                }

                conditionalBuffer.add(cond);

                for (int i = 1; i < selectedGenres.size(); i++) {
                    String genreName = selectedGenres.get(i);
                    cond = fulfillParams(env.getProperty("books.condition.add.genre.name"), genreName);
                    if (i == selectedGenres.size() - 1) {
                        conditionalBuffer.add(cond + ")");
                    } else {
                        conditionalBuffer.add(cond);
                    }
                }
            }

            // Simple queries
            if (p == Parameter.BOOK_PAGES) {
                final List<String> pageRange = extraParams.get(Parameter.BOOK_PAGES);

                if (ObjectUtils.isEmpty(pageRange)) {
                    continue;
                }

                final int start = Integer.parseInt(pageRange.get(0));
                final int finish = Integer.parseInt(pageRange.get(1));

                conditionalBuffer.add(fulfillParams(env.getProperty("books.condition.pages"), start, finish));
            }

            if (p == Parameter.BOOK_RATE_SUM) {
                final List<String> rateSumParam = extraParams.get(Parameter.BOOK_RATE_SUM);

                if (ObjectUtils.isEmpty(rateSumParam)) {
                    continue;
                }
                final int firstBound = Integer.parseInt(rateSumParam.get(0));
                final int secondBound = Integer.parseInt(rateSumParam.get(1));

                conditionalBuffer.add(fulfillParams(env.getProperty("books.condition.rated"), firstBound, secondBound));
            }

            // fix
            if (p == Parameter.BOOK_RELEASE) {
                final List<String> releaseParam = extraParams.get(Parameter.BOOK_RELEASE);

                if (ObjectUtils.isEmpty(releaseParam)) {
                    continue;
                }
                final String firstBound = releaseParam.get(0);
                final String secondBound = releaseParam.get(1);

                // fix type to Date
                if (firstBound != null && secondBound != null) {
                    conditionalBuffer.add(fulfillParams(env.getProperty("books.condition.rated"), firstBound, secondBound));
                }
            }

            if (p == Parameter.BOOK_PUBLISHING_HOUSE) {
                final List<String> bookPubHouseParam = extraParams.get(Parameter.BOOK_PUBLISHING_HOUSE);

                if (ObjectUtils.isEmpty(bookPubHouseParam)) {
                    continue;
                }

                final String pubHouse = bookPubHouseParam.get(0);

                if (pubHouse != null) {
                    conditionalBuffer.add(fulfillParams(env.getProperty("books.condition.publishing_house"), pubHouse));
                }
            }

            // Reserved queries
            final int limit = AchievementUtils.extractLimit(p, extraParams);

            if (p == Parameter.RESERVED_BOOK_NEWEST) {
                conditionalBuffer.add(fulfillParams(env.getProperty("books.condition.newest"), limit));
            }

            if (p == Parameter.RESERVED_BOOK_LARGEST) {
                conditionalBuffer.add(fulfillParams(env.getProperty("books.condition.largest"), limit));
            }
            if (p == Parameter.RESERVED_BOOK_OLDER) {
                conditionalBuffer.add(fulfillParams(env.getProperty("books.condition.older"), limit));
            }

            if (p == Parameter.RESERVED_BOOK_RATED) {
                conditionalBuffer.add(fulfillParams(env.getProperty("books.condition.rated"), limit));
            }
        }

        query.add(conditionalBuffer.toString());

        System.out.println("Raw query : " + query.toString());
        System.out.println("Prepared query : " + fulfillParams(query.toString(), count));
        return fulfillParams(query.toString(), count);
    }

    private boolean isValidVerb(Verb verb, TableName tableName) {
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

    private static class AchievementUtils {

        private static final int DEFAULT_LIMIT_VALUE = 10;

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
                case CREATED_TIME:
                    return DEFAULT_LIMIT_VALUE;
            }

            return Integer.parseInt(limitParam.get(0));
        }
    }
}