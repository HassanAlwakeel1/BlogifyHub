package com.BlogifyHub.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryImageService {
    Map<String, Object> upload(byte[] fileBytes);

    Map<String, Object> upload(MultipartFile file);
}
