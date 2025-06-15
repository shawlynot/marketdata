package io.shawlynot.marketdata.model;

import java.util.List;

public record KrakenUpdate(String channel, List<KrakenCandle> data) implements KrakenMessage {
}
