/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver.route;

import com.interactuamovil.core.extension.apiserver.ApiContainer;

/**
 *
 * @author sergeiw
 */
public class SimpleRoute<T extends ApiContainer> extends Route<T> {

    public SimpleRoute(String pathSegment) {
        super(pathSegment);
    }

    @Override
    protected Route<T> createRoute(String pathSegment) {
        return new SimpleRoute<T>(pathSegment);
    }
    
    
     
}
