/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

/**
 *
 * @author sergeiw
 */
public class InvalidParameterException extends ApiServerException {

    private String parameterName;    
    
    public InvalidParameterException() {
    }

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(Throwable thrwbl) {
        super(thrwbl);
    }

    public InvalidParameterException(String message, Throwable thrwbl) {
        super(message, thrwbl);
    }
    
    public InvalidParameterException(String parameterName, String message, Throwable thrwbl) {
        super(message, thrwbl);
        this.parameterName = parameterName;
    }
    
    public InvalidParameterException(String parameterName, String message) {
        super(message);
        this.parameterName = parameterName;
    }

    /**
     * @return the parameterName
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * @param parameterName the parameterName to set
     */
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    
    
    
    
}
