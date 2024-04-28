package cn.itcast.mq.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ZyRabbitProperties {
    public static final String deadExchange = "deadExchange"; // 死信交换机
    public static final String deadQueue = "deadQueue";  // 死信队列
    /**
     * 转发到 死信队列，配置参数
     */
    public static Map<String, Object> deadQueueArgs() {
        Map<String, Object> map = new HashMap<>();
        // 绑定该队列到死信交换机
        map.put("x-dead-letter-exchange", deadExchange);
        return map;
    }
    public String deadQueue(){
        return deadQueue;
    }
}
