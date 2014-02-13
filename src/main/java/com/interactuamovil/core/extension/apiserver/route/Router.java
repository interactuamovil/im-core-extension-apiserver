/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver.route;

import com.interactuamovil.core.extension.apiserver.ApiContainer;
import com.interactuamovil.core.extension.apiserver.ApiRequest;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.simpleframework.http.Path;
import org.simpleframework.http.parse.PathParser;

/**
 *
 * @author sergeiw
 */
public class Router<T> {
    
    private static final Logger logger = Logger.getLogger(Router.class);
        
    private HashMap<String, T> directRoutes = new HashMap<String, T>();

    private Route defaultRoute = null;
    private boolean modifyPathInfo = Boolean.TRUE;
    
    public Router() {
    }
    
    public void attach(String pathTemplate, T handler) {
        Path path = new PathParser(pathTemplate);
        
        Route<T> currentRoute = getDefaultRoute();
        String[] reqSegments = path.getSegments();
        for (int i = 0; i < reqSegments.length; i++) {
            Route<T> r = currentRoute.getOrCreateRoute(reqSegments[i]);            
            
            // if last
            if (i == reqSegments.length -1) {
                logger.trace("registering handler");
                r.setHandler(handler);
            }
            
            currentRoute = r;
        }                       

        if (!currentRoute.isWildcardRoute()) {
            directRoutes.put(path.getPath(), handler);
        }
    }
    
    public T resolveHandler(ApiRequest request, String path, String[] segments) {
        T handler = null;
        T lastHandler = null;
        StringBuilder matchedPath = new StringBuilder();
        StringBuilder pathInfo = new StringBuilder();
        
        logger.trace("getting path");
        // direct check
        logger.trace("check for direct route");
        if (directRoutes.containsKey(path)) {
            matchedPath.append(path);
            handler = directRoutes.get(path);        
            logger.trace("direct route match: " + path);
        } else {
            logger.trace("chained check");                        
            
            Route<T> currentRoute = getDefaultRoute();
            lastHandler = currentRoute.getHandler();
            
            int segCount;
            for (segCount = 0; segCount < segments.length; segCount++) {
                String segment = segments[segCount];                
                                                
                Route nextRoute = currentRoute.goNextRoute(segment);                
                if (nextRoute == null) {
                    logger.trace("using handler for " + matchedPath);
                    handler = currentRoute.getHandler();                    
                    break;
                } else {
                    currentRoute = nextRoute;
                    lastHandler = currentRoute.getHandler();
                    matchedPath.append("/").append(currentRoute.getPathSegment());
                    if (currentRoute.isWildcardRoute()) {
                        request.getAttributes().put(currentRoute.getSegmentName(), segment);
                    }                    
                }
                
            }
            
            if (isModifyPathInfo()) {
                // PathInfo
                for (int i = segCount; i < segments.length; i++) {
                    String segment = segments[i];
                    pathInfo.append("/").append(segment);
                }            
                request.setPathInfo(new PathParser(pathInfo.toString()));
                logger.trace("path info: " + request.getPathInfo());
            }
                        
        }
        
        if (handler == null) {
            if (lastHandler != null) {
                handler = lastHandler;
            }
        }
        
        if (handler != null) {
            logger.trace("using handler for " + matchedPath.toString());
            request.setMatchedPath(matchedPath.toString());
        } else {
            logger.trace("no match");
        }
        
        return handler;
    }

    /**
     * @return the defaultRoute
     */
    public Route<T> getDefaultRoute() {
        if (defaultRoute == null) {
            defaultRoute = new SimpleRoute("");
        }            
        return defaultRoute;
    }

    /**
     * @param defaultRoute the defaultRoute to set
     */
    public <T extends ApiContainer> void setDefaultRoute(Route<T> defaultRoute) {
        this.defaultRoute = defaultRoute;
    }

    /**
     * @return the modifyPathInfo
     */
    public boolean isModifyPathInfo() {
        return modifyPathInfo;
    }

    /**
     * @param modifyPathInfo the modifyPathInfo to set
     */
    public void setModifyPathInfo(boolean modifyPathInfo) {
        this.modifyPathInfo = modifyPathInfo;
    }
}
