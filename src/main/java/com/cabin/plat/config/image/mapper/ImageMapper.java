package com.cabin.plat.config.image.mapper;

import com.cabin.plat.config.image.dto.ImageResponse;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {
    public ImageResponse.Avatar toAvatar(String avatar) {
        return ImageResponse.Avatar.builder()
                .avatar(avatar)
                .build();
    }
}
