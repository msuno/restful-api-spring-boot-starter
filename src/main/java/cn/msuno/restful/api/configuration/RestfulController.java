package cn.msuno.restful.api.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import cn.msuno.restful.api.bean.Swagger;

@RestController
@ConditionalOnMissingClass({"restfulController"})
public class RestfulController {
    
    @Autowired
    private Swagger swagger;
    
    @GetMapping("/swagger-resources")
    public List<Map> swaggerResources(){
        Map<String, String> result = Maps.newHashMap();
        result.put("location", "/v3/api-docs");
        result.put("name","default");
        result.put("swaggerVersion", "20");
        result.put("url", "/v3/api-docs");
        return Collections.singletonList(result);
    }
    
    @GetMapping("/v3/api-docs")
    public String swagger() {
        return JSON.toJSONString(swagger);
    }

}
