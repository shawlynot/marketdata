package io.shawlynot.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;


public record KrakenCandle(
        String symbol,
        Long open,
        Long high,

        Long close,
        Instant interval_begin
) {
}
