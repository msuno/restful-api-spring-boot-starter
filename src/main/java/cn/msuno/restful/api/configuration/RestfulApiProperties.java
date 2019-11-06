package cn.msuno.restful.api.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import cn.msuno.restful.api.bean.Contact;
import cn.msuno.restful.api.bean.License;

@ConfigurationProperties(prefix = "restful.api")
public class RestfulApiProperties {

    private String title = "Restful API";
    private String swagger = "3.0";
    private String host = "http://localhost:8080";
    private String basePath = "/";
    private String description = "RESTFUL API";
    private String version = "3.0";
    private String termsOfService = "https://www.msuno.cn";
    private Contact contact = new Contact();
    private License license = new License();
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getSwagger() {
        return swagger;
    }
    
    public void setSwagger(String swagger) {
        this.swagger = swagger;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getBasePath() {
        return basePath;
    }
    
    public void setBasePath(String basePath) {
        this.basePath = basePath;
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
}
