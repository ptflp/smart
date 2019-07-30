package com.microservice.cloud.models;


public class FileResponse {

    private String name;
    private String uri;
    private String fileType;

    public FileResponse(String name, String uri, String fileType) {
        this.name = name;
        this.uri = uri;
        this.fileType = fileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}
