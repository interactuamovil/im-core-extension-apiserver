/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

/**
 *
 * @author sergeiw
 */
public class DefaultApiContext implements ApiContext {
    
    //private boolean needsAuthentication = Boolean.FALSE;
    private boolean authenticated = Boolean.FALSE;
    private String apiType = "HTTP";

    public DefaultApiContext() {
    }

    protected DefaultApiContext(String apiType) {
        this.apiType = apiType;
    }
    
    /**
     * @return the authenticated
     */
    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * @param authenticated the authenticated to set
     */
    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    @Override
    public void close() {        
    }

    @Override
    public String getApiType() {
        return this.apiType;
    }
    
    
    
}
