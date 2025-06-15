package io.shawlynot.marketdata.listener;

import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public class StdoutListener implements WebSocket.Listener {

    @Override
    public void onOpen(WebSocket webSocket) {
        System.out.println("Websocket opened...");
        WebSocket.Listener.super.onOpen(webSocket);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        System.out.println(data);
        return WebSocket.Listener.super.onText(webSocket, data, last);
    }
}
