package bag.livestock.service;

import bag.livestock.model.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service responsible for managing stock data and updates.
 */
@Service
public class StockService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper;
    private final Random random = new Random();

    // List of cryptocurrency symbols similar to Binance
    private final String[] symbols = {
        "BTC/USDT", "ETH/USDT", "BNB/USDT", "SOL/USDT", "XRP/USDT", 
        "ADA/USDT", "DOGE/USDT", "MATIC/USDT", "DOT/USDT", "AVAX/USDT"
    };

    // Base prices for each symbol (approximate values)
    private final BigDecimal[] basePrices = {
        new BigDecimal("42000"), // BTC
        new BigDecimal("2200"),  // ETH
        new BigDecimal("300"),   // BNB
        new BigDecimal("100"),   // SOL
        new BigDecimal("0.5"),   // XRP
        new BigDecimal("0.4"),   // ADA
        new BigDecimal("0.08"),  // DOGE
        new BigDecimal("0.7"),   // MATIC
        new BigDecimal("6"),     // DOT
        new BigDecimal("35")     // AVAX
    };

    // Keep track of the last prices to calculate changes
    private final List<Stock> lastStocks = new ArrayList<>();

    public StockService() {
        this.objectMapper = new ObjectMapper();
        // Configure ObjectMapper to handle Java 8 date/time types
        objectMapper.findAndRegisterModules();
        
        // Initialize the lastStocks list
        for (int i = 0; i < symbols.length; i++) {
            Stock stock = new Stock();
            stock.setSymbol(symbols[i]);
            stock.setPrice(basePrices[i]);
            stock.setPriceChange(BigDecimal.ZERO);
            stock.setPriceChangePercent(BigDecimal.ZERO);
            stock.setHigh24h(basePrices[i]);
            stock.setLow24h(basePrices[i]);
            stock.setVolume(new BigDecimal("1000000").multiply(basePrices[i].divide(new BigDecimal("1000"), 8, RoundingMode.HALF_UP)));
            stock.setLastUpdate(LocalDateTime.now());
            lastStocks.add(stock);
        }
    }

    /**
     * Register a new SSE emitter
     * @param emitter The emitter to register
     */
    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
    }

    /**
     * Remove an SSE emitter
     * @param emitter The emitter to remove
     */
    public void removeEmitter(SseEmitter emitter) {
        emitters.remove(emitter);
    }

    /**
     * Scheduled method to send stock updates to all registered emitters
     */
    @Scheduled(fixedRate = 3000)
    public void sendStockUpdates() {
        try {
            List<Stock> stocks = generateRandomStocks();
            String stocksJson = objectMapper.writeValueAsString(stocks);

            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event().name("stock-update").data(stocksJson));
                } catch (IOException e) {
                    emitters.remove(emitter);
                }
            }
        } catch (JsonProcessingException e) {
            System.err.println("Error serializing stocks to JSON: " + e.getMessage());
        }
    }

    /**
     * Generate random stock data
     * @return List of Stock objects with updated values
     */
    private List<Stock> generateRandomStocks() {
        List<Stock> stocks = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < symbols.length; i++) {
            Stock lastStock = lastStocks.get(i);

            // Generate a new price with small random change
            BigDecimal changePercent = new BigDecimal(random.nextDouble() * 2 - 1)  // -1% to +1%
                    .divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP);

            BigDecimal newPrice = lastStock.getPrice()
                    .multiply(BigDecimal.ONE.add(changePercent))
                    .setScale(8, RoundingMode.HALF_UP);

            // Calculate price change from base price
            BigDecimal priceChange = newPrice.subtract(basePrices[i]);
            BigDecimal priceChangePercent = priceChange
                    .divide(basePrices[i], 8, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .setScale(2, RoundingMode.HALF_UP);

            // Update 24h high/low if needed
            BigDecimal high24h = lastStock.getHigh24h();
            if (newPrice.compareTo(high24h) > 0) {
                high24h = newPrice;
            }

            BigDecimal low24h = lastStock.getLow24h();
            if (newPrice.compareTo(low24h) < 0) {
                low24h = newPrice;
            }

            // Generate random volume change
            BigDecimal volumeChange = new BigDecimal(random.nextDouble() * 0.1 - 0.05)  // -5% to +5%
                    .multiply(lastStock.getVolume());
            BigDecimal newVolume = lastStock.getVolume().add(volumeChange).setScale(2, RoundingMode.HALF_UP);

            // Create new stock with updated values
            Stock stock = new Stock(
                symbols[i],
                newPrice,
                priceChange.setScale(8, RoundingMode.HALF_UP),
                priceChangePercent,
                high24h,
                low24h,
                newVolume,
                now
            );

            stocks.add(stock);
            lastStocks.set(i, stock);  // Update last stock
        }

        return stocks;
    }
}
