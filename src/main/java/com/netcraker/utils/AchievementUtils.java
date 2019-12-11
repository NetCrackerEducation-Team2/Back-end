package com.netcraker.utils;

import com.netcraker.model.constants.Parameter;
import com.netcraker.model.constants.TableName;
import com.netcraker.model.constants.Verb;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

public final class AchievementUtils {

    private static final int DEFAULT_LIMIT_VALUE = 10;
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DESCRIPTION = "No description present";

    public static boolean isNonValidString(String str) {
        return str.contains("'") || str.contains("\"");
    }

    public static int extractLimit(Parameter parameter, Map<Parameter, List<String>> extraParams) {
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

    public static boolean isValidVerb(Verb verb, TableName tableName) {
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

    public static String insertParams(String template, Object... params) {
        return String.format(template, params);
    }
}