package cn.msuno.restful.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import cn.msuno.restful.api.configuration.RestfulApiConfiguration;
import cn.msuno.restful.api.configuration.RestfulController;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import({RestfulApiConfiguration.class, RestfulController.class})
public @interface EnableRestfulApi {
    /**
     * 扫描包路径
     * @return []
     */
    String[] value() default {};
    
    /**
     * 生成一个json文件
     * @return true
     */
    boolean single() default false;
}
