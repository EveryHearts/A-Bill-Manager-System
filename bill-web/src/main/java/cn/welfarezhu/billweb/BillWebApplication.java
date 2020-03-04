package cn.welfarezhu.billweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "cn.welfarezhu.billservice.mapper")
@ComponentScan(basePackages = {"cn.welfarezhu.billservice","cn.welfarezhu.billservice.config","cn.welfarezhu.billweb","cn.welfarezhu.billweb.config"})
public class BillWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillWebApplication.class, args);
    }

}