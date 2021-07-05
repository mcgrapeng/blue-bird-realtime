package com.bird.common.realtime;

import com.bird.common.web.thread.ContextAwarePoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 张朋
 * @version 1.0
 * @desc
 * @date 2021/5/14 17:58
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {BlueBirdCommonInfoflowServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)// 指定启动类
@Slf4j
public class MainTest {


    @Autowired
    private ContextAwarePoolExecutor executor;


    @Test
    public void test01() {


        for(int i = 1; i < 1 ;i++){
            System.out.println("===========");
        }

    }
}
