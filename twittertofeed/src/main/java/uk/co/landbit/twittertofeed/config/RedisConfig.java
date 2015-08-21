package uk.co.landbit.twittertofeed.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfig {

    @Autowired
    public RedisConnectionFactory redisConnectionFactory;

    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory factory) {
      final StringRedisTemplate template = new StringRedisTemplate(factory);
      Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
      ObjectMapper om = new ObjectMapper();
      om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
      om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
      jackson2JsonRedisSerializer.setObjectMapper(om);
      template.setKeySerializer(new StringRedisSerializer());
      template.setValueSerializer(jackson2JsonRedisSerializer);
      template.setDefaultSerializer(jackson2JsonRedisSerializer);
      template.setHashValueSerializer(jackson2JsonRedisSerializer);
      template.setHashKeySerializer(new StringRedisSerializer());
      template.afterPropertiesSet();

      return template;
    }
    
//    @Bean
//    public RedisTemplate<String, ?> redisTemplate() {
//	RedisTemplate<String, ?> template = new RedisTemplate();
//	template.setConnectionFactory(redisConnectionFactory);
//	template.setValueSerializer(jackson2JsonRedisSerializer());
//	template.setHashValueSerializer(jackson2JsonRedisSerializer());
//	return template;
//    }
//
//    @Bean
//    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
//	Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//	jackson2JsonRedisSerializer.setObjectMapper(objectMapper());
//	return jackson2JsonRedisSerializer;
//    }
//
//    @Bean
//    public ObjectMapper objectMapper() {
//	ObjectMapper objectMapper = new ObjectMapper();
//	objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
//	objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//	return objectMapper;
//    }
}
