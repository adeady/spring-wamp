package com.example

import ch.rasc.wampspring.annotation.WampCallListener
import ch.rasc.wampspring.message.PublishMessage
import ch.rasc.wampspring.message.WampMessage
import ch.rasc.wampspring.message.WelcomeMessage
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.MappingJsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.WebSocketClient
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.handler.TextWebSocketHandler


@Service
public class TestService {

    @WampCallListener("toUpperCase")
    public String toUpperCase(String aString) {
        return aString.toUpperCase();
    }

    def send(String aString) {

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        final JsonFactory jsonFactory = new MappingJsonFactory(new ObjectMapper());
        TextWebSocketHandler handler = new TextWebSocketHandler() {

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                WampMessage wampMessage = WampMessage.fromJson(jsonFactory, message.getPayload());
            }

        };

        ListenableFuture<WebSocketSession> future = webSocketClient.doHandshake(handler, "ws://localhost:8080/wamp");
        future.addCallback(new ListenableFutureCallback<WebSocketSession>() {
            @Override
            void onFailure(Throwable ex) {
                System.out.println("ERROR SENDING PUBLISH_MESSAGE" + ex);
            }

            @Override
            void onSuccess(WebSocketSession result) {
                PublishMessage publishMessage = new PublishMessage("echo", aString);
                result.sendMessage(new TextMessage(publishMessage.toJson(jsonFactory)));
                println "SENT MESSAGE"
            }

        })
    }

}