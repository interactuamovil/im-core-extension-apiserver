/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver.rest;

import com.interactuamovil.core.extension.apiserver.DefaultApiContext;
import java.lang.reflect.Method;

/**
 *
 * @author sergeiw
 */
public class RestApiContext extends DefaultApiContext {

    public RestApiContext() {
        this("REST");
    }
    
    protected RestApiContext(String apiType) {
        super(apiType);
    }        
    
    private Method handlerMethod;

    /**
     * @return the handlerMethod
     */
    public Method getHandlerMethod() {
        return handlerMethod;
    }

    /**
     * @param handlerMethod the handlerMethod to set
     */
    public void setHandlerMethod(Method handlerMethod) {
        this.handlerMethod = handlerMethod;
    }
    
}
