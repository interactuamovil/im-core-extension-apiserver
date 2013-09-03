/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver.auth;

import com.interactuamovil.core.extension.apiserver.ApiContext;
import com.interactuamovil.core.extension.apiserver.ApiRequest;
import com.interactuamovil.core.extension.apiserver.ApiResponse;
import com.interactuamovil.core.extension.apiserver.ApiServerException;

/**
 *
 * @author sergeiw
 */
public interface Authenticator<T extends ApiContext> {
    
    public boolean authenticate(ApiRequest request, ApiResponse response, T context) throws ApiServerException;
    
}
