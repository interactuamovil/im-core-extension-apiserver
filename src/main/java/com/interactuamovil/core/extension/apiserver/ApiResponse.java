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
    
    ApiResponseDescriptor responseDescriptor;
    JsonObject error;
    
    public ApiResponse(Response response) {
        super(response);
    }
    
    public void setResponseDescriptor(ApiResponseDescriptor descriptor) {
        this.responseDescriptor = descriptor;
    }
    
    
    
}
