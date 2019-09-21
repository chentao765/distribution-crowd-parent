package cn.ct.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
// @EnableEurekaClient 专门针对Eureka注册中心
@EnableDiscoveryClient // 更为通用
@MapperScan("cn.ct.cloud.mapper")
public class DatabaseProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(DatabaseProviderApplication.class,args);
    }
}
