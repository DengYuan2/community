package com.spring.community.community;

import com.spring.community.community.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.midi.SoundbankResource;
import java.util.Date;

@SpringBootTest
class CommunityApplicationTests {

    @Test
    void contextLoads() {
        Date date = new Date(1575380855652L);
        System.out.println(date);
    }

}
