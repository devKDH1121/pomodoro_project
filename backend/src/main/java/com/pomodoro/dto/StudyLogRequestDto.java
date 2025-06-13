package com.pomodoro.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudyLogRequestDto {
    private String memo;
    private int duration;
}
