package com.spring.community.community;

import com.spring.community.community.mapper.QuestionMapper;
import com.spring.community.community.mapper.UserMapper;
import com.spring.community.community.model.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.midi.SoundbankResource;
import java.util.Date;

@SpringBootTest
class CommunityApplicationTests {
    @Autowired
    QuestionMapper questionMapper;

    @Test
    void contextLoads() {
        Date date = new Date(1575380855652L);
        System.out.println(date);
    }
@Test
    void test(){
    Question question = questionMapper.selectByPrimaryKey((long) 555);
    System.out.println("keyilsalla");
    if (question.getTitle()==null) System.out.println("是空的！");
    else System.out.println("不是空的！");
}

}
