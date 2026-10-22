package com.springboot.library.dto.response;

import java.time.Instant;

public record ErrorRespDto(
        String message,
        String comments,
        Instant timeStamp
) {
}
