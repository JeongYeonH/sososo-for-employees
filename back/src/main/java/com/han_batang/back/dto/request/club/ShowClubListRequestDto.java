package com.han_batang.back.dto.request.club;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShowClubListRequestDto {
    private String showingType;
    private int page = 0;
    private int size = 8;
}
