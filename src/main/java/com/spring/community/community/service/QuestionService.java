package com.spring.community.community.service;

import com.spring.community.community.dto.PageDTO;
import com.spring.community.community.dto.QuestionDTO;
import com.spring.community.community.exception.CustomizeErrorCode;
import com.spring.community.community.exception.CustomizeException;
import com.spring.community.community.mapper.QuestionExtMapper;
import com.spring.community.community.mapper.QuestionMapper;
import com.spring.community.community.mapper.UserMapper;
import com.spring.community.community.model.Question;
import com.spring.community.community.model.QuestionExample;
import com.spring.community.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.util.resources.cldr.rof.CurrencyNames_rof;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    public PageDTO list(Integer page, Integer size) {
        //例如：一次从数据库取出5条数据，第1页时显示的数据为数据库中1-5条，sql语句为select * from question limit 0,5
        //第2页时显示的数据为数据库中6-10条，sql语句为select * from question limit 5,5 【前一个数字为偏移量，表示从
        // 哪个开始，后一个数字表示一次拿出5条】
        //所以我按第1页的时候，偏移量为size*(page-1)【size为一次显示的条数，此处设置默认为5，page为在网页上选择第几页】

        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
        Integer totalPage;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Integer offset = size * (page - 1);
        QuestionExample questionExample = new QuestionExample();

        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        PageDTO<QuestionDTO> pageDTO = new PageDTO<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);//将question对象的属性抄给question对象
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pageDTO.setPageNation(totalPage, page);
        pageDTO.setData(questionDTOS);
        //Integer totalCount = questionMapper.count();

        return pageDTO;
    }

    public PageDTO list(Long userId, Integer page, Integer size) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
        Integer totalPage;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        System.out.println(totalPage);
        Integer offset = size * (page - 1);
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOS = new ArrayList<>();
        PageDTO<QuestionDTO> pageDTO = new PageDTO<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);//将question对象的属性抄给question对象
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        pageDTO.setPageNation(totalPage, page);
        pageDTO.setData(questionDTOS);
        //Integer totalCount = questionMapper.count();

        return pageDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            question.setGmtCreate(System.currentTimeMillis());
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        } else {
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setDescription(question.getDescription());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());//额，其实我觉得应该直接用传过来的question，只要设置一个gmtModified就好了
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        } else {
            String[] tags = StringUtils.split(queryDTO.getTag(), ",");
            String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));//因为想用正则表达式来查找标签，用|连接，QuestionExtMapper.xml中该方法的语句就类似于-->【select * from QUESTION where ID != 782 and TAG REGEXP 'ES|mysql';】,可以查找出tag中含ES和mysql的，当然，查找的是相关问题，不要包含自己
            Question question = new Question();
            question.setId(queryDTO.getId());
            question.setTag(regexpTag);
            List<Question> questions = questionExtMapper.selectRelated(question);
            List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
                QuestionDTO  questionDTO= new QuestionDTO();
                BeanUtils.copyProperties(q,questionDTO);
                return questionDTO;
            }).collect(Collectors.toList());
            return questionDTOS;
        }
    }
}
