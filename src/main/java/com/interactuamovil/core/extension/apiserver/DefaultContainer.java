/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

import org.apache.log4j.Logger;

/**
 *
 * @author sergeiw
 */
public class DefaultContainer extends ApiContainer<DefaultApiContext> {

    private static final Logger logger = Logger.getLogger(DefaultContainer.class);
        
    private static final String handlerName = DefaultContainer.class.getCanonicalName();
    
    @Override
    protected void handle(ApiRequest request, ApiResponse response, DefaultApiContext context) {
        setResponse(response, "Default Container: OK");
    }

    @Override
    public String getHandlerName() {
        return handlerName;
    }

    @Override
    protected boolean validateParameters(ApiRequest request, ApiResponse response, DefaultApiContext context) {
        return Boolean.TRUE;
    }
    
}
