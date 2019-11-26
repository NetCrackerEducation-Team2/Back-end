package com.netcraker.services.impl;

import com.netcraker.model.PublishingHouse;
import com.netcraker.repositories.PublishingHouseRepository;
import com.netcraker.services.PublishingHouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublishingHouseImpl implements PublishingHouseService {
    private final PublishingHouseRepository repository;
    @Override
    public List<PublishingHouse> getPublishingHouses() {
        return repository.getPublishingHouses();
    }
}
