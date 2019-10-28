package cn.msuno.restful.api.bean;

import java.util.LinkedHashMap;
import java.util.Map;

public class License {
    
    private String name = "msuno";
    private String url = "https://wwww.msuno.cn";
    private Map<String, Object> vendorExtensions = new LinkedHashMap();
    
    public License() {    }
    
    public License(String name, String url) {
        this.name = name;
        this.url = url;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Map<String, Object> getVendorExtensions() {
        return vendorExtensions;
    }
    
    public void setVendorExtensions(Map<String, Object> vendorExtensions) {
        this.vendorExtensions = vendorExtensions;
    }
}
