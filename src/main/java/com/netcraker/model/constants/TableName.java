package com.netcraker.model.constants;


public enum TableName {
    BOOKS("books"),
    USERS("users"),
    USERS_BOOKS("users_books"),
    BOOK_REVIEWS("book_reviews"),
    BOOK_OVERVIEWS("book_overviews"),
    REVIEW_COMMENTS("review_comments"),
    MESSAGES("messages"),
    ANNOUNCEMENTS("announcements"),
    ACTIVITIES("activities"),
    FRIENDS("friends"),
    INVITATIONS("invitations"),
    SETTINGS("settings"),
    ACHIEVEMENTS("achievements"),
    USERS_ACHIEVEMENTS("users_achievements")


    ;

    private String tableName;

    TableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
    }

//public final class TableName {
//    public static final String BOOKS = "books";
//    public static final String USERS = "users";
//    public static final String USERS_BOOKS = "users_books";
//    public static final String BOOK_REVIEWS = "book_reviews";
//    public static final String REVIEW_COMMENTS = "review_comments";
//    public static final String MESSAGES = "messages";
//    public static final String ANNOUNCEMENTS = "announcements";
//    public static final String ACTIVITIES = "activities";
//    public static final String FRIENDS = "friends";

//    public static boolean equals(String table1, String table2) {
//        if(table1!=null && table2!=null) {
//
//        }
//    }
//}
