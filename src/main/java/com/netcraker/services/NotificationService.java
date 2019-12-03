package com.netcraker.services;

/*Notification types:
     (10, 'announcements')
     (11, 'book_reviews')
     (12, 'book_overviews')
     (13, 'invitations')
     (14, 'activities')
     (15, 'achievements')

      Notification messages:
      (10, 'Hello')
      (11, 'You have new invitation to friend.')
      (12, 'You created new announcement. Please wait until our moderator will publish your announcement.')
      (13, 'You created new book review. Please wait until our moderator will publish your book review.')
      (14, 'Your book review was published by moderator.')
      (15, 'Your announcement was published by moderator.')
      (16, 'Your book review was published by moderator.')
*/

import com.netcraker.model.Page;
import com.netcraker.model.constants.NotificationTypeMessage;
import com.netcraker.model.constants.NotificationTypeName;
import com.netcraker.model.vo.NotificationMessage;


public interface NotificationService {
    Page<NotificationMessage> getUserNotification(int id, int page, int pageSize);
    <T> boolean sendNotification(NotificationTypeName notificationTypeName, NotificationTypeMessage notificationTypeMessage, T entity);
}
