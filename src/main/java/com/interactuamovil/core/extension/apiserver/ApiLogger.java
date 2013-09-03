/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

import com.interactuamovil.core.application.ApplicationException;

/**
 *
 * @author sergeiw
 */
public interface ApiLogger<C extends ApiContext> {
    
    
    public void registerRequest(ApiRequest request, C context) throws ApplicationException;
    public void registerResponse(ApiResponse response, C context) throws ApplicationException;
        
}
