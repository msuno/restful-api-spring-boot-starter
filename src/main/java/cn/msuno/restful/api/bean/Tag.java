package cn.msuno.restful.api.bean;

import java.util.LinkedHashMap;
import java.util.Map;

public class Tag {
    private Map<String, Object> vendorExtensions = new LinkedHashMap();
    private String name;
    private String description;
   
    public Tag() { }
    
    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public Tag setName(String name) {
        this.name = name;
        return this;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Tag setDescription(String description) {
        this.description = description;
        return this;
    }
    
    public Map<String, Object> getVendorExtensions() {
        return vendorExtensions;
    }
    
    public Tag setVendorExtensions(Map<String, Object> vendorExtensions) {
        this.vendorExtensions = vendorExtensions;
        return this;
    }
}
