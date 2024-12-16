package com.han_batang.back.dto.request.club;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinClubRequestDto {
    
    @NotNull
    private Integer clubId;

    @NotBlank
    private String joinerUserId;

}
