package com.hoyong.ilote.login;

import com.hoyong.ilote.core.response.ResponseBase;
import com.hoyong.ilote.member.LoginUser;
import com.hoyong.ilote.member.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/api/login")
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/sso/kakao/login")
    public ResponseBase<String> getAccessToken(@RequestParam String kakaoAuthCode) {
        return ResponseBase.of(loginService.getKakaoSSO(kakaoAuthCode));
    }

    @PostMapping("")
    public ResponseBase<Member> loginUser(HttpSession session, @RequestBody LoginUser loginUser) {
        return ResponseBase.of(loginService.loginUser(session,loginUser));
    }

    @GetMapping("/logout")
    public ResponseBase<?> logout(HttpSession session){
        session.invalidate();
        return ResponseBase.success();
    }

    @PostMapping("/signUp")
    public ResponseBase<Member> signUpMember(@RequestBody Member member) {
        return ResponseBase.of(loginService.signUpMember(member));
    }

    @GetMapping("/getSessionId")
    public String getSessionId(HttpSession session) {
        return session.getId();
    }

}
