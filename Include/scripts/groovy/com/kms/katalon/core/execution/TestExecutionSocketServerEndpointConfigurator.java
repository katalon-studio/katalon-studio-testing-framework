package com.kms.katalon.core.execution;

import javax.websocket.server.ServerEndpointConfig.Configurator;

/**
 * This configurator is used during the initialization of a {@link TestExecutionSocketServerEndpoint}
 * to provide a singleton for all incoming websocket requests.
 * 
 * @author thanhto
 *
 */
public class TestExecutionSocketServerEndpointConfigurator extends Configurator {
    private static TestExecutionSocketServerEndpoint endpoint = new TestExecutionSocketServerEndpoint();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
        return (T) endpoint;
    }
}
