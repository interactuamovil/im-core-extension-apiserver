/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

import com.interactuamovil.core.extension.apiserver.route.Route;
import com.interactuamovil.core.extension.apiserver.route.Router;
import com.interactuamovil.core.extension.apiserver.route.SimpleRoute;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

/**
 *
 * @author sergeiw
 */
public class ApiServer
    implements Container {
    
    static Router<ApiContainer> router = new Router<ApiContainer>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("./app.log4j.api.properties");        
        
        DefaultContainer defaultContainer = new DefaultContainer();
        //router.attach("/http/send_to_contact", new HttpSendToContact());
        //router.attach("/http/send_to_groups", defaultContainer);
        //router.attach("/http/path2/path3", defaultContainer);
        //router.attach("/http/path2/path4", defaultContainer);
        //router.attach("/http/", defaultContainer);
        Route<DefaultContainer> defaultRoute = new SimpleRoute<DefaultContainer>("");
        defaultRoute.setHandler(defaultContainer);
        router.setDefaultRoute(defaultRoute);
        
        Container container = new ApiServer();
        Server server = new ContainerServer(container);
        Connection connection = new SocketConnection(server);
        SocketAddress address = new InetSocketAddress(8089);

        connection.connect(address);
    }
    
    public void handle(Request request, Response response) {
      try {
          
         
         
         PrintStream body = response.getPrintStream();
         long time = System.currentTimeMillis();
   
         //response.setValue("Content-Type", "text/plain");
         //response.setValue("Server", "HelloWorld/1.0 (Simple 4.0)");
         response.setDate("Date", time);
         response.setDate("Last-Modified", time);
   
         request.getContentLength();
         
         body.println("Hello World");
         for(String s: request.getPath().getSegments()) {
             body.println("segment: " + s);
         }
          for (String key : request.getQuery().keySet()) {
              body.println("query["+key+"]: " + request.getQuery().get(key));
          }
          for (Object key : request.getAttributes().keySet()) {
              body.println("atribute["+key+"]: " + request.getAttribute(key));
          }
          for (String n : request.getNames()) {
              body.println("names["+n+"]: " + request.getValue(n));
          }
          body.println("address: " + request.getAddress().toString());
          body.println("content: " + request.getContent());
          body.println("method: " + request.getMethod());
          body.println("target: " + request.getTarget());
          body.println("msisdn: " + request.getValue("msisdn"));
          body.println("client: " + request.getClientAddress().toString());
//          body.println("path: " + request.getPath().getPath(0));
//          body.println("path: " + request.getPath().getPath(1));
//          body.println("path: " + request.getPath().getPath(2));
          
          //HttpApiManager.getInstance().getAccountByApiKey(request.getQuery().get("api_key"));
          //body.println("")
           body.close();
           
          ApiRequest apiRequest = new ApiRequest(request);
          ApiResponse apiResponse = new ApiResponse(response);
          
          ApiContainer resolveHandler = router.resolveHandler(apiRequest,
                  apiRequest.getPath().getPath(),
                  apiRequest.getPath().getSegments());
          resolveHandler.handle(apiRequest, apiResponse);
          
          for (Object key : request.getAttributes().keySet()) {
              body.println("atribute["+key+"]: " + request.getAttribute(key));
          }
        
      } catch(Exception e) {
         e.printStackTrace();
      }
   } 

}
