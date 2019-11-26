package com.netcraker.controllers;

import com.netcraker.model.PublishingHouse;
import com.netcraker.services.PublishingHouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping({"/api"})
@CrossOrigin(methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublishingHouseController {
    private final PublishingHouseService publishingHouseService;
    @GetMapping
    public List<PublishingHouse> getPublishingHouses() {
        return publishingHouseService.getPublishingHouses();
    }
}
