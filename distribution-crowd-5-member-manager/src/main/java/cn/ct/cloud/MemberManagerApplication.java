package cn.ct.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients //启用微服务
public class MemberManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemberManagerApplication.class,args);
    }
}
