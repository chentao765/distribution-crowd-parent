package cn.ct.cloud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseProviderTest {
    @Autowired
    private DataSource dataSource;

    @Test
    public void testDemo() throws  Exception{
        System.out.println(dataSource.getConnection());
    }
}
