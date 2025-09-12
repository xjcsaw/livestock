package bag.livestock.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Model class representing stock data with fields similar to Binance's cryptocurrency data.
 */
public class Stock {
    private String symbol;
    private BigDecimal price;
    private BigDecimal priceChange;
    private BigDecimal priceChangePercent;
    private BigDecimal high24h;
    private BigDecimal low24h;
    private BigDecimal volume;
    private LocalDateTime lastUpdate;

    // Default constructor for JSON serialization
    public Stock() {
    }

    // Constructor with all fields
    public Stock(String symbol, BigDecimal price, BigDecimal priceChange, BigDecimal priceChangePercent,
                 BigDecimal high24h, BigDecimal low24h, BigDecimal volume, LocalDateTime lastUpdate) {
        this.symbol = symbol;
        this.price = price;
        this.priceChange = priceChange;
        this.priceChangePercent = priceChangePercent;
        this.high24h = high24h;
        this.low24h = low24h;
        this.volume = volume;
        this.lastUpdate = lastUpdate;
    }

    // Getters and setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(BigDecimal priceChange) {
        this.priceChange = priceChange;
    }

    public BigDecimal getPriceChangePercent() {
        return priceChangePercent;
    }

    public void setPriceChangePercent(BigDecimal priceChangePercent) {
        this.priceChangePercent = priceChangePercent;
    }

    public BigDecimal getHigh24h() {
        return high24h;
    }

    public void setHigh24h(BigDecimal high24h) {
        this.high24h = high24h;
    }

    public BigDecimal getLow24h() {
        return low24h;
    }

    public void setLow24h(BigDecimal low24h) {
        this.low24h = low24h;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
