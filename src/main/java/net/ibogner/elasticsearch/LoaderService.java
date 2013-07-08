package net.ibogner.elasticsearch;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import net.ibogner.elasticsearch.resources.LoadResource;
import org.elasticsearch.client.Client;

public class LoaderService extends Service<LoaderConfig> {

    public static void main(String[] args) throws Exception {
        new LoaderService().run(args);
    }

    @Override
    public void initialize(Bootstrap<LoaderConfig> bootstrap) {
    }

    @Override
    public void run(LoaderConfig configuration, Environment environment) throws Exception {
        Injector injector = Guice.createInjector(new LoaderGuiceModule(configuration));

        environment.addResource(new LoadResource(injector.getInstance(Client.class)));
        environment.manage(new LoaderManager(injector.getInstance(Client.class)));
    }
}
