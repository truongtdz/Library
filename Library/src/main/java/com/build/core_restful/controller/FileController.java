package com.build.core_restful.controller;

import java.io.IOException;
import java.util.List;

import com.build.core_restful.util.upload.CloudinaryUpload;
import com.build.core_restful.util.upload.ServerUpload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.build.core_restful.util.annotation.AddMessage;

@RestController
@RequestMapping("/api/v1/upload")
public class FileController {
    private final ServerUpload serverUpload;
    private final CloudinaryUpload cloudinaryUpload;

    public FileController(ServerUpload serverUpload, CloudinaryUpload cloudinaryUpload) {
        this.serverUpload = serverUpload;
        this.cloudinaryUpload = cloudinaryUpload;
    }

    @PostMapping("/server")
    @AddMessage("Upload file to server")
    public ResponseEntity<List<String>> uploadManyFileToServer(@RequestParam List<MultipartFile> files){
        return ResponseEntity.ok(serverUpload.storeMultipleFiles(files));
    }

    @PostMapping("/cloudinary")
    @AddMessage("Upload file to cloudinary")
    public ResponseEntity<List<String>> uploadManyFileToCloudinary(@RequestParam List<MultipartFile> files) throws IOException{
        return ResponseEntity.ok(cloudinaryUpload.uploadMultipartFile(files));
    }
}
