package com.hoyong.ilote.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *
 */
@RestController
@RequestMapping(value = "/api/member")
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    // 생성
    @PostMapping("/")
    public ResponseEntity<?> saveMember(@RequestBody Member member) {
        Member savedMember = memberRepository.save(member);
        return ResponseEntity.ok(savedMember);
    }

    // 목록 조회
    @GetMapping("/all")
    public List<Member> all() {
        return memberRepository.findAll();
    }

    // 단건 조회
    @GetMapping("/{id}")
    public Member getMember(@PathVariable Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("illegal argument :" + id));
    }

    // 수정
    @PutMapping("/{id}")
    public Member updateMember(@PathVariable Long id, @RequestBody Member newMember) {
        return memberRepository.findById(id)
                .map(member -> {
                    member.setUserName(newMember.getUserName());
                    return memberRepository.save(newMember);
                })
                .orElseGet(() -> {
                    newMember.setId(id);
                    return memberRepository.save(newMember);
                });
    }

    // 삭제
    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable Long id) {
        memberRepository.deleteById(id);
    }
}
