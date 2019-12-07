package com.netcraker.services;

import javax.servlet.http.HttpServletResponse;

public interface FileService {
    void downloadFile(String path, HttpServletResponse response);
    byte[] getImage(String path);
    String getImageBase64(String path);
}
