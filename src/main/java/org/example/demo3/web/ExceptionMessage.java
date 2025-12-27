package org.example.demo3.web;

import java.time.LocalDateTime;

public record ExceptionMessage(
        String message,
        String detailedMessage,
        LocalDateTime errorTime
) {}
