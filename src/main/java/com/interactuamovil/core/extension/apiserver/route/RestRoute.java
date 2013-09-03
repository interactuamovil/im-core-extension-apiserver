/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver.route;

import com.interactuamovil.core.extension.apiserver.rest.RestApiContainer;

/**
 *
 * @author sergeiw
 */
public class RestRoute<T extends RestApiContainer> extends Route<T> {

    public RestRoute(String pathSegment) {
        super(pathSegment);
    }

    @Override
    protected Route<T> createRoute(String pathSegment) {
        return new RestRoute<T>(pathSegment);
    }
    
    
    
    
}
