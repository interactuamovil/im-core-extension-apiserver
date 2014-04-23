/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

/**
 *
 * @author sergeiw
 */
public interface ApiContext {

    boolean isAuthenticated();
    void setAuthenticated(boolean authenticated);
    
    public String getApiType();
    
    void commit();
    void rollback();
    void close();
}
