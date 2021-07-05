package com.bird.common.realtime;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.bird.common.realtime"
        , "com.bird.common.web", "com.bird.common.web.ex","com.bird.common.security","com.bird.common.tools.video"
        ,"com.bird.common.mq"})
public class BlueBirdCommonInfoflowServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlueBirdCommonInfoflowServiceApplication.class, args);
    }

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

}
