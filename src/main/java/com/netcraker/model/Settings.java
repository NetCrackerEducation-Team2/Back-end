package com.netcraker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Settings {
    private Integer settingsId;
    private Integer userId;
    private Boolean disableNotifications;
    private Boolean makePrivate;
    private Boolean showMyFavouriteBooks;
    private Boolean showMyAnnouncements;
    private Boolean showMyBookReviews;
    private Boolean showMyBookOverviews;
}
