package io.shawlynot.model;

import java.util.List;

public record KrakenUpdate(List<KrakenCandle> data) implements KrakenMessage {
}
