package com.hoyong.ilote.login;

import com.hoyong.ilote.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/login")
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/sso/kakao/login")
    public String getAccessToken(@RequestParam String kakaoAuthCode) {
        return loginService.getKakaoSSO(kakaoAuthCode);
    }

    @PostMapping("/")
    public Member loginUser(@RequestBody Member member) {
        return loginService.loginUser(member);
    }

    @PostMapping("/signUp")
    public Member signUpMember(@RequestBody Member member) {
        return loginService.signUpMember(member);
    }


}
