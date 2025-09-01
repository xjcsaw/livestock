package bag.livestock.controller;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // Custom SseEmitter that sets the Content-Type header with charset=UTF-8
    public static class CustomSseEmitter extends SseEmitter {
        public CustomSseEmitter() {
            super();
        }

        @Override
        protected void extendResponse(ServerHttpResponse outputMessage) {
            super.extendResponse(outputMessage);
            outputMessage.getHeaders().set("Content-Type", "text/event-stream;charset=UTF-8");
        }
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
    public SseEmitter streamStockPrices() {
        SseEmitter emitter = new CustomSseEmitter();
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        // Set the Content-Type header to text/event-stream
        try {
            emitter.send(SseEmitter.event().comment(""));
        } catch (IOException e) {
            // If we can't send the initial event, the client connection is likely broken
            emitter.completeWithError(e);
            return emitter;
        }

        emitters.add(emitter);
        return emitter;
    }

    @Scheduled(fixedRate = 3000)
    public void sendStockUpdates() {
        String stockJson = generateRandomStockJson();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("stock-update").data(stockJson));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }

    private String generateRandomStockJson() {
        // Dummy data for demo purposes
        double price = 180 + Math.random() * 10;
        return "{\"symbol\":\"AAPL\",\"price\":" + String.format(java.util.Locale.US, "%.2f", price) + "}";
    }
}
