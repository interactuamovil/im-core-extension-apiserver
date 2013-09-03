/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver.logger;

import com.interactuamovil.core.application.ApplicationException;
import com.interactuamovil.core.extension.apiserver.ApiContext;
import com.interactuamovil.core.extension.apiserver.ApiLogger;
import com.interactuamovil.core.extension.apiserver.ApiRequest;
import com.interactuamovil.core.extension.apiserver.ApiResponse;

/**
 *
 * @author sergeiw
 */
public class DefaultApiLogger implements ApiLogger {

    @Override
    public void registerRequest(ApiRequest request, ApiContext context) throws ApplicationException {
        
    }

    @Override
    public void registerResponse(ApiResponse response, ApiContext context) throws ApplicationException {
        
    }

    
}
