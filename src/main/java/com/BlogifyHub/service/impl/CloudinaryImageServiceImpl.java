package com.BlogifyHub.service.impl;

import com.BlogifyHub.service.CloudinaryImageService;
import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryImageServiceImpl implements CloudinaryImageService {
    private Cloudinary cloudinary;

    public CloudinaryImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Map upload(MultipartFile file) {
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(),Map.of());
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Image uploading Failed..");
        }
    }
}
