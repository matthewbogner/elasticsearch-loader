package net.ibogner.elasticsearch.managers;

import com.yammer.dropwizard.lifecycle.Managed;
import org.elasticsearch.client.Client;

public class LoaderManager implements Managed {

    private Client client;

    public LoaderManager(Client client) {
        this.client = client;
    }

    @Override
    public void start() throws Exception {
//        client.prepareIndex()
//                .setIndex("indexcreationtest1")
//                .setType("sometype")
//                .setId(UUID.randomUUID().toString())
//                .setSource("{\"example\":true}")
//                .execute()
//                .get();

    }

    @Override
    public void stop() throws Exception {
        client.close();
    }
}
