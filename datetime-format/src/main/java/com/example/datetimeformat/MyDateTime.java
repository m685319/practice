package com.example.datetimeformat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyDateTime {
    @JsonFormat(
            pattern = "yyyy:MM:dd'##':HH:mm:ss:SSS",
            locale = "ru_RU",
            timezone = "UTC"
    )
    private LocalDateTime dateTime;
}
