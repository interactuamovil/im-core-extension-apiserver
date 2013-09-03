/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

import com.interactuamovil.core.extension.apiserver.route.Router;
import java.io.PrintStream;
import java.net.SocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.core.Container;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;

/**
 *
 * @author sergeiw
 */
public class AsynchronousService implements Container {

    private static final Logger logger = Logger.getLogger("AsyncServer");
    
    
    private Executor executor;
    private Router<ApiContainer> router;
    private Connection connection;
    private Server server;
    private SocketAddress address;
    
    
    public AsynchronousService(int size, Router<ApiContainer> router) {        
        this.executor = Executors.newFixedThreadPool(size);
        this.router = router;
    }
    
    /**
     * @return the router
     */
    public Router<ApiContainer> getRouter() {
        return router;
    }

    /**
     * @param router the router to set
     */
    public void setRouter(Router<ApiContainer> router) {
        this.router = router;
    }
    
    public void handle(Request request, Response response) {
        try {
            Task task = new Task(new ApiRequest(request), new ApiResponse(response));

            executor.execute(task);
        } catch (Exception e) {
            response.setCode(Status.INTERNAL_SERVER_ERROR.getCode());
            response.setDescription(Status.INTERNAL_SERVER_ERROR.getDescription());

            logger.error(String.format("%s - %s %s => %d %s %s",
                    this.getRealClientAddress(request),
                    request.getMethod(), request.getPath().getPath(), 
                    response.getCode(), response.getDescription(),
                    e.getMessage()));
            if (logger.isTraceEnabled()) {
                logger.trace(response.toString(), e);
            }
        }
           
    }
    
    public String getRealClientAddress(Request request) {        
            String forwarded = request.getValue("X-Forwarded-For");
            if (forwarded != null)
                return forwarded;
            return request.getClientAddress().getHostName();
        }

    public class Task implements Runnable {
  
        private final ApiResponse response;
        private final ApiRequest request;

        public Task(ApiRequest request, ApiResponse response) {
            this.response = response;
            this.request = request;
        }

        public void run() {
            try {

                logger.trace(String.format("Processing request: %s %s",
                        request.getMethod(), request.getPath().getPath())); 
                logger.trace(String.format("Headers: %s",
                        request.getHeader())); 


                PrintStream body = response.getPrintStream();
                Path path = request.getPath();
                ApiContainer requestHandler = getRouter().resolveHandler(request, path.getPath(), path.getSegments());
                response.setValue("Server", "ContactoSMS API/3.0 (Simple 5.1.4)");

                if (requestHandler == null) {
                    response.setValue("Content-Type", "text/plain");
                    response.setCode(Status.NOT_FOUND.getCode());
                    response.setDescription(Status.NOT_FOUND.getDescription());
                    //body.println("ContactoSMS API: Not Found");
                    body.close();
                } else {
                    requestHandler.handle(request, response);
                }            
                logger.info(String.format("%s - %s %s => %d %s [%d millis, %s]",
                        getRealClientAddress(request),
                        request.getMethod(), request.getPath().getPath(), 
                        response.getCode(), response.getDescription(), 
                        response.getResponseTime() - request.getRequestTime(),
                        requestHandler != null ? requestHandler.getHandlerName() : "Not Found"));            
            } catch(Exception e) {
                response.setCode(Status.INTERNAL_SERVER_ERROR.getCode());
                response.setDescription(Status.INTERNAL_SERVER_ERROR.getDescription());

                logger.error(String.format("%s - %s %s => %d %s %s",
                        getRealClientAddress(request),
                        request.getMethod(), request.getPath().getPath(), 
                        response.getCode(), response.getDescription(),
                        e.getMessage()));
                if (logger.isTraceEnabled()) {
                    logger.trace(response.toString(), e);
                }
            }
        }
      
        /*public String getRealClientAddress(Request request) {        
            String forwarded = request.getValue("X-Forwarded-For");
            if (forwarded != null)
                return forwarded;
            return request.getClientAddress().getHostName();
        }*/
   }      

   

   /*
   public static void main(String[] list) throws Exception {
      org.apache.log4j.PropertyConfigurator.configureAndWatch("./app.log4j.api.properties");
      
      ApiSettings.init(new PropertiesConfiguration("app.properties"));      
      logger.info(String.format("STARTING server on port %d. Active threads: %d  ********************************", 
              ApiSettings.getServicePort(), ApiSettings.getActiveThreads()));
      
      Container container = new AsynchronousService(ApiSettings.getActiveThreads());
      Server server = new ContainerServer(container);
      final Connection connection = new SocketConnection(server);
      SocketAddress address = new InetSocketAddress(ApiSettings.getServicePort());

      Runnable shutdownHook = new Runnable() {
            public void run() {
                logger.info("STOPPING Server");
                try {
                    connection.close();
                } catch (IOException ex) {
                    logger.error("Error stopping server", ex);
                }
                logger.info("Server STOP ********************************"
                        + "\n****************************************************************");
            }
        };
      Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook));
      
      DatabaseHelper.init(new PropertiesConfiguration("database.properties"));
      
      connection.connect(address);
      logger.info("STARTED  ********************************");
      
   }
   * 
   */
}
