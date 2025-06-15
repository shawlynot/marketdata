package io.shawlynot.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.shawlynot.listener.ProtobufParquetListener;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class KrakenClient implements AutoCloseable {

    public static CompletableFuture<KrakenClient> subscribe() {
        return HttpClient.newHttpClient().newWebSocketBuilder().buildAsync(
                URI.create("wss://ws.kraken.com/v2"),
                new ProtobufParquetListener()
        ).thenCompose(webSocket -> webSocket.sendText(getSubscriptionRequest(), true))
                .thenApply(KrakenClient::new);

    }

    private static String getSubscriptionRequest() {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(
                    Map.of(
                            "method", "subscribe",
                            "params", Map.of(
                                    "channel", "ohlc",
                                    "symbol", List.of("BTC/USD")
                            )
                    ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private final CountDownLatch running = new CountDownLatch(1);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final WebSocket client;

    private KrakenClient(WebSocket client) {
        this.client = client;
    }

    public boolean isRunning() {
        return running.getCount() > 0;
    }

    public void await() throws InterruptedException {
        running.await();
    }

    @Override
    public void close() {
        running.countDown();
        client.abort();
    }
}
