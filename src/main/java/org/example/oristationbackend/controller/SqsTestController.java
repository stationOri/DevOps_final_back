package org.example.oristationbackend.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.config.QueueWebSocketHandler;
import org.example.oristationbackend.dto.user.CombinedDto;
import org.example.oristationbackend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
@RequiredArgsConstructor
@RestController


public class SqsTestController {
    @Autowired
    private SqsClient sqsClient;
    private final ReservationService reservationService;
    private final String queueUrl = "https://sqs.ap-northeast-2.amazonaws.com/381492295588/myq-test.fifo";

    @PostMapping("/reservation")
    public String addReservation(@RequestBody String userId) {
        return userId;
    }
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @PostMapping("/reserve")
    public ResponseEntity<String> reserve(@RequestBody String userId) {
        String messageBody = userId.toString();  // request를 문자열로 변환
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .messageGroupId("reservation")
                .build();
        sqsClient.sendMessage(sendMsgRequest);

        SendMessageResponse sendMsgResponse = sqsClient.sendMessage(sendMsgRequest);
        // 대기 중인 메시지 수를 가져와서 대기 순서 계산
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(1)  // 대기 중인 메시지를 한 개만 가져옴
                .build();

        // 대기 중인 메시지 수를 가져와서 대기 순서 계산
        int position = getQueuePosition("reservation");

        // 현재 대기열 상태를 클라이언트로 전송
        messagingTemplate.convertAndSend("/topic/waitingQueue", position);

        return ResponseEntity.ok("Reservation request sent to queue.");
    }


    private int getQueuePosition(String messageGroupId) {
        int position = 0;

        // 모든 메시지를 반복적으로 조회하여 대기 순서 계산
        boolean done = false;
        while (!done) {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(10)  // 최대 10개 메시지 가져오기
                    .build();
            ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(receiveMessageRequest);
            List<software.amazon.awssdk.services.sqs.model.Message> messages = receiveMessageResponse.messages();

            if (!messages.isEmpty()) {
                for (software.amazon.awssdk.services.sqs.model.Message message : messages) {
                    // 메시지 그룹 ID가 일치하는 메시지만 처리
                    if (message.attributes().get("MessageGroupId").equals(messageGroupId)) {
                        position++;
                    }
                }
            } else {
                done = true;
            }
        }

        return position;
    }

}

