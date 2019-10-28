package cn.msuno.restful.api.bean;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Info {
    private String description = "RESTFUL API";
    private String version = "3.0";
    private String title = "RESTFUL API";
    private String termsOfService = "https://www.msuno.cn";
    private Contact contact = new Contact();
    private License license = new License();
    private Map<String, Object> vendorExtensions = new LinkedHashMap();
    
    public Info() {
    }
    
    public Info(String description, String version, String title, String termsOfService, Contact contact,
            License license) {
        this.description = description;
        this.version = version;
        this.title = title;
        this.termsOfService = termsOfService;
        this.contact = contact;
        this.license = license;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTermsOfService() {
        return termsOfService;
    }
    
    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
    }
    
    public Contact getContact() {
        return contact;
    }
    
    public void setContact(Contact contact) {
        this.contact = contact;
    }
    
    public License getLicense() {
        return license;
    }
    
    public void setLicense(License license) {
        this.license = license;
    }
    
    public Map<String, Object> getVendorExtensions() {
        return vendorExtensions;
    }
    
    public void setVendorExtensions(Map<String, Object> vendorExtensions) {
        this.vendorExtensions = vendorExtensions;
    }
}
