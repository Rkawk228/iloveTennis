package com.hoyong.ilote.member;

import com.hoyong.ilote.core.response.ResponseBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *
 */
@RestController
@RequestMapping(value = "/v1/api/member")
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    // 생성
    @PostMapping("/")
    public ResponseBase<Member> saveMember(@RequestBody Member member) {
        Member savedMember = memberRepository.save(member);
        return ResponseBase.of(savedMember);
    }

    // 목록 조회
    @GetMapping("/all")
    public List<Member> all() {
        return memberRepository.findAll();
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseBase<Member> getMember(@PathVariable Long id) {
        Member result = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("illegal argument :" + id));
        return ResponseBase.of(result);
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseBase<Member> updateMember(@PathVariable Long id, @RequestBody Member newMember) {
        Member result = memberRepository.findById(id)
                .map(member -> {
                    member.setUserName(newMember.getUserName());
                    return memberRepository.save(newMember);
                })
                .orElseGet(() -> {
                    newMember.setId(id);
                    return memberRepository.save(newMember);
                });
        return ResponseBase.of(result);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable Long id) {
        memberRepository.deleteById(id);
    }
}
