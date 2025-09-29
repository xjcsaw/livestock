package bag.livestock.webflux.service;

import bag.livestock.webflux.model.InsuranceDamage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Service responsible for generating and streaming insurance damage statistics.
 * This service demonstrates the reactive programming model of Spring WebFlux.
 */
@Service
public class InsuranceDamageService {

    private final Random random = new Random();

    // Types of insurance damages
    private final String[] damageTypes = {
        "Fire", "Flood", "Theft", "Vehicle", "Property", "Liability", 
        "Health", "Travel", "Pet", "Business"
    };

    // Base values for statistics
    private final int[] baseNewReports = {
        15, 8, 20, 45, 30, 12, 60, 5, 3, 10
    };

    private final BigDecimal[] baseTotalPayouts = {
        new BigDecimal("50000"), // Fire
        new BigDecimal("35000"), // Flood
        new BigDecimal("8000"),  // Theft
        new BigDecimal("12000"), // Vehicle
        new BigDecimal("25000"), // Property
        new BigDecimal("40000"), // Liability
        new BigDecimal("5000"),  // Health
        new BigDecimal("2000"),  // Travel
        new BigDecimal("800"),   // Pet
        new BigDecimal("30000")  // Business
    };

    // Keep track of the last statistics to simulate realistic changes
    private final List<InsuranceDamage> lastStatistics = new ArrayList<>();

    public InsuranceDamageService() {
        // Initialize the lastStatistics list
        for (int i = 0; i < damageTypes.length; i++) {
            InsuranceDamage stat = new InsuranceDamage();
            stat.setDamageType(damageTypes[i]);
            stat.setNewReportsCount(baseNewReports[i]);
            stat.setTotalPayoutAmount(baseTotalPayouts[i]);
            stat.setAveragePayoutAmount(baseTotalPayouts[i].divide(new BigDecimal(baseNewReports[i]), 2, RoundingMode.HALF_UP));
            stat.setPendingClaimsCount(baseNewReports[i] * 3);
            stat.setProcessedClaimsCount(baseNewReports[i] * 10);
            stat.setLastUpdate(LocalDateTime.now());
            lastStatistics.add(stat);
        }
    }

    /**
     * Get all current insurance damage statistics as a Flux.
     * This method returns a single emission of all statistics.
     * 
     * @return Flux of InsuranceDamage objects
     */
    public Flux<InsuranceDamage> getAllStatistics() {
        return Flux.fromIterable(generateRandomStatistics());
    }

    /**
     * Get a specific insurance damage statistic by type.
     * 
     * @param type The damage type to retrieve
     * @return Mono of InsuranceDamage for the specified type
     */
    public Mono<InsuranceDamage> getStatisticsByType(String type) {
        return getAllStatistics()
                .filter(stat -> stat.getDamageType().equalsIgnoreCase(type))
                .next()
                .switchIfEmpty(Mono.error(new RuntimeException("Damage type not found: " + type)));
    }

    /**
     * Stream insurance damage statistics with updates every 2 seconds.
     * This demonstrates the reactive streaming capabilities of WebFlux.
     * 
     * @return Flux of InsuranceDamage objects that continuously updates
     */
    public Flux<InsuranceDamage> streamStatistics() {
        return Flux.interval(Duration.ofSeconds(2))
                .onBackpressureDrop()
                .flatMapIterable(i -> generateRandomStatistics())
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Generate random insurance damage statistics.
     * 
     * @return List of InsuranceDamage objects with updated values
     */
    private List<InsuranceDamage> generateRandomStatistics() {
        List<InsuranceDamage> statistics = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < damageTypes.length; i++) {
            InsuranceDamage lastStat = lastStatistics.get(i);

            // Generate new reports count with small random change
            int changeReports = random.nextInt(5) - 2; // -2 to +2
            int newReportsCount = Math.max(1, lastStat.getNewReportsCount() + changeReports);

            // Generate new total payout with small random change
            BigDecimal changePercent = new BigDecimal(random.nextDouble() * 0.04 - 0.02) // -2% to +2%
                    .setScale(4, RoundingMode.HALF_UP);
            
            BigDecimal newTotalPayout = lastStat.getTotalPayoutAmount()
                    .multiply(BigDecimal.ONE.add(changePercent))
                    .setScale(2, RoundingMode.HALF_UP);

            // Calculate average payout
            BigDecimal averagePayout = newTotalPayout.divide(new BigDecimal(newReportsCount), 2, RoundingMode.HALF_UP);

            // Update pending and processed claims
            int pendingChange = random.nextInt(5) - 2; // -2 to +2
            int pendingClaims = Math.max(0, lastStat.getPendingClaimsCount() + pendingChange);
            
            int processedChange = random.nextInt(8) - 1; // -1 to +7 (more likely to increase)
            int processedClaims = lastStat.getProcessedClaimsCount() + processedChange;

            // Create new statistic with updated values
            InsuranceDamage stat = new InsuranceDamage(
                damageTypes[i],
                newReportsCount,
                newTotalPayout,
                averagePayout,
                pendingClaims,
                processedClaims,
                now
            );

            statistics.add(stat);
            lastStatistics.set(i, stat); // Update last statistic
        }

        return statistics;
    }
}
