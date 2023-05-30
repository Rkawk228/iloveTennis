package com.hoyong.ilote.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface MemberRepository extends JpaRepository<Member, Long>
        , QuerydslPredicateExecutor<Member>
{
    Optional<Member> findByUserId(String userId);

    Optional<Member> findByUserName(String userName);

    Optional<Member> findByUserIdAndPassword(String userId,String password);

    Optional<Member> findByRefreshToken(String refreshToken);

}
