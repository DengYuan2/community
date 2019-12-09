package com.spring.community.community.service;

import com.spring.community.community.dto.PageDTO;
import com.spring.community.community.dto.QuestionDTO;
import com.spring.community.community.mapper.QuestionMapper;
import com.spring.community.community.mapper.UserMapper;
import com.spring.community.community.model.Question;
import com.spring.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PageDTO list(Integer page, Integer size) {
        //例如：一次从数据库取出5条数据，第1页时显示的数据为数据库中1-5条，sql语句为select * from question limit 0,5
        //第2页时显示的数据为数据库中6-10条，sql语句为select * from question limit 5,5 【前一个数字为偏移量，表示从
        // 哪个开始，后一个数字表示一次拿出5条】
        //所以我按第1页的时候，偏移量为size*(page-1)【size为一次显示的条数，此处设置默认为5，page为在网页上选择第几页】

        Integer totalCount = questionMapper.count();
        Integer totalPage;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page<1){
            page=1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        Integer offset = size*(page-1);
        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        PageDTO pageDTO = new PageDTO();
        for (Question question:questions){
            User user =userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//将question对象的属性抄给question对象
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pageDTO.setPageNation(totalPage,page);
        pageDTO.setQuestions(questionDTOS);
        //Integer totalCount = questionMapper.count();

        return pageDTO;
    }

    public PageDTO list(int userId, Integer page, Integer size) {
        Integer totalCount = questionMapper.countByUserId(userId);
        Integer totalPage;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page<1){
            page=1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        System.out.println(totalPage);
        Integer offset = size*(page-1);
        List<Question> questions = questionMapper.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        PageDTO pageDTO = new PageDTO();
        for (Question question:questions){
            User user =userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//将question对象的属性抄给question对象
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pageDTO.setPageNation(totalPage,page);
        pageDTO.setQuestions(questionDTOS);
        //Integer totalCount = questionMapper.count();

        return pageDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        User user = userMapper.findById(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);
        return  questionDTO;
    }
}
