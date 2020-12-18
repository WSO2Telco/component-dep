package com.wso2telco.workflow.model;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

public class TierUpdtConnDTO {

    private String serverEP;
    private String path;
    private MediaType mediaType;
    private MultivaluedMap<String,String> headers;
    private BaseTierUpdtReq tierUpdtReq;

    public String getServerEP() {
        return serverEP;
    }

    public void setServerEP(String serverEP) {
        this.serverEP = serverEP;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public MultivaluedMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(MultivaluedMap<String, String> headers) {
        this.headers = headers;
    }

    public BaseTierUpdtReq getTierUpdtReq() {
        return tierUpdtReq;
    }

    public void setTierUpdtReq(BaseTierUpdtReq tierUpdtReq) {
        this.tierUpdtReq = tierUpdtReq;
    }
}
