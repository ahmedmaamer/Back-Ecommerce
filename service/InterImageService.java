package com.example.ecommerce.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.entities.Image;

public interface InterImageService {
    public Image addImage(MultipartFile file) throws IOException;
}
