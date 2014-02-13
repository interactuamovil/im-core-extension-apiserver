/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

import com.interactuamovil.core.application.ApplicationException;
import com.interactuamovil.core.extension.apiserver.auth.Authenticator;
import com.interactuamovil.core.extension.apiserver.auth.DefaultAuthenticator;
import com.interactuamovil.core.extension.apiserver.logger.DefaultApiLogger;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.log4j.Logger;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.core.Container;

/**
 *
 * @author sergeiw
 */
public abstract class ApiContainer<C extends ApiContext> implements Container {

    private static final Logger logger = Logger.getLogger(ApiContainer.class);
    
    private Authenticator<C> authenticator;
    private ApiLogger apiLogger;

    public ApiContainer() {
        this(new DefaultAuthenticator<C>(), new DefaultApiLogger());
    }

    /*public ApiContainer(Authenticator<C> authenticator) {
        this.authenticator = authenticator;
    }*/
    
    public ApiContainer(Authenticator<C> authenticator, ApiLogger apiLogger) {
        this.authenticator = authenticator;
        this.apiLogger = apiLogger;
    }
    
    
    
    protected void setResponse(ApiResponse rspns, String responseText) {
        setResponse(rspns, responseText, Status.OK, 0);
    }
    
    protected void setResponse(ApiResponse rspns, Object jsonResponse) {
        setResponse(rspns, jsonResponse, Status.OK, 0);
    }
    
    protected void setResponse(ApiResponse rspns, Object jsonResponse, Status status) {
        setResponse(rspns, jsonResponse, status, 0);
    }
    
    protected void setResponse(ApiResponse rspns, Object jsonResponse, Status status, Integer apiStatus) {
        String message;
        try {
            message = jsonResponse.toString();
        } catch (Exception ex) {
            message = "{ \"code\":\"503\", \"error\":\"Error interno.\"}";
        }
        setResponse(rspns, message, status, apiStatus);
    }
    
    protected void setResponse(ApiResponse rspns, String responseText, Status status, Integer apiStatus) {
        PrintStream body = null;
        try {
            rspns.setCode(status.getCode());
            rspns.setDescription(status.getDescription());
            rspns.setApiStatus(apiStatus);
            rspns.setValue("Content-Type", "application/json");
            body = rspns.getPrintStream();
            body.print(responseText);
            
            if (logger.isTraceEnabled()) {
                logger.trace(String.format("Response: %d %s - %s", 
                        rspns.getCode(),
                        rspns.getDescription(),
                        responseText));
            }
            
        } catch (IOException ex) {
            logger.error("Unexpected error sending response body");
        } finally {
            if (body != null) {
                body.close();
            }
        }
    }
    
    @Override
    public void handle(Request request, Response response) {
        handle((ApiRequest)request, (ApiResponse)response);
    }
        
    public void handle(ApiRequest request, ApiResponse response) {        
        C context = createContext(request);
        try {            
            
            //request.setResourceName(resourceName);
            if (validateParameters(request, response, context)) {
                if (this.authenticator.authenticate(request, response, context)) {
                    logger.trace("Authentication: AUTHORIZED");
                    apiLogger.registerRequest(request, context);
                    handle((ApiRequest)request, (ApiResponse)response, context);
                } else {
                    logger.trace("Authentication: DENIED");
                    apiLogger.registerRequest(request, context);
                    setResponse(response, ErrorJsonResponse.create(401, "No autorizado"), Status.UNAUTHORIZED);
                }
            }
        } catch(Exception ex) {
            logger.error("Error al procesar la consulta", ex);            
            setResponse(response, ErrorJsonResponse.create(503, "Error interno. Reintentar"), Status.INTERNAL_SERVER_ERROR);
        } finally {
            try {
                response.close();
                apiLogger.registerResponse(response, context);
            } catch (IOException ex) {
                logger.error("Unable to commit response", ex);
            } catch (ApplicationException ex) {
                logger.error("Unable to register response", ex);
            }
            
            context.close();
            
        }
    }
    
    abstract protected void handle(ApiRequest request, ApiResponse response, C context);
    abstract public String getHandlerName();
    abstract protected boolean validateParameters(ApiRequest request, ApiResponse response, C context);
    
    protected C createContext(ApiRequest request) {
        return (C) new DefaultApiContext();        
    }
}
