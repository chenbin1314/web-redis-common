package com.jingoal.web.redis;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.jingoal.web.common.exception.MessageException;
import com.jingoal.web.common.queue.Producer;

/**
 * Description: redis消息发送实现
 * 
 * @company: 北京今目标信息技术有限公司
 */
public class RedisProducer<M extends Serializable> implements Producer<RedisMessage<M>> {

  private static final Logger logger = LoggerFactory.getLogger(RedisProducer.class);

  private JedisConnectionFactory connectionFactory;
  private RedisSerializer<Object> serializer;

  public void setConnectionFactory(JedisConnectionFactory connectionFactory) {
    this.connectionFactory = connectionFactory;
  }

  public void setSerializer(RedisSerializer<Object> serializer) {
    this.serializer = serializer;
  }

  public void send(RedisMessage<M> message) throws MessageException {
    JedisConnection jedisConnection = null;
    try {
      jedisConnection = connectionFactory.getConnection();
      byte[] key = serializer.serialize(message.key());
      byte[] value = serializer.serialize(message);
      jedisConnection.lPush(key, value);
      jedisConnection.expire(key, message.expire());
    } catch (Exception ex) {
      logger.error(
          "error[key=" + message.key() + " seconds=" + message.expire() + "]" + ex.getMessage(),
          ex);
    } finally {
      RedisConnectionUtils.releaseConnection(jedisConnection, connectionFactory);
    }
  }
}
