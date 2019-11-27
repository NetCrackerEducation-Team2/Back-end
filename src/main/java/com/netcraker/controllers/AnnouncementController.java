package com.netcraker.controllers;

import com.netcraker.exceptions.CreationException;
import com.netcraker.exceptions.UpdateException;
import com.netcraker.model.Announcement;
import com.netcraker.model.Page;
import com.netcraker.services.AnnouncementService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping({"/api/announcements"})
@CrossOrigin(methods={RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor
public class AnnouncementController {


    final private AnnouncementService announcementService;

    @GetMapping
    public ResponseEntity<Page<Announcement>> getAnnouncements(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int pageSize) {
        Page<Announcement> pagination = announcementService.getAnnouncementsPagination(page, pageSize);
        return new ResponseEntity<>(pagination, HttpStatus.OK);
    }

    @GetMapping("/published")
    public ResponseEntity<Page<Announcement>> getPublishAnnouncements(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int pageSize) {
        Page<Announcement> pagination = announcementService.getPublishAnnouncementsPagination(page, pageSize);
        return new ResponseEntity<>(pagination, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getAnnouncementById(@PathVariable int id){
        return ResponseEntity.ok().body(announcementService.getAnnouncementById(id)
                .orElseThrow(() -> new CreationException("Cannot find announcement by id")));
    }

    @PostMapping
    public ResponseEntity<Announcement> createAnnouncement(@RequestBody @Validated Announcement announcement,
                                                           BindingResult result){
        if (result.hasErrors()) {
            throw new CreationException("Cannot create announcement");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(announcementService.addAnnouncement(announcement)
                        .orElseThrow(() -> new CreationException("Cannot create announcement")));
    }

    @PutMapping
    public ResponseEntity<Announcement> updateAnnouncement(@RequestBody @Validated Announcement announcement){
        return ResponseEntity.ok()
                .body(announcementService.updateAnnouncement(announcement)
                        .orElseThrow(() -> new UpdateException("Cannot update announcement")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable int id){
        return ResponseEntity.ok().body(announcementService.deleteAnnouncement(id));
    }

    @PutMapping("/publish/{id}")
    public ResponseEntity<?> publishAnnouncement(@PathVariable int id){
        announcementService.publishAnnouncement(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/unpublish/{id}")
    public ResponseEntity<?> unpublishAnnouncement(@PathVariable int id){
        announcementService.unpublishAnnouncement(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }



}
