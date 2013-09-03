/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

/**
 *
 * @author sergeiw
 */
public class ApiResponseDescriptor {
    
    public static final ApiResponseDescriptor BAD_REQUEST;
    
    static {
        BAD_REQUEST = new ApiResponseDescriptor(400, 40001, "lkjkfsdl");
    }
    
    
    private int httpCode;
    private int apiCode;
    private String description;

    public ApiResponseDescriptor() {
    }

    public ApiResponseDescriptor(int httpCode, int apiCode, String description) {
        this.httpCode = httpCode;
        this.apiCode = apiCode;
        this.description = description;
    }

    /**
     * @return the httpCode
     */
    public int getHttpCode() {
        return httpCode;
    }

    /**
     * @param httpCode the httpCode to set
     */
    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    /**
     * @return the apiCode
     */
    public int getApiCode() {
        return apiCode;
    }

    /**
     * @param apiCode the apiCode to set
     */
    public void setApiCode(int apiCode) {
        this.apiCode = apiCode;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}
