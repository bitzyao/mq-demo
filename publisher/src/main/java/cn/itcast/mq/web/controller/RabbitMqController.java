package cn.itcast.mq.web.controller;

//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;

//
//@RestController
//@RequestMapping("/RabbitMq")
public class RabbitMqController {

//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    @PutMapping("/putmessage/{msg}")
//    public void putmessage(@PathVariable("msg") String msg){
//        String queueName = "simple.queue";
//        String message = msg;
//        CorrelationData correlationData = new CorrelationData();
//        correlationData.setId("111");
//        rabbitTemplate.convertAndSend(queueName, (Object) message,correlationData);
//    }
}
