package com.cabin.plat.config.image.service;

import com.cabin.plat.config.image.dto.ImageResponse;
import com.cabin.plat.config.image.mapper.ImageMapper;
import com.cabin.plat.global.util.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageMapper imageMapper;
    private final S3FileComponent s3FileComponent;

    @Override
    public ImageResponse.Avatar uploadAvatarImage(MultipartFile image) {
        return imageMapper.toAvatar(s3FileComponent.uploadFile("image", image));
    }
}
