/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver.route;

import com.interactuamovil.core.extension.apiserver.ApiContainer;
import com.interactuamovil.core.extension.apiserver.ApiRequest;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author sergeiw
 */
public abstract class Route<T> {
    
    private static final Logger logger = Logger.getLogger(Route.class);
    
    private String pathSegment = null;
    private String segmentName = null;
    private boolean wildcardSegment = Boolean.FALSE;
    
    private T handler = null;
    private HashMap<String, Route<T>> routes = null;
    private Route<T> wildcardRoute = null;

    public Route(String pathSegment) {
        this.pathSegment = pathSegment;
        wildcardSegment = RouteUtils.isWildcard(pathSegment);
        if (wildcardSegment) {
            this.segmentName = RouteUtils.extractSegmentName(pathSegment);
        }        
        
    }
    
    protected abstract Route<T> createRoute(String pathSegment);
    
    public boolean hasWildcardRoute() {
        return this.getWildcardRoute() != null;
    }    

    /**
     * @return the handler
     */
    public T getHandler() {
        return handler;
    }

    /**
     * @param handler the handler to set
     */
    public void setHandler(T handler) {
        this.handler = handler;
    }

    /**
     * @return the wildcardRoute
     */
    public Route<T> getWildcardRoute() {
        return wildcardRoute;
    }

    /**
     * @return the pathSegment
     */
    public String getPathSegment() {
        return pathSegment;
    }

    /**
     * @return the segmentName
     */
    public String getSegmentName() {
        return segmentName;
    }
    
    /**
     * @return the wildcardSegment
     */
    public boolean isWildcardRoute() {
        return wildcardSegment;
    }
    
    /**
     * @return the routes
     */
    public HashMap<String, Route<T>> getRoutes() {
        if (routes == null) {
            routes = new HashMap<String, Route<T>>();
        }
        return routes;
    }
    
    
    
    public Route getOrCreateRoute(String pathSegment) {
        logger.trace("getOrCreateRoute: " + pathSegment);
        Route<T> r = getRouteForSegment(pathSegment);
        if (r == null) {
            logger.trace("creating route");
            r = createRoute(pathSegment);
            if (r.isWildcardRoute()) {
                wildcardRoute = r;
            } else {
                getRoutes().put(pathSegment, r);
            }           
        }
        return r;
    }
    
    public Route goNextRoute(String pathSegment) {
        logger.trace("go next route: " + pathSegment);
        Route<T> r = getRouteForSegment(pathSegment);        
        return r;
    }

    
    
    public Route<T> getRouteForSegment(String pathSegment) {
        Route<T> r = getRoutes().get(pathSegment);
        if (r != null) {
            logger.trace("route exits for: " + pathSegment);
        } else {
            
            if (hasWildcardRoute()) {
                r = getWildcardRoute();
                logger.trace("wildcard route exits for: " + r.getSegmentName());
            }
        }
        
        if (r == null) {
            logger.trace("no route for: " + pathSegment);
        }
        return r;
    }

    
        
    
    


    
    
    
    
    
    
}
