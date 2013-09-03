/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.interactuamovil.core.extension.apiserver;

import com.interactuamovil.core.application.AbstractApplication;
import com.interactuamovil.core.application.ApplicationException;
import com.interactuamovil.core.extension.apiserver.route.Router;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.apache.log4j.Logger;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

/**
 *
 * @author sergeiw
 */
public class ApiServerApplication extends AbstractApplication {

    private static final Logger logger = Logger.getLogger(ApiServerApplication.class);
    
    private Container container;
    private Connection connection;
    private Server server;
    private SocketAddress address;
    
    private Router<ApiContainer> router;
    
    public ApiServerApplication() {        
    }

    @Override
    public void initialize() throws ApplicationException {
        super.initialize();
        
        initializeServer();
    }
    
    private void initializeServer() throws ApplicationException {
        try {            
            this.container = new AsynchronousService(ApiSettings.getActiveThreads(), getRouter());
            this.server = new ContainerServer(container);
            this.connection = new SocketConnection(server);
            this.address = new InetSocketAddress(ApiSettings.getServicePort());
                        
        } catch (IOException ex) {
            throw new ApplicationException("An error ocurred while configuring server", ex);
        }
    }
    
    
    @Override
    public void run() {
        try {
            logger.info(String.format("STARTING server on port %d. Active threads: %d  ********************************", 
              ApiSettings.getServicePort(), ApiSettings.getActiveThreads()));
            
            connection.connect(address);
            
        } catch (IOException ex) {
            logger.error("Unable to start server", ex);
        }
    }

    @Override
    public void stop() {
        logger.info("STOPPING Server");
        try {
            connection.close();
        } catch (IOException ex) {
            logger.error("Error stopping server", ex);
        }
        logger.info("Server STOP ********************************"
                + "\n****************************************************************");
    }

    /**
     * @return the router
     */
    public Router<ApiContainer> getRouter() {
        if (router == null) {
            router = new Router<ApiContainer>();
        }
        return router;
    }

    /**
     * @param router the router to set
     */
    public void setRouter(Router<ApiContainer> router) {
        this.router = router;
    }
    
}
