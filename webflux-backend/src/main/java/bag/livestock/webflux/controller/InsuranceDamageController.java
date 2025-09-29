package bag.livestock.webflux.controller;

import bag.livestock.webflux.model.InsuranceDamage;
import bag.livestock.webflux.service.InsuranceDamageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for insurance damage statistics.
 * This controller demonstrates the reactive programming model of Spring WebFlux.
 */
@RestController
@RequestMapping("/api/insurance")
public class InsuranceDamageController {

    private final InsuranceDamageService insuranceDamageService;

    @Autowired
    public InsuranceDamageController(InsuranceDamageService insuranceDamageService) {
        this.insuranceDamageService = insuranceDamageService;
    }

    /**
     * Get all insurance damage statistics.
     * This endpoint returns a JSON array of all statistics.
     * 
     * @return Flux of InsuranceDamage objects
     */
    @GetMapping
    public Flux<InsuranceDamage> getAllStatistics() {
        return insuranceDamageService.getAllStatistics();
    }

    /**
     * Get a specific insurance damage statistic by type.
     * 
     * @param type The damage type to retrieve
     * @return Mono of InsuranceDamage for the specified type
     */
    @GetMapping("/{type}")
    public Mono<InsuranceDamage> getStatisticsByType(@PathVariable String type) {
        return insuranceDamageService.getStatisticsByType(type);
    }

    /**
     * Stream insurance damage statistics as Server-Sent Events (SSE).
     * This endpoint demonstrates the streaming capabilities of WebFlux.
     * 
     * @return Flux of InsuranceDamage objects as SSE
     */
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<InsuranceDamage> streamStatistics() {
        return insuranceDamageService.streamStatistics();
    }

    /**
     * Stream statistics for a specific damage type as Server-Sent Events (SSE).
     * 
     * @param type The damage type to stream
     * @return Flux of InsuranceDamage objects for the specified type as SSE
     */
    @GetMapping(path = "/stream/{type}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<InsuranceDamage> streamStatisticsByType(@PathVariable String type) {
        return insuranceDamageService.streamStatistics()
                .filter(stat -> stat.getDamageType().equalsIgnoreCase(type));
    }
}
