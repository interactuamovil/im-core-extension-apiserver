/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver.route;

/**
 *
 * @author sergeiw
 */
public class RouteUtils {
    
    public static boolean isWildcard(String pathSegment) {
       return pathSegment != null && pathSegment.startsWith("{");
    }
    
    public static String extractSegmentName(String wildcardSegment) {
        return wildcardSegment.replace("{", "").replace("}", "").trim();
    }
}
