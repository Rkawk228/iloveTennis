package com.hoyong.ilote.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.collections.RedisProperties;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

//추상메서드로 해서 공통부분과 BE , FE 구분
@Configuration
//@EnableRedisRepositories
@EnableRedisHttpSession
public abstract class BaseRedisConfiguration {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String password;

    // lettuce
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        this.redisTemplateKeySerializer(RedisSerializerTypes.String, redisTemplate);
        this.redisTemplateValueSerializer(RedisSerializerTypes.String, redisTemplate);
        return redisTemplate;
    }

    /**
     * @param type
     * @param template
     * @return
     */
    private static <K, V> RedisTemplate<?, ?> redisTemplateKeySerializer(final RedisSerializerTypes type, final RedisTemplate<K, V> template) {
        if (RedisSerializerTypes.String.equals(type)) {
            template.setKeySerializer(new StringRedisSerializer());
            template.setHashKeySerializer(new StringRedisSerializer());
        } else if (RedisSerializerTypes.GenericJackson2Json.equals(type)) {
            template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
            template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        } else {
            template.setKeySerializer(new JdkSerializationRedisSerializer());
            template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        }
        return template;
    }

    /**
     * @param type
     * @param template
     * @return
     */
    public static <K, V> RedisTemplate<?, ?> redisTemplateValueSerializer(final RedisSerializerTypes type, final RedisTemplate<K, V> template) {
        if (RedisSerializerTypes.GenericJackson2Json.equals(type)) {
            template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
            template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        } else if (RedisSerializerTypes.String.equals(type)) {
            template.setValueSerializer(new StringRedisSerializer());
            template.setHashValueSerializer(new StringRedisSerializer());
        } else {
            template.setValueSerializer(new JdkSerializationRedisSerializer());
            template.setHashValueSerializer(new JdkSerializationRedisSerializer());
        }
        return template;
    }

    private enum RedisSerializerTypes {
        String,
        GenericJackson2Json,
        JdkSerialization
    };

}
