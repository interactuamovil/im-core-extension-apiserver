/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.interactuamovil.core.transport.ISerializer;
import com.interactuamovil.core.transport.SerializerFactory;
import java.io.IOException;

/**
 *
 * @author sergeiw
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class JsonObject {
    
    private static final ISerializer serializer;
    
    static {
        serializer = SerializerFactory.getSerializer(SerializerFactory.Format.JSON);
    }
    
    public String toJson() throws IOException {
        return serializer.serialize(this);
    }

    @Override
    public String toString() {
        try {
            return toJson();
        } catch (IOException ex) {
            return super.toString();
        }
    }

    public static <T> T fromJson(String json, Class<T> cls) throws IOException {
        return serializer.deserialize(json, cls);        
    }   
    
    public static <T> T fromJson(String json, TypeReference<T> t) throws IOException {
        return serializer.deserialize(json, t);        
    }

}
