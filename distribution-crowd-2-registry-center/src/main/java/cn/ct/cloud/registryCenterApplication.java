package cn.ct.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer //开启注册中心服务
public class registryCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(registryCenterApplication.class,args);
    }
}
