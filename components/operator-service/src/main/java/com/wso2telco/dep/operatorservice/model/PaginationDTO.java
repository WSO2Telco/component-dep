package com.wso2telco.dep.operatorservice.model;

public class PaginationDTO {

    private int limit;
    private int offset;

    public PaginationDTO(int limit, int offset){
        this.limit = limit;
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
