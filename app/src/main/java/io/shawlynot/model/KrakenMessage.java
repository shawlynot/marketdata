package io.shawlynot.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true, defaultImpl = KrakenIgnoredMessage.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = KrakenUpdate.class, names = {"snapshot", "update"}),
})
public interface KrakenMessage {
}
