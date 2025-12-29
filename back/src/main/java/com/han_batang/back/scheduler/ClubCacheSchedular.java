package com.han_batang.back.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.han_batang.back.service.ClubService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClubCacheSchedular {

    private final ClubService clubService;

    @Scheduled(fixedRate = 30_000)
    public void maintainTopCache(){
        clubService.maintainTopClubDetailCache();
    }
}
