/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

import org.apache.commons.configuration.Configuration;

/**
 *
 * @author sergeiw
 */
public class ApiSettings {


    private static Configuration config;

    private static String serviceUri;
    private static int servicePort;
    private static int activeThreads;
    
    private static final int DEFAULT_ACTIVE_THREADS = 30;
        
        
    public static void init(Configuration c) {
        config = c;
        String configPrefix = "app.api";

        serviceUri = config.getString(String.format("%s.serviceUris", configPrefix));
        servicePort = config.getInt(String.format("%s.servicePort", configPrefix));
        activeThreads = config.getInt(String.format("%s.activeThreads", configPrefix), DEFAULT_ACTIVE_THREADS);
                
    }

    /**
     * @return the serviceUri
     */
    public static String getServiceUri() {
        return serviceUri;
    }

    /**
     * @return the servicePort
     */
    public static int getServicePort() {
        return servicePort;
    }

    /**
     * @return the activeThreads
     */
    public static int getActiveThreads() {
        return activeThreads;
    }

    
    
}
