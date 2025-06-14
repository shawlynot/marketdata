package io.shawlynot.listener;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.shawlynot.model.KrakenMessage;
import io.shawlynot.model.KrakenUpdate;

import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

import static io.shawlynot.util.ThrowingSupplier.wrap;

public class ArrowListener implements WebSocket.Listener {

    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        try {
            var message = wrap(() -> objectMapper.readValue(String.valueOf(data), KrakenMessage.class));
            if (message instanceof KrakenUpdate) {
                System.out.printf("Processing: %s\n", message);
            } else {
                System.out.printf("Skipping: %s\n", data);
            }
        } finally {
            webSocket.request(1);
        }
        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        error.printStackTrace();
    }
}
