package com.hoyong.ilote.member;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;
//import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Data
public class Member implements Serializable {

    /**
     * Key 값
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String userId;

    private String password;
    /**
     * 이름
     */
    private String userName;

    /**
     * 나이
     */
    private Integer age;

    /** 점수 */
    private double totalPoint;

    /** 승률 */
    private double winningPercentage;

    /**
     * 전적승
     */
    private double totalWin;

    /**
     * 전적패
     */
    private double totalLose;

    /**
     * 기타(원백,투백 등등)
     */
    private String etc;

    private String refreshToken;

    private String accessToken;

    public Member updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
        return this;
    }

    //== 패스워드 암호화 ==//
//    public void encodePassword(PasswordEncoder passwordEncoder){
//        this.password = passwordEncoder.encode(password);
//    }
}