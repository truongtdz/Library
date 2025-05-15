package com.build.core_restful.util.upload;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryUpload {
    private final Cloudinary cloudinary;

    public CloudinaryUpload(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        return this.cloudinary.uploader()
                .upload(file.getBytes(), Map.of("public_id", UUID.randomUUID().toString()))
                .get("url").toString();
    }

    public List<String> uploadMultipartFile(List<MultipartFile> files) throws IOException {
        List<String> fileNames = new ArrayList<>();

        for(MultipartFile file: files){
            String fileName = this.cloudinary.uploader()
                    .upload(file.getBytes(), Map.of("public_id", UUID.randomUUID().toString()))
                    .get("url").toString();

            fileNames.add(fileName);
        }

        return fileNames;
    }
}
