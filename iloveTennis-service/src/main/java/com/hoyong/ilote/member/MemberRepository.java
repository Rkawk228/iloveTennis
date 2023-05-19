package com.hoyong.ilote.member;

import org.bitbucket.gt_tech.spring.data.querydsl.value.operators.ExpressionProviderFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface MemberRepository extends JpaRepository<Member, Long>
        , QuerydslPredicateExecutor<Member>
        , QuerydslBinderCustomizer<QMember>
{
    @Override
    default void customize(QuerydslBindings bindings, QMember root) {
        bindings.bind(root.userName).all(ExpressionProviderFactory::getPredicate);
    }

    Optional<Member> findByUserId(String userId);

    Optional<Member> findByUserName(String userName);

    Optional<Member> findByUserIdAndPassword(String userId,String password);

    Optional<Member> findByRefreshToken(String refreshToken);

}
