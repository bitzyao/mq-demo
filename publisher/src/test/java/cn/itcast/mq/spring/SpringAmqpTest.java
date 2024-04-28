package cn.itcast.mq.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage2SimpleQueue() {
        String queueName = "simple.queue";
        String message = "hello, spring amqp!";
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("111");
        //CORRELATED 模式
        rabbitTemplate.convertAndSend(queueName, (Object) message,correlationData);
    }

    @Test
    public void testSendMessageManualQueue() throws InterruptedException {
        String queueName = "manual.queue";
        String message = "hello, 需要手动确认!";

        //CORRELATED 模式
            CorrelationData correlationData = new CorrelationData();
            for (int i = 1; i <= 10; i++) {
                correlationData.setId(i+"");
                rabbitTemplate.convertAndSend(queueName, (Object) ((Object) message + "_" + i), correlationData);
                Thread.sleep(100);
            }
    }

    @Test
    public void testSendMessage2WorkQueue() throws InterruptedException {
        String queueName = "simple.queue";
        String message = "hello, message__";
        CorrelationData correlationData = new CorrelationData();
        for (int i = 1; i <= 50; i++) {
            correlationData.setId(i+"");
            rabbitTemplate.convertAndSend(queueName, (Object) (message + i),correlationData);
            Thread.sleep(20);
        }
    }

    /**
     *  测试消息队列不存在是否回退消息
     * @throws InterruptedException
     */
    @Test
    public void testSendMessage2WorkQueue1() throws InterruptedException {
        String queueName = "simple.queue11";
        String message = "hello, message__";
        CorrelationData correlationData = new CorrelationData();
        for (int i = 1; i <= 50; i++) {
            correlationData.setId(i+"");
            rabbitTemplate.convertAndSend(queueName, (Object) (message + i),correlationData);
            Thread.sleep(20);
        }
    }

    /**
     * 广播
     */
    @Test
    public void testSendFanoutExchange() {
        // 交换机名称
        String exchangeName = "itcast.fanout";
        // 消息
        String message = "hello, every one!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }

    /**
     * 测试 交换机存在，但是没有绑定队列(先回调退回消息，在执行是否确认收到消息) 能接收消息；但是没人处理
     */
    @Test
    public void testSendFanoutExchange1() {
        // 交换机名称
        String exchangeName = "itcast.fanout1";
        // 消息
        String message = "hello, every one!";
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("111");
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "", message,correlationData);
    }

    /**
     * 路由
     */
    @Test
    public void testSendDirectExchange() {
        // 交换机名称
        String exchangeName = "itcast.direct";
        // 消息
        String message = "hello, yellow!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "yellow", message);
    }

    /**
     * 主题
     */
    @Test
    public void testSendTopicExchange() {
        // 交换机名称
        String exchangeName = "itcast.topic";
        // 消息
        String message = "今天天气不错，我的心情好极了!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "china.weather", message);
    }

    /**
     * 发送对象
     */
    @Test
    public void testSentObjectQueue(){
        for (int i = 0; i < 50; i++) {
            HashMap<String, Object> msg = new HashMap<>();
            msg.put("name","张三");
            msg.put("age",i);
            rabbitTemplate.convertAndSend("object.queue", msg);
        }

    }
}
