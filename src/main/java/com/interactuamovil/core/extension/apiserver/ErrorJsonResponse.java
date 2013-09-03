/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

import com.interactuamovil.core.extension.apiserver.JsonObject;

/**
 *
 * @author sergeiw
 */
public class ErrorJsonResponse extends JsonObject {
    
    private Integer code;
    private String error;

    public ErrorJsonResponse() {
    }

    public ErrorJsonResponse(Integer code, String error) {
        this.code = code;
        this.error = error;
    }
    
    public static ErrorJsonResponse create(Integer code, String error) {
        return new ErrorJsonResponse(code, error);
    }
    
    
    
    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }
    
    
}
