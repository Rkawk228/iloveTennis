package com.hoyong.ilote.login;

import com.hoyong.ilote.exception.BusinessException;
import com.hoyong.ilote.jwt.JwtServiceImp;
import com.hoyong.ilote.member.LoginUser;
import com.hoyong.ilote.member.Member;
import com.hoyong.ilote.member.MemberRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class LoginService {

    private final JwtServiceImp jwtServiceImp;

    private final MemberRepository memberRepository;

    private final RedisTemplate redisTemplate;

//    private final PasswordEncoder passwordEncoder;

    public String getKakaoSSO (String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

//        try {
//            URL url = new URL(reqURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//
//            //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//            StringBuilder sb = new StringBuilder();
//            sb.append("grant_type=authorization_code");
//            sb.append("&client_id=97d39d6e91ff9fdedf425d2586080d4e");
//            sb.append("&redirect_uri=http://localhost:8080/login");
//            sb.append("&code=" + authorize_code);
//            bw.write(sb.toString());
//            bw.flush();
//
//            //    결과 코드가 200이라면 성공
//            int responseCode = conn.getResponseCode();
//            System.out.println("responseCode : " + responseCode);
//
//            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line = "";
//            String result = "";
//
//            while ((line = br.readLine()) != null) {
//                result += line;
//            }
//            System.out.println("response body : " + result);
//
//            //    Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
//            JsonParser parser = new JsonParser();
//            JsonElement element = parser.parse(result);
//
//            access_Token = element.getAsJsonObject().get("access_token").getAsString();
//            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
//
//            System.out.println("access_token : " + access_Token);
//            System.out.println("refresh_token : " + refresh_Token);
//
//            br.close();
//            bw.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        return access_Token;
    }

    @Transactional
    public Member loginUser(HttpSession session, LoginUser loginUser) {
        String userId = loginUser.getUserId();
        String password = loginUser.getPassword();
        String sessionId = session.getId();

        Member member = memberRepository.findByUserId(userId).orElseThrow(() -> new BusinessException("ER0001","존재하지 않은 ID 입니다."));

        if(!loginUser.getPassword().equals(member.getPassword())){
            throw new BusinessException("ER0002","비밀번호가 일치하지 않습니다.");
        }

        session.setAttribute(sessionId,member);
        return member;
    }

    @Transactional
    public Member signUpMember(Member member) {
//        member.encodePassword(passwordEncoder);
        memberRepository.save(member);
        return member;
    }
}
