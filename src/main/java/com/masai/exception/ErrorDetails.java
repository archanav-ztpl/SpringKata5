package com.masai.exception;

import java.time.LocalDateTime;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorDetails {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private String details;
}
