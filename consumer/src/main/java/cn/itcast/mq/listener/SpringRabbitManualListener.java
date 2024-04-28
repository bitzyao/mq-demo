package cn.itcast.mq.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Map;
import java.util.Random;

/**
 * 消费端自动确认组件
 */
@Component
@Slf4j
public class SpringRabbitManualListener {

    @RabbitListener(queues ="manual.queue",ackMode = "MANUAL")
    public void message(String msg, Channel channel, Message message) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("开始处理消息:{}",msg);
            int i = new Random().nextInt(10);
            if(i%2 == 0 ){
                int a = 1/0;
            }
            //手动ack  第二个参数为false是表示仅仅确认当前消息 true表示确认之前所有的消息
            log.info("开始处理消息:{} 处理成功",msg);
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            //手动nack 告诉rabbitmq该消息消费失败  第三个参数：true: 被拒绝的消息应该被重新请求，而不是被丢弃或变成死信
            log.error("消息处理失败:{} 处理失败",msg);
            channel.basicNack(deliveryTag,false,false);
        }
    }


}
