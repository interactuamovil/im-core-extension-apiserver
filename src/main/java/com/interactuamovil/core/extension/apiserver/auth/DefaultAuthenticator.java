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
public class DefaultAuthenticator<C extends ApiContext> implements Authenticator<C> {

    @Override
    public boolean authenticate(ApiRequest request, ApiResponse response, C context) throws ApiServerException {
        return Boolean.TRUE;
    }
    
}
