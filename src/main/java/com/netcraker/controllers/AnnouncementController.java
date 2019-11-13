package com.netcraker.controllers;

import com.netcraker.model.Announcement;
import com.netcraker.model.Page;
import com.netcraker.services.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping({"/api"})
@CrossOrigin(methods={RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        Assert.notNull(announcementService, "AnnouncementService must not be null!");
        this.announcementService = announcementService;
    }

    @GetMapping("/announcements")
    public Page<Announcement> getAnnouncements() {

        return announcementService.getAnnouncements(0);
    }

    @GetMapping("/announcements/{id}")
    public Announcement getAnnouncementById(@PathVariable int id){
        return announcementService.getAnnouncementById(id);
    }

    @PostMapping("/announcements")
    public Announcement createAnnouncement(){
        /*Announcement announcement = Announcement.builder()
                .title("Test Insert")
                .description("This is a testing of insert")
                .userId(5)
                .bookId(4)
                .published(false).build();
        return announcementService.addAnnouncement(announcement);*/
        return null;
    }

    @PutMapping("/announcements")
    public Announcement updateAnnouncement(){
        /*Announcement announcement = Announcement.builder()
                .announcementId(4)
                .title("Test Insert")
                .description("This is a testing of update")
                .userId(5)
                .bookId(4)
                .published(false).build();
        return announcementService.updateAnnouncement(announcement);*/
        return null;
    }

    @DeleteMapping("/announcements")
    public boolean deleteAnnouncement(){
        //return announcementService.deleteAnnouncement(4);
        return false;
    }


}
