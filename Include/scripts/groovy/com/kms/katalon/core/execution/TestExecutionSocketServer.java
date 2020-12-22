package com.kms.katalon.core.execution;

import javax.websocket.server.ServerEndpointConfig;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

public class TestExecutionSocketServer {
    private static TestExecutionSocketServer instance;

    private TestExecutionSocketServerEndpoint activeSocket;

    private Server server;

    private ServerConnector firefoxConnetor;

    private TestExecutionSocketServer() {
        // hide constructor
    }

    public static TestExecutionSocketServer getInstance() {
        if (instance == null) {
            instance = new TestExecutionSocketServer();
        }
        return instance;
    }

    public void start(Class<?> socketClass, int port) {
        if (isRunning()) {
            return;
        }
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        firefoxConnetor = new ServerConnector(server);
        firefoxConnetor.setPort(50001);
        server.addConnector(connector);
        server.addConnector(firefoxConnetor);
        try {
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
            wscontainer.addEndpoint(ServerEndpointConfig.Builder.create(socketClass, "/test_execution/")
                    .configurator(new TestExecutionSocketServerEndpointConfigurator())
                    .build());
            server.start();
        } catch (Exception e) {
            // Ignore it
        }
    }

    public boolean isRunning() {
        return server != null && server.isRunning();
    }

    public synchronized void addActiveSocket(TestExecutionSocketServerEndpoint socket) {
        if (socket == null) {
            return;
        }
        activeSocket = socket;
    }

    public synchronized void removeActiveSocket(TestExecutionSocketServerEndpoint socket) {
        if (socket == null) {
            return;
        }
        activeSocket = null;
    }

    public TestExecutionSocketServerEndpoint getEndpoint() {
        return activeSocket;
    }

    public synchronized void stop() {
        try {
            activeSocket.waitForMhtmlHandlingThread();
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
