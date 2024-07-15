package org.example.oristationbackend.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.user.EmailDto;
import org.example.oristationbackend.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;


    @PostMapping("/send")
    public ResponseEntity<String> MailSend(HttpSession httpSession, @RequestParam("mail") String mail) {
        String num = emailService.sendMail(mail);

        httpSession.setAttribute("code", num);

        return ResponseEntity.ok("Verification code sent and stored in session");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(HttpSession httpSession, @RequestParam("code") String code) {
        String sessionCode = (String) httpSession.getAttribute("code");

        if (sessionCode != null && sessionCode.equals(code)) {
            return ResponseEntity.ok("Code verified successfully");
        } else {
            return ResponseEntity.status(400).body("Invalid code");
        }
    }



}
