package cn.hellopika.flashnote.mq;

import cn.hellopika.flashnote.model.dto.SendSmsDto;
import cn.hellopika.flashnote.util.api.SmsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @date: 2021/6/13
 * @author: pikachu
 * @description: 发送短信 消息队列的消费者
 **/

@Component
@Slf4j
public class SmsConsumer {

    @Autowired
    private SmsApi smsApi;

    /**
     * 监听指定队列，进行消费
     * @param sendSmsDto
     */
    @RabbitListener(queuesToDeclare = @Queue("sms_quene"))
    public void sendSms(SendSmsDto sendSmsDto){
        // 调用 API, 发送短信
        smsApi.sendSms(sendSmsDto.getPhone(), sendSmsDto.getMessage());
    }
}
