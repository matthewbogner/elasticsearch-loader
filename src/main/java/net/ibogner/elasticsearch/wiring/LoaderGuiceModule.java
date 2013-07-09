package net.ibogner.elasticsearch.wiring;

import com.google.inject.AbstractModule;
import net.ibogner.elasticsearch.config.LoaderConfig;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class LoaderGuiceModule extends AbstractModule {

    private final LoaderConfig loaderConfig;

    public LoaderGuiceModule(LoaderConfig loaderConfig) {
        this.loaderConfig = loaderConfig;
    }

    @Override
    protected void configure() {
        Client client = new TransportClient(ImmutableSettings.settingsBuilder()
                                                             .put("cluster.name", loaderConfig.getClusterName()).build())
                .addTransportAddress(new InetSocketTransportAddress(loaderConfig.getNode1IP(), loaderConfig.getNode1Port()))
                .addTransportAddress(new InetSocketTransportAddress(loaderConfig.getNode2IP(), loaderConfig.getNode2Port()));

        bind(Client.class).toInstance(client);
    }
}
