package com.build.core_restful.util.upload;

import org.springframework.stereotype.Component;

// FileStorageProperties.java
@Component
public class FileStorageProperties {
    private final String uploadDir = "D:/6-DoAn/BackEnd/uploads/";

    public String getUploadDir() {
        return uploadDir;
    }
}
