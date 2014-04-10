package com.ultima.settings.otaupdater;

public class UpdateResult {
    
    private String name;
    private String version;
    private String codename;
    private String url;
    private String md5;
    private String change;
    private String android;

    public UpdateResult() {
    }

    public String getName(){
        return name;
    }
    
    public String getVersion(){
        return version;
    }
    
    public String getCodename(){
        return codename;
    }
    
    public String getUrl(){
        return url;
    }
    
    public String getMd5(){
        return md5;
    }
    
    public String getChangelog(){
        return change;
    }
    
    public String getAndroidVersion(){
        return android;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setVersion(String version){
        this.version = version;
    }
    
    public void setCodename(String codename){
        this.codename = codename;
    }
    
    public void setUrl(String url){
        this.url = url;
    }
    
    public void setMd5(String md5){
        this.md5 = md5;
    }
    
    public void setChangelog(String change){
        this.change = change;
    }
    
    public void setAndroidVersion(String android){
        this.android = android;
    }
    
}
