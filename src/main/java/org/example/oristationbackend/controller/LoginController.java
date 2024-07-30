package org.example.oristationbackend.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.oristationbackend.dto.user.RegisterDto;
import org.example.oristationbackend.securiity.LoginWrapper;
import org.example.oristationbackend.service.LoginService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverClientSecret;

    @Value("${naver.redirect.uri}")
    private String naverRedirectUri;

    @GetMapping
    public String naverLogin(HttpServletRequest request) {
        String clientId = naverClientId;
        String redirectUri = naverRedirectUri;
        String state = RandomStringUtils.randomAlphabetic(10);
        String loginUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&state=" + state;

        request.getSession().setAttribute("state", state);

        return loginUrl;
    }

    @GetMapping("/redirect")
    public ResponseEntity<String> naverRedirect(HttpServletRequest request) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String sessionState = String.valueOf(request.getSession().getAttribute("state"));

//        if (!state.equals(sessionState)) {
//            System.out.println("세션 불일치");
//            request.getSession().removeAttribute("state");
//            return ResponseEntity.status(403).body("세션 불일치");
//        }

        String tokenURL = "https://nid.naver.com/oauth2.0/token";
        String clientId = naverClientId;
        String clientSecret = naverClientSecret;

        MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
        parameter.add("grant_type", "authorization_code");
        parameter.add("client_id", clientId);
        parameter.add("client_secret", clientSecret);
        parameter.add("code", code);
        parameter.add("state", state);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> entity = new HttpEntity<>(parameter, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<HashMap> result = restTemplate.postForEntity(tokenURL, entity, HashMap.class);
            Map<String, String> resMap = result.getBody();

            String accessToken = resMap.get("access_token");

            String userInfoURL = "https://openapi.naver.com/v1/nid/me";
            // Header에 access_token 삽입
            headers.set("Authorization", "Bearer "+accessToken);

            // Request entity 생성
            HttpEntity<?> userInfoEntity = new HttpEntity<>(headers);

            // Post 방식으로 Http 요청
            // 응답 데이터 형식은 Hashmap 으로 지정
            ResponseEntity<HashMap> userResult = restTemplate.postForEntity(userInfoURL, userInfoEntity, HashMap.class);
            HashMap<String, String> responseMap = (HashMap<String, String>) userResult.getBody().get("response"); // response 맵
            //응답 데이터 확인

            RegisterDto registerDto= new RegisterDto(responseMap.get("name"),responseMap.get("nickname"),responseMap.get("email"),RegisterDto.changeNumber(responseMap.get("mobile")));

            LoginWrapper loginWrapper = loginService.checkRegister(registerDto);
            String path="";
            if(loginWrapper.hasRegistered()){
                String msg=loginService.LoginOk(loginWrapper.getLoginDto());
                if(msg==null){
                    switch(loginWrapper.whatType()){
                        case ADMIN:
                            path="admin?token=";
                            break;
                        case RESTAURANT:
                            path= "rest?token=";
                            break;
                        default:
                            path="?token=";
                    }
                }else{
                    return ResponseEntity.status(302).header(HttpHeaders.LOCATION, "https://waitmate.shop/?isok=" + msg).build(); // 여기서 URL 형식을 수정
                }
            }else{
                path="?signin=true&token=";
            }
            String jwtToken=loginService.genJwtToken(registerDto.getUserName(),loginWrapper);

            String frontendRedirectUrl = "https://waitmate.shop/"+path+ URLEncoder.encode(jwtToken, "UTF-8");

            return ResponseEntity.status(302).header(HttpHeaders.LOCATION, frontendRedirectUrl).build();

        }  catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("에러 발생");
        } finally {
            request.getSession().removeAttribute("state");
        }
    }

    @GetMapping("/id/{loginId}")
    public String idLogin(@PathVariable(name = "loginId") int loginId) {
        return loginService.loginType(loginId);
    }

}
