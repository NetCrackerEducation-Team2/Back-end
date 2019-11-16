package com.netcraker.services.impl;

import com.netcraker.services.FileService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Service
@PropertySource("classpath:path.properties")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileServiceImp implements FileService {

    private final @NonNull ResourceLoader resourceLoader;

    @Override
    public void downloadFile(String filePath, HttpServletResponse response) {
        Resource resource = resourceLoader.getResource(filePath);
        try {
            InputStream is = resource.getInputStream();
            FileCopyUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] getImage(String imagePath) {
        Resource resource = resourceLoader.getResource(imagePath);
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
}
