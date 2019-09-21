package cn.ct.cloud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisProviderTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void testDemo(){
        redisTemplate.opsForValue().set("name","chentao");
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println(name);

    }
}
