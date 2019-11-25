package com.netcraker.repositories;

import com.netcraker.model.Activity;

import java.util.List;

public interface ActivityRepository extends BaseOptionalRepository<Activity>{
    List<Activity> getActivityByUsersId(List<Integer> usersId, int size, int offset);
    List<Activity> getFriendsActivity(int userId, int size, int offset);
    int getTotalFriendsActivity(int userId);
}
