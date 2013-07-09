package net.ibogner.elasticsearch.resources;

import com.google.common.collect.ImmutableMap;
import net.ibogner.elasticsearch.service.LoadGenerator;
import org.elasticsearch.client.Client;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Path("/load")
@Produces(MediaType.APPLICATION_JSON)
public class LoadResource {

    private final LoadGenerator loader;

    public LoadResource(Client esClient) {
        loader = new LoadGenerator(esClient);
    }

    @GET
    @Path("indices")
    public StreamingOutput createIndices(@QueryParam("numIndices") final int numIndices) {
        return new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                loader.createIndices(numIndices, output);
            }
        };
    }

    @DELETE
    @Path("indices")
    public Map<String, Object> deleteAllIndices() throws ExecutionException, InterruptedException {
        loader.deleteAllIndices();
        return ImmutableMap.<String, Object>of("success", true);
    }

    @GET
    @Path("documents")
    public StreamingOutput load(@QueryParam("numIndices") final int numIndices, @QueryParam("numDocs") final int numDocs) {
        return new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                loader.createDocuments(numIndices, numDocs, output);
            }
        };
    }


}
