package com.cabin.plat.config.image.controller;

import com.cabin.plat.config.image.dto.ImageResponse;
import com.cabin.plat.config.image.service.ImageService;
import com.cabin.plat.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
@Tag(name = "이미지 API")
public class ImageController {
    private final ImageService imageService;

    @Operation(summary = "사진 업로드", description = "사진을 업로드하여 해당 사진의 URL 을 반환합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<ImageResponse.Avatar> uploadAvatarImage(@RequestPart(value = "image") MultipartFile image) {
        return BaseResponse.onSuccess(imageService.uploadAvatarImage(image));
    }
}
