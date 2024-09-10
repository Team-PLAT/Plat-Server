package com.cabin.plat.config.image.service;

import com.cabin.plat.config.image.dto.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageResponse.Avatar uploadAvatarImage(MultipartFile image);
}
