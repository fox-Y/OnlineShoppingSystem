package org.example.onlineshoppingsystem.common.dto;

import java.time.Instant;

public record ApiError (
    Instant timestamp,
    int status,
    String error,
    String message,
    String path
) {}
