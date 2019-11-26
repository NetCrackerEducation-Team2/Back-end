package com.netcraker.repositories;

import com.netcraker.model.PublishingHouse;

import java.util.List;

public interface PublishingHouseRepository extends BaseOptionalRepository<PublishingHouse> {
    List<PublishingHouse> getPublishingHouses();
}