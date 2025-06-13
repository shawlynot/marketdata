package io.shawlynot.client;

import java.util.List;
import java.util.Map;

public class KrakenClient {


    private Map<String, Object> getSubscriptionRequest() {
        return Map.of(
                "method", "subscribe",
                "params", Map.of(
                        "channel", "ticker",
                        "symbol", "BTC/USD"
                )
        );
    }

}
