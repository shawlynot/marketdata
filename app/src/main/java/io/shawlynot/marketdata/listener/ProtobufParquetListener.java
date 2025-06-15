package io.shawlynot.marketdata.listener;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.shawlynot.marketdata.model.KrakenMessage;
import io.shawlynot.marketdata.model.KrakenUpdate;
import io.shawlynot.marketdata.proto.CandleProtos;
import io.shawlynot.marketdata.util.ThrowingSupplier;

import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public class ProtobufParquetListener implements WebSocket.Listener {

    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).registerModule(new JavaTimeModule());

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        try {
            var message = ThrowingSupplier.wrap(() -> objectMapper.readValue(String.valueOf(data), KrakenMessage.class));
            if (message instanceof KrakenUpdate updates && updates.channel().equals("ohlc")) {
                var protos = updates.data().stream()
                        .map(u -> CandleProtos.Candle.newBuilder()
                                .setSymbol(u.symbol())
                                .setOpen(u.open())
                                .setHigh(u.high())
                                .setClose(u.close())
                                .setIntervalBegin(
                                        u.interval_begin().getEpochSecond() * 1_000_000_000
                                                + u.interval_begin().getNano()
                                ).build()
                        ).toList();
                System.out.printf("Updates: %s\n", protos);
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
