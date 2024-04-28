package cn.itcast.mq.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Map;

/**
 * 消费端自动确认组件
 */
@Component
public class SpringRabbitAutoListener {

    // @RabbitListener(queues = "simple.queue")
    // public void listenSimpleQueue(String msg) {
    //     System.out.println("消费者接收到simple.queue的消息：【" + msg + "】");
    // }

    @RabbitListener(queues = "simple.queue")
    public void listenWorkQueue1(String msg, Channel channel, Message message) throws InterruptedException, IOException {
//        int a = 1/0;
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            System.out.println("消费者1接收到消息：【" + msg + "】" + LocalTime.now());
            channel.basicAck(deliveryTag,false);
            Thread.sleep(20);
        } catch (Exception e) {
            // deliveryTag：这是要 Nack 的消息的传送标签（Delivery Tag）。它是一个整数，用于唯一标识通道上的每条消息。
            // 第二个参数 true: 是否 Nack 多条消息,队列中在deliveryTag之前的数据
            //手动nack 告诉rabbitmq该消息消费失败  第三个参数：true: 被拒绝的消息应该被重新请求，而不是被丢弃或变成死信

            channel.basicNack(deliveryTag,false,true);
            throw new RuntimeException(e);
        }
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        channel.basicAck(deliveryTag,false); // 手动确认
//        channel.basicNack(deliveryTag,false,false); // 重试

    }

//    @RabbitListener(queues = "simple.queue")
//    public void listenWorkQueue2(String msg, Channel channel, Message message) throws InterruptedException, IOException {
//        System.err.println("消费者2........接收到消息：【" + msg + "】" + LocalTime.now());
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        channel.basicAck(deliveryTag,false); // 手动确认
//        Thread.sleep(200);
//    }

    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String msg) {
        System.out.println("消费者接收到fanout.queue1的消息：【" + msg + "】");
    }


    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String msg) {
        System.out.println("消费者接收到fanout.queue2的消息：【" + msg + "】");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "itcast.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}
    ))
    public void listenDirectQueue1(String msg){
        System.out.println("消费者接收到direct.queue1的消息：【" + msg + "】");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2"),
            exchange = @Exchange(name = "itcast.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}
    ))
    public void listenDirectQueue2(String msg){
        System.out.println("消费者接收到direct.queue2的消息：【" + msg + "】");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue1"),
            exchange = @Exchange(name = "itcast.topic", type = ExchangeTypes.TOPIC),
            key = "china.#"
    ))
    public void listenTopicQueue1(String msg){
        System.out.println("消费者接收到topic.queue1的消息：【" + msg + "】");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue2"),
            exchange = @Exchange(name = "itcast.topic", type = ExchangeTypes.TOPIC),
            key = "#.news"
    ))
    public void listenTopicQueue2(String msg){
        System.out.println("消费者接收到topic.queue2的消息：【" + msg + "】");
    }

    @RabbitListener(queues = "object.queue")
    public void listenObjectQueue(Map<String,Object> msg){
        System.out.println("接收到object.queue的消息：" + msg);
    }
}
