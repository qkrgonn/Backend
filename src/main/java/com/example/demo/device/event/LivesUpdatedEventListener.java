package com.example.demo.device.event;

import com.example.demo.common.sse.LivesSseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class LivesUpdatedEventListener {

    private final LivesSseManager sse;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onLivesUpdated(LivesUpdatedEvent event) {
        sse.publishLives(event.userId(), event.payload());
    }
}
