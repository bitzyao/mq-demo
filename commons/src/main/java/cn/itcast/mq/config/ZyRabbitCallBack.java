package cn.itcast.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class ZyRabbitCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this); // 发送者确认模式回电
        rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void confirm(@NonNull CorrelationData correlationData, boolean ack, @Nullable String cause) {
        String id = correlationData != null ? correlationData.getId() : "null";
        if(ack){
            log.info("交换机收到消息id为{}消息",id);
        }else{
            log.info("交换机未收到消息id为{}消息，原因：{}",id,cause);
        }
    }

    /**
     * 通道不存在，或异常消息退回,  (交换机成功收到消息，没有队列接收)
     * @param message 封装消息的实体
     * @param replyCode
     * @param replyText 退回原因
     * @param exchange 交换机
     * @param routingKey 路由Key
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        MessageProperties messageProperties = message.getMessageProperties();
        String correlationId = messageProperties.getCorrelationId();
        log.error("消息{}，被交换机{}退回，退回原因：{}，路由Key：{}",new String(message.getBody()),exchange,replyText,routingKey);
        if (StringUtils.hasText(correlationId)) {
            log.info("correlationId:{}",correlationId);
        }

    }
//    @Override
//    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
//
//    }
}
