package bag.livestock.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.json.JSONObject;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StockController stockController;

    @Test
    public void contextLoads() {
        // Verify that the controller is properly registered in the Spring context
        assert stockController != null;
    }

    @Test
    public void testStreamEndpoint() throws Exception {
        // Test that the /api/stocks/stream endpoint returns an SseEmitter
        // and sets the appropriate content type header
        mockMvc.perform(get("/api/stocks/stream"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/event-stream;charset=UTF-8"));
    }

    @Test
    public void testGenerateRandomStockJson() throws Exception {
        // Use reflection to access the private method
        Method generateRandomStockJsonMethod = StockController.class.getDeclaredMethod("generateRandomStockJson");
        generateRandomStockJsonMethod.setAccessible(true);

        // Get the JSON string from the method
        String jsonString = (String) generateRandomStockJsonMethod.invoke(stockController);

        // Verify that the JSON can be parsed without errors
        assertDoesNotThrow(() -> {
            JSONObject jsonObject = new JSONObject(jsonString);
            // Verify the structure of the JSON
            assert jsonObject.has("symbol");
            assert jsonObject.has("price");
            // Verify that price is a number (not a string with a comma)
            assert jsonObject.get("price") instanceof Number;
        }, "JSON parsing should not throw an exception");
    }
}
