package com.pn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
//mapper接口扫描器，指明mapper接口所在包，然后会自动为mapper接口创建代理对象并加入到容器中
/*@MapperScan(basePackages = "com.abmotion.mapper")*/
//这里扫的包不对，所以在service的实现类注入mapper接口就注入不到
@MapperScan(basePackages = "com.pn.mapper")
@SpringBootApplication
public class WarehouseManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WarehouseManagerApplication.class, args);
    }
}
