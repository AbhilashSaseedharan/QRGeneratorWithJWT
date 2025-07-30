package org.example.myspringapp.models;

public class ApiResponse<T> {
    private String version;
    private T response;

    public ApiResponse() {}

    public ApiResponse(String version, T response) {
        this.version = version;
        this.response = response;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}

