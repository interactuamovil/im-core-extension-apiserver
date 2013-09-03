/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver.rest;

import com.interactuamovil.core.extension.apiserver.*;
import com.interactuamovil.core.extension.apiserver.http.Post;
import com.interactuamovil.core.extension.apiserver.http.Put;
import com.interactuamovil.core.extension.apiserver.http.Delete;
import com.interactuamovil.core.extension.apiserver.http.HttpMethods;
import com.interactuamovil.core.extension.apiserver.http.Path;
import com.interactuamovil.core.extension.apiserver.http.Get;
import com.interactuamovil.core.extension.apiserver.auth.Authenticator;
import com.interactuamovil.core.extension.apiserver.auth.DefaultAuthenticator;
import com.interactuamovil.core.extension.apiserver.logger.DefaultApiLogger;
import com.interactuamovil.core.extension.apiserver.route.Router;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.simpleframework.http.Status;



    
    
    
/**
 *
 * @author sergeiw
 */
public abstract class RestApiContainer<C extends RestApiContext> extends ApiContainer<C> {

    private static final Logger logger = Logger.getLogger(RestApiContainer.class);
    
    protected Map<String, MethodContainer> methodContainersCache = null; 
    private Router<MethodContainer> router = null;    

    
    
    public RestApiContainer() {
        this(new DefaultAuthenticator(), new DefaultApiLogger());
    }
    
    public RestApiContainer(Authenticator<C> authenticator, ApiLogger apiLogger) {
        super(authenticator, apiLogger);
        
        methodContainersCache = Collections.synchronizedMap(new HashMap<String, MethodContainer>());
        router =  new Router<MethodContainer>();
        router.setModifyPathInfo(Boolean.FALSE);
        
        initializeMethodHandlers();        
    }
    
    final synchronized void initializeMethodHandlers() {
        Class thisClass = this.getClass();

        Method[] methods = thisClass.getMethods();
        for (Method method : methods) {

            Method handlerMethod = null;
            HttpMethods httpMethod = null;
            String handlerPath = "/";

            if (method.getAnnotation(Get.class) != null) {
                handlerMethod = method;
                httpMethod = HttpMethods.GET;
            } else if (method.getAnnotation(Post.class) != null) {
                handlerMethod = method;
                httpMethod = HttpMethods.POST;
            } else if (method.getAnnotation(Put.class) != null) {
                handlerMethod = method;
                httpMethod = HttpMethods.PUT;
            } else if (method.getAnnotation(Delete.class) != null) {
                handlerMethod = method;
                httpMethod = HttpMethods.DELETE;
            }

            if (handlerMethod != null) {
                Path path = method.getAnnotation(Path.class);
                if (path != null) {
                    handlerPath = path.value();
                }

                MethodContainer mContainer = methodContainersCache.get(handlerPath);
                if (mContainer == null) {                        
                    mContainer = new MethodContainer();
                    methodContainersCache.put(handlerPath, mContainer);
                }                
                mContainer.put(httpMethod, handlerMethod);                    
                logger.trace(String.format("[%s] Registered Handler: %s => %s %s", 
                        getHandlerName(),
                        handlerPath, httpMethod, handlerMethod.getName()));
            }

        }

        for (Map.Entry<String, MethodContainer> entry : methodContainersCache.entrySet()) {
            router.attach(entry.getKey(), entry.getValue());
        }       

    }
    
    
    @Override
    protected boolean validateParameters(ApiRequest request, ApiResponse response, C context) {
        MethodContainer methodContainer = router.resolveHandler(request, 
                                                 request.getPathInfo().getPath(), 
                                                 request.getPathInfo().getSegments());
        
        if (methodContainer != null) {
            Method handlerMethod = methodContainer.get(HttpMethods.valueOf(request.getMethod()));
            if (handlerMethod != null) {
                logger.trace("Found handler method: " + handlerMethod.getName());
                context.setHandlerMethod(handlerMethod);
                return Boolean.TRUE;            
            } else {
                logger.trace("No handler method");
                setResponse(response, 
                        ErrorJsonResponse.create(405, "Method not allowed"), 
                        Status.METHOD_NOT_ALLOWED);
                return Boolean.FALSE;
            }
        } else {
            logger.trace("No handler method");
            setResponse(response, 
                    ErrorJsonResponse.create(405, "Method not allowed"), 
                    Status.METHOD_NOT_ALLOWED);
            return Boolean.FALSE;
        }
    }

    
    
    @Override
    protected void handle(ApiRequest request, ApiResponse response, C context) {
        
        if (context.getHandlerMethod() != null) {            
            try {
                context.getHandlerMethod().invoke(this, new Object[] { request, response, context });
            } catch (IllegalAccessException ex) {
                logger.error(ex);
            } catch (IllegalArgumentException ex) {
                logger.error(ex);
            } catch (InvocationTargetException ex) {
                logger.error(ex);
            }
        } else {
            logger.trace("No handler method");
            setResponse(response, 
                    ErrorJsonResponse.create(405, "Method not allowed"), 
                    Status.METHOD_NOT_ALLOWED);
        }
                
    }
    
    protected C createContext(ApiRequest request) {
        return (C) new RestApiContext();        
    }
    
}
