package cn.msuno.restful.api.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;


public class Swagger {
    
    protected String swagger = "2.0";
    protected Info info;
    protected String host;
    protected String basePath;
    protected List<Map<String, String>> tags = new ArrayList<>();
    protected Map<String, JSONObject> paths = new HashMap<>();
    protected Map<String, JSONObject> definitions = new HashMap<>();
    private Map<String, String> statusCode = new HashMap<>();
    
    public Swagger() { }
    
    public String getSwagger() {
        return swagger;
    }
    
    public Swagger setSwagger(String swagger) {
        this.swagger = swagger;
        return this;
    }
    
    public Info getInfo() {
        return info;
    }
    
    public Swagger setInfo(Info info) {
        this.info = info;
        return this;
    }
    
    public String getHost() {
        return host;
    }
    
    public Swagger setHost(String host) {
        this.host = host;
        return this;
    }
    
    public String getBasePath() {
        return basePath;
    }
    
    public Swagger setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }
    
    public List<Map<String, String>> getTags() {
        return tags;
    }
    
    public Swagger setTags(List<Map<String, String>> tags) {
        this.tags = tags;
        return this;
    }
    
    public Swagger setTag(Map<String, String> tag) {
        if (null == this.tags) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tag);
        return this;
    }
    
    public Map<String, JSONObject> getPaths() {
        return paths;
    }
    
    public Map<String, JSONObject> getDefinitions() {
        return definitions;
    }
    
    public Swagger setDefinitions(Map<String, JSONObject> definitions) {
        this.definitions = definitions;
        return this;
    }
    
    public Swagger setDefinition(String key, JSONObject json) {
        if(null == this.definitions) {
            this.definitions = new HashMap<>();
        }
        this.definitions.put(key, json);
        return this;
    }
    
    public Swagger setPaths(Map<String, JSONObject> paths) {
        this.paths = paths;
        return this;
    }
    
    public Swagger setPath(String path,JSONObject json) {
        if (null == this.paths) {
            this.paths = new HashMap<>();
        }
        this.paths.put(path, json);
        return this;
    }
    
    public Map<String, String> getStatusCode() {
        return statusCode;
    }
    
    public Swagger setStatusCode(Map<String, String> statusCode) {
        this.statusCode = statusCode;
        return this;
    }
}
