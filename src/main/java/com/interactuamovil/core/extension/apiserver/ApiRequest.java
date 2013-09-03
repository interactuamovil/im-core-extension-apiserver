/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.simpleframework.http.Request;
import org.simpleframework.http.RequestWrapper;
import org.simpleframework.http.parse.PathParser;

/**
 *
 * @author sergeiw
 */
public class ApiRequest extends RequestWrapper {

    private static final Logger logger = Logger.getLogger(ApiRequest.class);
    
    private String resourceName = null;    
    private Integer apiLogId = null;
    
    private HashMap<String, String> attributes;
    private List<String> orderedAttributes;
    private String matchedPath = null;
    private PathParser pathInfo = null;    
    private String queryString = null;
    
    public ApiRequest(Request request) {
        super(request);
        
        try {
        String[] parts = request.getTarget().split("\\?");
        if (parts.length > 1) {
            try {
                queryString = URLDecoder.decode(parts[1], "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                queryString = parts[1];
            }
        }
        } catch (Throwable e) {
            logger.error("Unable to parse query string", e);
        }
        
    }    
    
    /**
     * @return the resourceName
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * @param resourceName the resourceName to set
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    
    /**
     * @return the apiLogId
     */
    public Integer getApiLogId() {
        return apiLogId;
    }

    /**
     * @param apiLogId the apiLogId to set
     */
    public void setApiLogId(Integer apiLogId) {
        this.apiLogId = apiLogId;
    }
    
    public String getRealClientAddress() {        
        String forwarded = this.getValue("X-Forwarded-For");
        if (forwarded != null)
            return forwarded;
        return this.getClientAddress().getHostName();
    }
    
    public HashMap<String, String> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<String, String>();
        }
        return attributes;
    }

    /**
     * @return the matchedPath
     */
    public String getMatchedPath() {
        return matchedPath;
    }

    /**
     * @param matchedPath the matchedPath to set
     */
    public void setMatchedPath(String matchedPath) {
        this.matchedPath = matchedPath;
    }

    /**
     * @return the pathInfo
     */
    public PathParser getPathInfo() {
        if (pathInfo == null) {
            pathInfo = new PathParser("/");
        }
        return pathInfo;
    }

    /**
     * @param pathInfo the pathInfo to set
     */
    public void setPathInfo(PathParser pathInfo) {
        this.pathInfo = pathInfo;
    }

    /**
     * @return the queryString
     */
    public String getQueryString() {
        return queryString;
    }
        
}

