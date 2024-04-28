package cn.itcast.mq.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class DeadQueueListener {
    @RabbitListener(queues = "#{zyRabbitConfig.deadQueue()}",ackMode = "NONE")
    public void listenDeadQueue(String msg, Channel channel, Message message,@Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        log.warn("-----管理员快来啊,又有消息出问题了,消息内容是:{}-----",msg);
    }
}
