# restful-api-spring-boot-starter

### Usage
- 1、引入pom.xml依赖
```xml
<dependency>
  <groupId>cn.msuno</groupId>
   <artifactId>restful-api-spring-boot-starter</artifactId>
  <version>1.0.0</version>
  <scope>compile</scope>
</dependency>
```

- 2、在Springboot入口增加注解
>EnableRestfulApi
>> 直接引入注解会扫描整个包，如果不想扫描整个项目，可以增加value方法指定需要扫描的javadoc包

>example
```java
@SpringBootApplication
@EnableRestfulApi(value = {"cn.msuno.starter.controller", "cn.msuno.starter.model"})
public class StarterApplication {
    public static void main(String[] args) {
        SpringApplication.run(StarterApplication.class, args);
    }
}
```

- 3、项目配置
```yaml
restful:
  api:
    basePath: /test
    title: test
    version: 3.0
    description: 67890-
    license:
      name: 666666
      url: https://www.yzliusha.cn
    contact:
      email: msuno@yzliusha.cn
```

- 4、自定义全局枚举返回码
> 需要增加一个bean 类型为map
```java
@Component
public class Config {
    @Bean("responseCode")
    public Map<String,String> responseCode() {
        Map<String, String> responseCode = new HashMap<>();
        for(StatusCode s : StatusCode.values()) {
            responseCode.put(String.valueOf(s.getCode()), s.getDesc());
        }
        return responseCode;
    }
}
```

##### 注：
项目通过编译javadoc信息，生成swagger.json, 页面显示UI使用了[swagger-bootstrap-ui](https://github.com/xiaoymin/Swagger-Bootstrap-UI)
