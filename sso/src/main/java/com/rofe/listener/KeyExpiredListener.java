package com.rofe.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.nio.charset.StandardCharsets;

@Slf4j
public class KeyExpiredListener extends KeyExpirationEventMessageListener {
	public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
		super(listenerContainer);
	}
	@Override
	public void onMessage(Message message, byte[] pattern) {
		String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
		//过期的key
		String key = new String(message.getBody(), StandardCharsets.UTF_8);
		log.info("redis key 过期：pattern={},channel={},key={}", new String(pattern), channel, key);
	}
}