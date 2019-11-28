package com.netcraker.model.constants;


public enum TableName {
    BOOKS("books"),
    USERS("users"),
    USERS_BOOKS("users books"),
    BOOK_REVIEWS("book reviews"),
    BOOK_OVERVIEWS("book overviews"),
    REVIEW_COMMENTS("review comments"),
    MESSAGES("messages"),
    ANNOUNCEMENTS("announcements"),
    ACTIVITIES("activities"),
    FRIENDS("friends"),
    INVITATIONS("invitations"),
    SETTINGS("settings"),
    ACHIEVEMENTS("achievements"),
    USERS_ACHIEVEMENTS("user achievements");

    private String representingName;

    TableName(String representingName) {
        this.representingName = representingName;
    }

    public String getRepresentingName() {
        return representingName;
    }
}