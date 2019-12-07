package com.netcraker.services.impl;

import com.netcraker.services.FileService;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Service
@PropertySource("classpath:path.properties")
@RequiredArgsConstructor
public class FileServiceImp implements FileService {

    private final ResourceLoader resourceLoader;

    @Value("${files.contentPath}")
    private String contentPath;
    @Value("${files.imagePath}")
    private String imagePath;

    @Override
    public void downloadFile(String filePath, HttpServletResponse response) {
        Resource resource = resourceLoader.getResource(contentPath + filePath);
        try {
            InputStream is = resource.getInputStream();
            FileCopyUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] getImage(String filePath) {
        Resource resource = resourceLoader.getResource(imagePath + filePath);
        if(!resource.exists()) return null;
        byte[] image = null;
        try {
            InputStream is = resource.getInputStream();
            image = IOUtils.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public String getImageBase64(String filePath) {
        return Base64.encode(getImage(filePath));
    }
}
