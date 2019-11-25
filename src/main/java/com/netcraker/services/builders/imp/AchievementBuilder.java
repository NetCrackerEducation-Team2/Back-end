package com.netcraker.services.builders.imp;

import com.netcraker.exceptions.CreationException;
import com.netcraker.model.Achievement;
import com.netcraker.model.constants.TableName;
import com.netcraker.model.constants.Verb;
import com.netcraker.model.vo.AchievementReq;
import com.netcraker.services.builders.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;


@Service
@PropertySource("classpath:achievement-templates.properties")
public class AchievementBuilder implements Builder<AchievementReq, Achievement> {

    @Value("${common.hasCount}")
    private String countTemplate;
    @Value("${common.hasRead}")
    private String readTemplate;
    @Value("${common.hasWrite}")
    private String writeTemplate;
    @Value("${books.withGenre}")
    private String readGenreTemplate;

    @Override
    public Achievement build(AchievementReq achievementReq) {
        final Verb verb = achievementReq.getVerb();
        final TableName subject = achievementReq.getSubject();
        final String name = achievementReq.getName();
        final int count = achievementReq.getCount();

        if (!isValidAchievementReq(verb, subject)) {
            throw new CreationException("Invalid values for achievement");
        }

        String query;

        switch (verb) {
            case HAS:
                query = createSql(countTemplate, count, subject.getTableName());
                break;
            case READ:
                query = createSql(readTemplate, count, subject.getTableName());
                break;
            case PUBLISH:
                query = createSql(writeTemplate, count, subject.getTableName());
                break;
            default:
                throw new CreationException("Cannot create achievement");
        }

        return Achievement.builder().name(name).requirement(query).tableName(subject).build();
    }

    private String createSql(String template, Object... params) {
        return String.format(template, params);
    }

    private boolean isValidAchievementReq(Verb verb, TableName tableName) {
        switch (verb) {
            case HAS:
                switch (tableName) {
                    case BOOK_REVIEWS:
                    case ANNOUNCEMENTS:
                    case BOOKS:
                        return false;
                    default:
                        return true;

                }
            case READ:
                return TableName.BOOKS == tableName;
            case PUBLISH:
                switch (tableName) {
                    case BOOK_REVIEWS:
                    case ANNOUNCEMENTS:
                        return true;
                    default:
                        return false;

                }
            default:
                throw new CreationException("Invalid values for achievement");
        }
    }
}