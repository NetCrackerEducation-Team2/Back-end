package com.netcraker;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Random;


@SpringBootApplication
public class Application {
    static private final String URL = "ws://localhost:8081/socket";

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);

        WebSocketClient socketClient = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(socketClient);
        stompClient.setMessageConverter(new StringMessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        stompClient.connect(URL, sessionHandler);

        System.err.println("wating for msgs...");

    }


    static class MyStompSessionHandler implements StompSessionHandler {
        @Override
        public void afterConnected(StompSession session, StompHeaders stompHeaders) {
            System.out.println("connected!");
            final int userId = new Random().nextInt(10);
            System.out.println("Your id: " + userId);
//            session.subscribe("/app/subscribe/" + userId, this);
//            System.out.println("Enter your user id to subscribe: ");
//            final int id = scanner.nextInt();
            session.subscribe("/topic/achievements", this);

//            session.subscribe("/topic/messages/" + userId, this);
            ;
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("Enter your greeting message : ");
//            final String msg = scanner.nextLine();
//
//            session.send("/app/hello", msg);
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
//            System.out.println("handling frame...");
            String msg = (String) payload;
            System.err.println("New achiviement : " + msg);
        }

        @Override
        public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
            System.out.println("handleException");
            throwable.printStackTrace();
        }

        @Override
        public void handleTransportError(StompSession stompSession, Throwable throwable) {
            System.out.println("handleTransportError");
            throwable.printStackTrace();

        }

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return String.class;
        }
    }
}