package cn.msuno.restful.api.bean;

public class Contact {
    
    private String name = "msuno";
    private String url = "https://www.msuno.cn";
    private String email = "msuno@msuno.cn";
    
    public Contact() { }
    
    public Contact(String name, String url, String email) {
        this.name = name;
        this.url = url;
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public String getUrl() {
        return url;
    }
    
    public Contact url(String url) {
        this.url = url;
        return this;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
