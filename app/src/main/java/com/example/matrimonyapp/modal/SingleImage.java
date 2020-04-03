package com.example.matrimonyapp.modal;

import java.io.Serializable;

public class SingleImage implements Serializable {

    public String uri;

    public SingleImage(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
