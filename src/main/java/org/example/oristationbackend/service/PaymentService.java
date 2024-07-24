package org.example.oristationbackend.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import org.example.oristationbackend.dto.user.PayCancelDto;
import org.example.oristationbackend.dto.user.PayDto;
import org.example.oristationbackend.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private IamportClient iamportClient; ;
    private final PaymentRepository paymentRepository;
    @Value("${imp.api.key}")
    private String key;
    @Value("${imp.api.secretkey}")
    private String secretkey;
    @PostConstruct
    public void init() {
        this.iamportClient= new IamportClient(key,secretkey);
    }
    public boolean verifyPayment(PayDto payDto) throws IamportResponseException, IOException {
        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(payDto.getImp_uid()); // 결제 검증 시작
        System.out.println(iamportResponse);
        Long amount = (iamportResponse.getResponse().getAmount()).longValue(); // 결제 금액
        System.out.println(amount);
        String status = iamportResponse.getResponse().getStatus(); // paid 이면 1
        System.out.println(status);
        if(amount.equals(payDto.getAmount())&&status.equals("paid")) {
            return true;
        } return false;
    }
    public boolean refundPayment(PayCancelDto payCancelDto) throws IamportResponseException, IOException {
        BigDecimal amount = BigDecimal.valueOf(payCancelDto.getAmount());
        BigDecimal checksum= BigDecimal.valueOf(payCancelDto.getChecksum());
        CancelData cancelData=  new CancelData(payCancelDto.getImp_uid(),true,amount);
        cancelData.setChecksum(checksum);
        cancelData.setReason(payCancelDto.getReason());
        IamportResponse<Payment> iamportResponse= iamportClient.cancelPaymentByImpUid(cancelData);
        System.out.println(iamportResponse);
        if (iamportResponse.getResponse() != null) {
            if(iamportResponse.getResponse().getStatus().equals("cancelled")){
                return true;
            } return false;
        }return false;


    }



}