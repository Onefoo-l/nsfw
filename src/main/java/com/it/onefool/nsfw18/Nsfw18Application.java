package com.it.onefool.nsfw18;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.it.onefool.nsfw18.mapper")
@EnableAspectJAutoProxy // 开启AOP
@EnableScheduling // 开启定时任务
@EnableTransactionManagement//开启注解事务
public class Nsfw18Application {

    private static final Logger log = LoggerFactory.getLogger(Nsfw18Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Nsfw18Application.class, args);
        log.info("app启动===============>");
    }

    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        interceptor.addInnerInterceptor(innerInterceptor);//如果配置多个插件,切记分页最后添加
        return interceptor;
    }
}
