package com.smart.watchboard.common.support;

import com.smart.watchboard.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTask {
    private final SseService sseService;

    @Scheduled(fixedRate = 30000)
    public void executeNotifyTask() {
        sseService.notifyCycle();
    }
}
