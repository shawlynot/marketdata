package io.shawlynot.marketdata.model;

import java.time.Instant;


public record KrakenCandle(
        String symbol,
        Long open,
        Long high,
        Long close,
        Instant interval_begin
) {
}
