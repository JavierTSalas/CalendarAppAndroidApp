package com.salas.javiert.magicmirror.Objects;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by javi6 on 6/10/2017.
 */

public class information {
    private String url;
    private StringEntity myEntity;

    public information(String url, StringEntity myEntity) {
        this.url = url;
        this.myEntity = myEntity;
    }

    public String getUrl() {
        return url;
    }

    public StringEntity getMyEntity() {
        return myEntity;
    }
}
