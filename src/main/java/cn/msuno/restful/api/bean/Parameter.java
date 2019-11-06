package cn.msuno.restful.api.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class Parameter {
    @JSONField(name = "default")
    private String defaultValue = "";
    
    private String in = "query";
    
    private String name = "";
    
    private String description = "";
    
    private String type = "";
    
    private boolean required = false;
    
    public String getDefaultValue() {
        return defaultValue;
    }
    
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public String getIn() {
        return in;
    }
    
    public void setIn(String in) {
        this.in = in;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public boolean isRequired() {
        return required;
    }
    
    public void setRequired(boolean required) {
        this.required = required;
    }
}
