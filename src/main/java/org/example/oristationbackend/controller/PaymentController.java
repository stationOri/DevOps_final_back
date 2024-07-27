package org.example.oristationbackend.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.oristationbackend.dto.user.PayCancelDto;
import org.example.oristationbackend.dto.user.PayDto;
import org.example.oristationbackend.service.PaymentService;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    @Value("${imp.api.key}")
    private String key;
    @Value("${imp.api.secretkey}")
    private String secretkey;

    @PostMapping("/validation")
    public boolean validateIamport(@RequestBody PayDto payDto) throws IamportResponseException, IOException {
        return paymentService.verifyPayment(payDto);

    }
    @PostMapping("/refund")
    public boolean refundPay(@RequestBody PayCancelDto payCancelDto) throws IamportResponseException, IOException {
        return paymentService.refundPayment(payCancelDto);

    }

}