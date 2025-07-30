package org.example.myspringapp.models;

public class QrRequest {
    private String link;
    private String name;

    public QrRequest() {}

    public QrRequest(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
