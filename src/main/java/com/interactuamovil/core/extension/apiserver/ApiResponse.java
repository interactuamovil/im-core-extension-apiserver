/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

import org.simpleframework.http.Response;
import org.simpleframework.http.ResponseWrapper;

/**
 *
 * @author sergeiw
 */
public class ApiResponse extends ResponseWrapper {
    

    private Integer apiStatus;
    
    public ApiResponse(Response response) {
        super(response);
    }

    /**
     * @return the apiStatus
     */
    public Integer getApiStatus() {
        return apiStatus;
    }

    /**
     * @param apiStatus the apiStatus to set
     */
    public void setApiStatus(Integer apiStatus) {
        this.apiStatus = apiStatus;
    }
    
    
    
}
