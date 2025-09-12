package bag.livestock.controller;

import bag.livestock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.http.server.ServerHttpResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // Custom SseEmitter that sets the Content-Type header with charset=UTF-8
    public static class CustomSseEmitter extends SseEmitter {
        public CustomSseEmitter() {
            super();
        }

        public CustomSseEmitter(Long timeout) {
            super(timeout);
        }

        @Override
        protected void extendResponse(ServerHttpResponse outputMessage) {
            super.extendResponse(outputMessage);
            outputMessage.getHeaders().set("Content-Type", "text/event-stream;charset=UTF-8");
        }
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE + ";charset=UTF-8")
    public SseEmitter streamStockPrices() {
        // Set timeout to 1 hour (3600000 milliseconds)
        SseEmitter emitter = new CustomSseEmitter(3600000L);

        // Register completion and timeout callbacks
        emitter.onCompletion(() -> stockService.removeEmitter(emitter));
        emitter.onTimeout(() -> stockService.removeEmitter(emitter));

        // Set the Content-Type header to text/event-stream
        try {
            emitter.send(SseEmitter.event().comment(""));
        } catch (IOException e) {
            // If we can't send the initial event, the client connection is likely broken
            emitter.completeWithError(e);
            return emitter;
        }

        // Register the emitter with the service
        stockService.addEmitter(emitter);
        return emitter;
    }
}
