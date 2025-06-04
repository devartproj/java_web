package step.learning.ws;

import com.google.inject.AbstractModule;

public class WebsocketModule extends AbstractModule {
    @Override
    protected void configure() {
        // інжекція у статичні поля
        requestStaticInjection( WebsocketConfigurator.class ) ;
    }
}
