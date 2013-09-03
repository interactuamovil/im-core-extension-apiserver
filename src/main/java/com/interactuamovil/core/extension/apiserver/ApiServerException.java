/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

/**
 *
 * @author sergeiw
 */
public class ApiServerException extends Exception {

    public ApiServerException() {
    }

    public ApiServerException(String string) {
        super(string);
    }

    public ApiServerException(Throwable thrwbl) {
        super(thrwbl);
    }

    public ApiServerException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
    
    
    
}
