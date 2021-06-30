package cn.hellopika.flashnote.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @date: 2021/6/13
 * @author: pikachu
 * @description: RabbitMQ 使用的发送短信 dto
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendSmsDto implements Serializable {

    private String phone;
    private String message;
}
