/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.simpleframework.http.Query;

/**
 *
 * @author sergeiw
 */
public class RequestParametersHelper {
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static Date getDate(Query query, String paramName) throws InvalidParameterException {
        return getDate(query, paramName, dateFormat);
    }
    
    public static Date getDate(Query query, String paramName, String format) throws InvalidParameterException {
        return getDate(query, paramName, new SimpleDateFormat(format));
    }
        
    public static Date getDate(Query query, String paramName, SimpleDateFormat format) throws InvalidParameterException {
        String param = null;        
        
        try {
            param = query.get(paramName);
            if (param != null) {
                Date date = dateFormat.parse(param);            
                return date;
            }
            return null;
        } catch (ParseException ex) {
            throw new InvalidParameterException(paramName, "Invalid date format. Expected yyyy-MM-dd HH:mm:ss, Received: " + param, ex);
        }
    }
    
    
    public static Integer getInteger(Query query, String paramName) throws InvalidParameterException {
        return getInteger(query, paramName, Boolean.FALSE, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    public static Integer getInteger(Query query, String paramName, boolean allowNull) throws InvalidParameterException {
        return getInteger(query, paramName, allowNull, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    public static Integer getInteger(Query query, String paramName, boolean allowNull, int minValue) throws InvalidParameterException {
        return getInteger(query, paramName, allowNull, minValue, Integer.MAX_VALUE);
    }
    
    public static Integer getInteger(Query query, String paramName, boolean allowNull, int minValue, int maxValue) throws InvalidParameterException {
        String param = null;
        try {
            param = query.get(paramName);
            if (param == null && allowNull) {
                return null;
            }
            
            int val = Integer.parseInt(param);            
            if (val >= minValue && val <= maxValue) {
                return val;
            } else {
                throw new InvalidParameterException(paramName, String.format("Start index must be in range [%d:%d]", minValue, maxValue));
            }
        } catch (NumberFormatException ex) {
            throw new InvalidParameterException(paramName, "Not a valid number: " + param, ex);
        }
    }
    
    public static String getString(Query query, String paramName) throws InvalidParameterException {
        return getString(query, paramName, Boolean.TRUE);
    }
    
    public static String getString(Query query, String paramName, boolean allowEmpty) throws InvalidParameterException {
        String param = null;
        param = query.get(paramName);
        if (!allowEmpty && StringUtils.isEmpty(param)) {
            throw new InvalidParameterException(paramName, "Cannot be null or blank");
        } else {
            return param;
        }
    }
    
    public static Boolean getBoolean(Query query, String paramName, boolean defaultTo) throws InvalidParameterException {        
        String param = query.get(paramName);
        if (param == null) {
            return defaultTo;
        } 

        if ("true".equalsIgnoreCase(param) || "false".equalsIgnoreCase(param)
                || "1".equalsIgnoreCase(param) || "0".equalsIgnoreCase(param)) {
            if ("1".equalsIgnoreCase(param))
                return Boolean.TRUE;
            if ("0".equalsIgnoreCase(param))
                return Boolean.FALSE;
            return Boolean.parseBoolean(param);
        } 

        throw new InvalidParameterException(paramName, "Invalid value for boolean");        
    }
    
    
}