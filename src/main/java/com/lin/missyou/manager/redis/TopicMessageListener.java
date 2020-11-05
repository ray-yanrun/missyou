package com.lin.missyou.manager.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

// 监听Redis的键空间通知
public class TopicMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();

        String expiredKey = new String(body);
        String topic = new String(channel);
//        String[] data = expiredKey.split(",");

        System.out.println(expiredKey);
        System.out.println(topic);
    }
}
