package com.cabin.plat.domain.track.controller;

import com.cabin.plat.domain.track.service.TrackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/tracks")
@Tag(name = "트랙 API", description = "트랙 관련 API 입니다.")
public class TrackController {
    private final TrackService trackService;


}
