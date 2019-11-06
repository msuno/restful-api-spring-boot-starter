package cn.msuno.restful.api.configuration;

import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_EMPTY;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_JSON_PATH;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_JSON_RES_PATH;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_PARAM_TYPE;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_SWAGGER;
import static cn.msuno.restful.api.json.JavadocUtils.JAVADOC_RESOURCE_SUFFIX;
import static cn.msuno.restful.api.json.JavadocUtils.build;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.msuno.restful.api.bean.Info;
import cn.msuno.restful.api.bean.Parameter;
import cn.msuno.restful.api.bean.Swagger;

@Component
@ConditionalOnWebApplication
@EnableConfigurationProperties(RestfulApiProperties.class)
public class RestfulApiConfiguration {
    
    private static final Map<String, Swagger> cache = new HashMap<>();
    
    @Bean
    @ConditionalOnMissingBean(name = "responseCode")
    public Map<String, String> responseCode() {
        return new HashMap<>();
    }
    
    @Autowired
    private RestfulApiProperties restfulApiProperties;
    
    @Bean
    @ConditionalOnMissingBean(Info.class)
    public Info apiInfo() {
        Info info = new Info();
        info.setTitle(restfulApiProperties.getTitle());
        info.setVersion(restfulApiProperties.getVersion());
        info.setTermsOfService(restfulApiProperties.getTermsOfService());
        info.setLicense(restfulApiProperties.getLicense());
        info.setContact(restfulApiProperties.getContact());
        info.setDescription(restfulApiProperties.getDescription());
        return info;
    }
    
    @Bean
    @ConditionalOnMissingBean(name = "globalParameters")
    public List<Parameter> globalParameters(){
        return Collections.emptyList();
    }
    
    @Bean
    @ConditionalOnMissingBean(Swagger.class)
    public Swagger apiSwagger(@Autowired Map<String,String> responseCode,
            @Autowired List<Parameter> parameters){
        if (cache.containsKey(ELEMENT_SWAGGER)) {
            return cache.get(ELEMENT_SWAGGER);
        }
        File file = SwaggerUtils.getFile("swagger.json");
        Swagger swagger;
        if (null == file) {
            Set<JSONObject> controller = SwaggerUtils.listFiles(ELEMENT_JSON_PATH).stream().map(SwaggerUtils::toJson).collect(Collectors.toSet());
            Map<String, JSONObject> map = new HashMap<>();
            SwaggerUtils.listFiles(ELEMENT_JSON_RES_PATH).forEach(v -> map.put(v.getName().replace(JAVADOC_RESOURCE_SUFFIX, ELEMENT_EMPTY), SwaggerUtils.toJson(v)));
            swagger = build(controller, map);
        } else {
            JSONObject object = SwaggerUtils.toJson(file);
            swagger = JSON.parseObject(object.toJSONString(), Swagger.class);
        }
        swagger.setInfo(apiInfo()).setBasePath(restfulApiProperties.getBasePath()).setHost(restfulApiProperties.getHost());
        swagger.setStatusCode(responseCode);
        buildGlobalParameter(parameters, swagger);
        cache.put(ELEMENT_SWAGGER, swagger);
        return swagger;
    }
    
    private void buildGlobalParameter(List<Parameter> parameters, Swagger swagger){
        if (CollectionUtils.isEmpty(parameters)) {
            return ;
        }
        Map<String, JSONObject> paths = swagger.getPaths();
        for (Map.Entry<String, JSONObject> entry : paths.entrySet()) {
            JSONObject jsonObject = entry.getValue();
            for (Map.Entry<String, Object> en : jsonObject.entrySet()) {
                JSONObject obj = (JSONObject)en.getValue();
                JSONArray jsonArray = obj.getJSONArray(ELEMENT_PARAM_TYPE);
                JSONArray array = JSON.parseArray(JSON.toJSONString(parameters));
                jsonArray.addAll(array);
            }
        }
    }
    
    
    
    
    
    
   
}
