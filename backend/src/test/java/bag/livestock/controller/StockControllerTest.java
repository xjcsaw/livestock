package bag.livestock.controller;

import bag.livestock.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.json.JSONArray;
import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StockController stockController;

    @Test
    void contextLoads() {
        // Verify that the controller is properly registered in the Spring context
        assert stockController != null;
    }

    @Test
    void testStreamEndpoint() throws Exception {
        // Test that the /api/stocks/stream endpoint returns an SseEmitter
        // and sets the appropriate content type header
        mockMvc.perform(get("/api/stocks/stream"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/event-stream;charset=UTF-8"));
    }

    @Autowired
    private StockService stockService;

    @Test
    void testStockServiceGeneratesValidStocks() {
        // Test that the StockService can generate stocks
        assertDoesNotThrow(() -> {
            // This will trigger the generation of random stocks
            // We're just testing that it doesn't throw an exception
            stockService.sendStockUpdates();
        }, "Stock generation should not throw an exception");
    }
}
