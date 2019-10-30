package cn.msuno.restful.api.configuration;

import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_ABSOLUTE_TYPE;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_AB_LIST;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_ARRAY;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_CONSUMES;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DEFINITIONS;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DEPRECATED;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DESCRIPTION;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DOC;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DOT;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_E;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_EMPTY;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_GT;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_ITEMS;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_JSON_PATH;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_LT;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_METHOD;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_METHODS;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_NAME;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_OBJECT;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_OPERATIONID;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_PARAM_TYPE;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_PATH;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_PRODUCES;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_PROPERTIES;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_REF;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_SCHEMA;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_SWAGGER;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_T;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_TAG;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_TAGS;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_TITLE;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_TYPE;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_USING;
import static cn.msuno.restful.api.json.JavadocUtils.JAVADOC_RESOURCE_SUFFIX;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.msuno.restful.api.bean.Info;
import cn.msuno.restful.api.bean.Swagger;
import cn.msuno.restful.api.json.JavadocUtils;

@Component
@ConditionalOnWebApplication
@EnableConfigurationProperties(RestfulApiProperties.class)
public class RestfulApiConfiguration {
    
    private static final Map<String, Swagger> cache = new HashMap<>();
    
    private static final Pattern blockSeparator = Pattern.compile("\\s", Pattern.MULTILINE);
    
    private static final Pattern description = Pattern.compile("\\s*@", Pattern.MULTILINE);
    
    private static final Set<String> bean = new HashSet<>();
    
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
    
    @Autowired
    private Map<String,String> responseCode;
    
    @Bean
    @ConditionalOnMissingBean(Swagger.class)
    public Swagger apiSwagger(){
        if (cache.containsKey(ELEMENT_SWAGGER)) {
            return cache.get(ELEMENT_SWAGGER);
        }
        Swagger swagger = new Swagger().setInfo(apiInfo()).setBasePath(restfulApiProperties.getBasePath()).setHost(restfulApiProperties.getHost());
        build(swagger);
        swagger.setStatusCode(responseCode);
        cache.put(ELEMENT_SWAGGER, swagger);
        return swagger;
    }
    
    private void build(Swagger swagger) {
        SwaggerUtils.listFiles(ELEMENT_JSON_PATH).stream().forEach(v -> {
            JSONObject json = SwaggerUtils.toJson(SwaggerUtils.readFile(v));
            swagger.setTag(buildTag(json));
            path(swagger, json);
            buildDefinitions(swagger, bean);
        });
    }
    
    private void path(Swagger swagger, JSONObject json) {
        JSONArray methods = json.getJSONArray(ELEMENT_METHODS);
        for (int i = 0; i < methods.size(); i ++) {
            JSONObject jsonObject = methods.getJSONObject(i);
            JSONArray childPath = jsonObject.getJSONArray(ELEMENT_PATH);
            JSONArray mds = jsonObject.getJSONArray(ELEMENT_METHOD);
            String absoluteType = jsonObject.getString(ELEMENT_ABSOLUTE_TYPE);
            jsonObject.remove(ELEMENT_PATH);
            jsonObject.remove(ELEMENT_METHOD);
            jsonObject.remove(ELEMENT_ABSOLUTE_TYPE);
            jsonObject.put(ELEMENT_TAGS, Collections.singletonList(buildTag(json).get(ELEMENT_NAME)));
            JSONArray parameters = jsonObject.getJSONArray(ELEMENT_PARAM_TYPE);
            for (int m = 0; m < parameters.size(); m ++) {
                JSONObject object = parameters.getJSONObject(m);
                String type = object.getString(ELEMENT_TYPE);
                if (JavadocUtils.baseType(type)) {
                    bean.add(type);
                    String chs = JavadocUtils.setRef(type);
                    if (JavadocUtils.isArray(type)) {
                        JSONObject ob = new JSONObject();
                        ob.put(ELEMENT_TYPE, ELEMENT_ARRAY);
                        JSONObject oj = new JSONObject();
                        oj.put(ELEMENT_REF, ELEMENT_DEFINITIONS + JavadocUtils.getSimpleName(chs));
                        ob.put(ELEMENT_ITEMS, oj);
                        object.put(ELEMENT_SCHEMA, ob);
                    } else {
                        JSONObject ob = new JSONObject();
                        ob.put(ELEMENT_REF, ELEMENT_DEFINITIONS + JavadocUtils.getSimpleName(chs));
                        object.put(ELEMENT_SCHEMA, ob);
                    }
                    object.remove(ELEMENT_TYPE);
                }
            }
            bean.add(absoluteType);
            for (int k = 0; k < childPath.size(); k ++) {
                for (int j = 0; j < mds.size(); j++) {
                    JSONObject o = new JSONObject();
                    jsonObject.put(ELEMENT_OPERATIONID, mds.getString(j).toLowerCase() + ELEMENT_USING + mds.getString(j).toUpperCase());
                    if (jsonObject.containsKey(ELEMENT_CONSUMES)  && ELEMENT_E.equals(jsonObject.getString(ELEMENT_CONSUMES))) {
                        jsonObject.put(ELEMENT_CONSUMES, ELEMENT_EMPTY);
                    }
                    if (jsonObject.containsKey(ELEMENT_PRODUCES) && ELEMENT_E.equals(jsonObject.getString(ELEMENT_PRODUCES))) {
                        jsonObject.put(ELEMENT_PRODUCES, ELEMENT_EMPTY);
                    }
                    o.put(mds.getString(j).toLowerCase(), jsonObject);
                    swagger.setPath(childPath.getString(k), o);
                }
            }
        }
        
    }
    
    private void buildDefinitions(Swagger swagger, Set<String> bean){
        Set<String> set = new HashSet<>();
        for (String type : bean) {
            if (ELEMENT_AB_LIST.equals(type) || type.startsWith(ELEMENT_AB_LIST) || !JavadocUtils.baseType(type) || !JavadocUtils.convertType(type).equals(type)) {
                continue;
            }
            String key = JavadocUtils.getSimpleName(type);
            String[] split = type.split(ELEMENT_LT);
            JSONObject jsonObject = SwaggerUtils.toJson(
                    SwaggerUtils.readFile(SwaggerUtils.getFile(split[0].replace(ELEMENT_GT, ELEMENT_EMPTY) + JAVADOC_RESOURCE_SUFFIX)));
            JSONObject properties = definitions(type, jsonObject.getJSONObject(ELEMENT_PROPERTIES), set);
            if (properties != null) {
                jsonObject.put(ELEMENT_PROPERTIES, properties);
            }
            jsonObject.remove(ELEMENT_DOC);
            jsonObject.remove(ELEMENT_NAME);
            jsonObject.remove(ELEMENT_DEPRECATED);
            jsonObject.put(ELEMENT_TYPE, ELEMENT_OBJECT);
            jsonObject.put(ELEMENT_TITLE, key);
            swagger.setDefinition(key, jsonObject);
            for (int i = 1; i < split.length; i ++) {
                String s = split[i].replace(ELEMENT_GT, ELEMENT_EMPTY);
                set.add(s);
            }
        }
        if (!set.isEmpty()) {
            buildDefinitions(swagger, set);
        }
    }
    
    private JSONObject definitions(String type, JSONObject jsonObject, Set<String> set){
        if (null == jsonObject || jsonObject.isEmpty()) {
            return null;
        }
        JSONObject res = new JSONObject();
        for (Map.Entry<String, Object> entry: jsonObject.entrySet()) {
            JSONObject object = JSONObject.parseObject(entry.getValue().toString());
            String strType = object.getString(ELEMENT_TYPE);
            if (ELEMENT_T.equals(strType)){
                String s = JavadocUtils.setRef(type);
                if (JavadocUtils.isArray(s)) {
                    s = JavadocUtils.getSimpleName(JavadocUtils.setRef(s));
                    object.put(ELEMENT_TYPE, ELEMENT_ARRAY);
                    JSONObject oj = new JSONObject();
                    oj.put(ELEMENT_REF, ELEMENT_DEFINITIONS + s);
                    object.put(ELEMENT_ITEMS, oj);
                } else {
                    object.put(ELEMENT_REF, ELEMENT_DEFINITIONS + JavadocUtils.getSimpleName(s));
                    object.remove(ELEMENT_TYPE);
                }
            } else if (JavadocUtils.baseType(strType)) {
                String chs = JavadocUtils.setRef(strType);
                if (JavadocUtils.isArray(strType)) {
                    object.put(ELEMENT_TYPE, ELEMENT_ARRAY);
                    JSONObject oj = new JSONObject();
                    oj.put(ELEMENT_REF, ELEMENT_DEFINITIONS + JavadocUtils.getSimpleName(chs));
                    object.put(ELEMENT_ITEMS, oj);
                } else {
                    object.put(ELEMENT_REF, ELEMENT_DEFINITIONS + JavadocUtils.getSimpleName(chs));
                    object.remove(ELEMENT_TYPE);
                }
                set.add(strType);
            }
            res.put(entry.getKey(), object);
        }
        return res;
    }
    
    
    private Map<String, String> buildTag(JSONObject json) {
        String doc = json.getString(ELEMENT_DOC);
        String[] split = description.split(doc);
        String name = json.getString(ELEMENT_NAME);
        name = name.substring(name.lastIndexOf(ELEMENT_DOT));
        Map<String, String> tag = new HashMap<>();
        
        tag.put(ELEMENT_DESCRIPTION, split[0] == null ? name : split[0].trim());
        for (int i = 1; i < split.length; i ++) {
            String[] sp = blockSeparator.split(split[i].trim(), 2);
            if (ELEMENT_TAG.equals(sp[0]) && sp.length > 1) {
                name = sp[i];
            }
        }
        tag.put(ELEMENT_NAME, name);
        return tag;
    }
   
}
