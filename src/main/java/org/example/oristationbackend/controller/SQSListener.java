package org.example.oristationbackend.controller;

import jakarta.annotation.PostConstruct;
import org.example.oristationbackend.config.QueueWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SQSListener {
    private final SqsClient sqsClient;
    private final String queueUrl = "https://sqs.ap-northeast-2.amazonaws.com/381492295588/myq-test.fifo";
    private final AtomicInteger position = new AtomicInteger(0);

    public SQSListener(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @PostConstruct
    public void listen() {
        new Thread(() -> {
            while (true) {
                ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .maxNumberOfMessages(10)
                        .waitTimeSeconds(20)
                        .build();

                ReceiveMessageResponse response = sqsClient.receiveMessage(receiveRequest);
                List<Message> messages = response.messages();

                for (Message message : messages) {
                    processMessage(message);
                    sqsClient.deleteMessage(b -> b.queueUrl(queueUrl).receiptHandle(message.receiptHandle()));
                }
            }
        }).start();
    }

    private void processMessage(Message message) {
        // 처리 로직 구현
        // ...
        System.out.println("sqslistner:"+ message.receiptHandle() + message);
        // WebSocket으로 대기 순서 업데이트 전송
        try {
            String userId = extractUserIdFromMessage(message.body());
            int currentPosition = position.incrementAndGet();
            System.out.println(currentPosition);
            QueueWebSocketHandler.sendMessageToUser(userId, "{\"userId\": \"" + userId + "\", \"position\": " + currentPosition + "}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void processMessage2(Message message) {
        // 메시지 처리 로직
        System.out.println("Received message: " + message.body());

        // 대기 순서 업데이트
        messagingTemplate.convertAndSend("/topic/waitingQueue", "Your turn");
    }

    private String extractUserIdFromMessage(String messageBody) {
        // 메시지에서 userId 추출 로직 구현
        return messageBody; // 간단히 예시로 리턴
    }
}
