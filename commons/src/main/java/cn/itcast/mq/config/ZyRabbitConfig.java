package cn.itcast.mq.config;

import cn.itcast.mq.config.ZyRabbitProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ZyRabbitConfig {

    // 对象转换器
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 长度挤压超过5进入死信队列
     * @return
     */
    @Bean
    public Queue simpleQueue(){
        return new Queue("simple.queue");
    }



    @Bean
    public Queue manualQueue(){
        Map<String, Object> args = ZyRabbitProperties.deadQueueArgs();
        args.put("x-dead-letter-routing-key", "deadRouting");
        args.put("x-max-length",5);
        return new Queue("manual.queue",true,false,false,args);
    }

    @Bean // ttlQueue中的队列达到过期时间就删除或者进入死信队列
    public Queue ttlQueue(){
        Map<String, Object> args = ZyRabbitProperties.deadQueueArgs();
        args.put("x-message-ttl",60*1000);
        args.put("x-dead-letter-routing-key", "ttlRouting");
        return new Queue("ttl.queue",true,false,false,args);
    }



    // 广播交换机
    @Bean
    public FanoutExchange fanoutExchange(){
        // 交换机名称,是否持久化,没有队列绑定时是否自定删除
        return new FanoutExchange("zy.fanout",true,false);
    }

    // 路由交换机
    @Bean
    public DirectExchange directExchange(){
        // 交换机名称,是否持久化,没有队列绑定时是否自定删除
        return new DirectExchange("zy.direct",true,false);
    }

    // 主题交换机
    @Bean
    public TopicExchange topicExchange(){
        // 交换机名称,是否持久化,没有队列绑定时是否自定删除
        return new TopicExchange("zy.topic",true,false);
    }

    // 头交换机
    @Bean
    public HeadersExchange headersExchange(){
        // 交换机名称,是否持久化,没有队列绑定时是否自定删除
        return new HeadersExchange("zy.headers",true,false);
    }

    // fanout.queue1 定义一个广播交换机用的队列
    @Bean
    public Queue fanoutQueue1(){
        // 构造出来的本事就是持久化队列
        return new Queue("fanout.queue1");
    }


    // 绑定队列1到广播交换机
    @Bean
    public Binding fanoutBinding1(Queue fanoutQueue1, FanoutExchange fanoutExchange){
        return BindingBuilder
                .bind(fanoutQueue1)
                .to(fanoutExchange);
    }

    // fanout.queue2
    @Bean
    public Queue fanoutQueue2(){
        return new Queue("fanout.queue2");
    }

    // 绑定队列2到广播交换机
    @Bean
    public Binding fanoutBinding2(Queue fanoutQueue2, FanoutExchange fanoutExchange){
        return BindingBuilder
                .bind(fanoutQueue2)
                .to(fanoutExchange);
    }

    // 定义一个发送对象的交换机
    @Bean
    public Queue objectQueue(){
        return new Queue("object.queue");
    }


    /**
     * 3.6.0支持惰性队列
     * @return
     */
    @Bean
    public Queue lazyQueue(){
        // 构造出来的本事就是持久化队列
        return QueueBuilder.durable("lazy.queue").lazy().build();
    }


    /**************** 死信配置 *****************/
    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(ZyRabbitProperties.deadExchange, true, false);
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue deadQueue() {
        return new Queue(ZyRabbitProperties.deadQueue);
    }

    /**
     * 死信队列和死信交换机绑定
     * @return
     */
    @Bean
    Binding deadRouteBinding() {
        return BindingBuilder.bind(deadQueue())
                .to(deadExchange())
                .with("deadRouting");
    }
}
