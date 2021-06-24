package cn.hellopika.flashnote;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.hellopika.flashnote.mapper")
public class FlashNoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlashNoteApplication.class, args);
    }

}
