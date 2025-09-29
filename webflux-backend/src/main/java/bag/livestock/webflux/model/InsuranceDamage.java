package bag.livestock.webflux.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Model class representing insurance damage statistics.
 */
public class InsuranceDamage {
    private String damageType;
    private int newReportsCount;
    private BigDecimal totalPayoutAmount;
    private BigDecimal averagePayoutAmount;
    private int pendingClaimsCount;
    private int processedClaimsCount;
    private LocalDateTime lastUpdate;

    // Default constructor for JSON serialization
    public InsuranceDamage() {
    }

    // Constructor with all fields
    public InsuranceDamage(String damageType, int newReportsCount, BigDecimal totalPayoutAmount,
                          BigDecimal averagePayoutAmount, int pendingClaimsCount, 
                          int processedClaimsCount, LocalDateTime lastUpdate) {
        this.damageType = damageType;
        this.newReportsCount = newReportsCount;
        this.totalPayoutAmount = totalPayoutAmount;
        this.averagePayoutAmount = averagePayoutAmount;
        this.pendingClaimsCount = pendingClaimsCount;
        this.processedClaimsCount = processedClaimsCount;
        this.lastUpdate = lastUpdate;
    }

    // Getters and setters
    public String getDamageType() {
        return damageType;
    }

    public void setDamageType(String damageType) {
        this.damageType = damageType;
    }

    public int getNewReportsCount() {
        return newReportsCount;
    }

    public void setNewReportsCount(int newReportsCount) {
        this.newReportsCount = newReportsCount;
    }

    public BigDecimal getTotalPayoutAmount() {
        return totalPayoutAmount;
    }

    public void setTotalPayoutAmount(BigDecimal totalPayoutAmount) {
        this.totalPayoutAmount = totalPayoutAmount;
    }

    public BigDecimal getAveragePayoutAmount() {
        return averagePayoutAmount;
    }

    public void setAveragePayoutAmount(BigDecimal averagePayoutAmount) {
        this.averagePayoutAmount = averagePayoutAmount;
    }

    public int getPendingClaimsCount() {
        return pendingClaimsCount;
    }

    public void setPendingClaimsCount(int pendingClaimsCount) {
        this.pendingClaimsCount = pendingClaimsCount;
    }

    public int getProcessedClaimsCount() {
        return processedClaimsCount;
    }

    public void setProcessedClaimsCount(int processedClaimsCount) {
        this.processedClaimsCount = processedClaimsCount;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
