package com.netcraker.services;

import javax.servlet.http.HttpServletResponse;

public interface FileService {
    void downloadFile(String fileName, HttpServletResponse response);
    byte[] getImage(String imageName);
}
