package com.netcraker.services;

import javax.servlet.http.HttpServletResponse;

public interface FileService {
    void downloadFile(String filePath, HttpServletResponse response);
    byte[] getImage(String filePath);
    String getImageBase64(String filePath);
}
