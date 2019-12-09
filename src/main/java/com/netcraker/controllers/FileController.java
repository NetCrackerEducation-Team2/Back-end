package com.netcraker.controllers;

import com.netcraker.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping({"/api/file"})
@RequiredArgsConstructor
@CrossOrigin(methods={RequestMethod.OPTIONS, RequestMethod.GET})
public class FileController {

    private final FileService fileService;

    @GetMapping("/download/{filePath}")
    public void downloadBook(@PathVariable String filePath, HttpServletResponse response){
        fileService.downloadFile(filePath, response);
    }

    @GetMapping("/image/{filePath}")
    public ResponseEntity<?> getImage(@PathVariable String filePath){
        return new ResponseEntity<>(fileService.getImageBase64(filePath), HttpStatus.OK);
    }
}