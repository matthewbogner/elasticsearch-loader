package net.ibogner.elasticsearch.service;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.UUID;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.joda.time.Instant;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class LoadGenerator {

    private static final String indexExistsFormat = "%s - index '%s' already exists\n";
    private static final String indexCreatedFormat = "%s - index '%s' creation %s\n";
    private static final String docCreatedFormat = "%s - documents indexed: %d\n";

    private final Client esClient;
    private final SecureRandom secureRandom;

    public LoadGenerator(Client esClient) {
        this.esClient = esClient;
        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public void createIndices(int num, OutputStream output) {
        for (int i = 1; i <= num; ++i) {
            try {
                output.write(createIndex(i).getBytes(Charset.forName("UTF-8")));
                output.flush();
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
    }

    public void createDocuments(int numIndices, int numDocs, OutputStream output) {
        int indexCtr = 1;
        for (int docCtr = 0; docCtr < numDocs; ++docCtr) {
            String indexName = getIndexName(indexCtr++);
            try {
                esClient.prepareIndex(indexName, "sometype")
                      .setId(UUID.randomUUID().toString())
                      .setSource(createUniqueDocument())
                      .execute()
                      .get();

                if (docCtr % 50 == 0) {
                    output.write(String.format(docCreatedFormat, new Instant().toString(), docCtr).getBytes());
                    output.flush();
                }
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }

            if (indexCtr > numIndices) {
                indexCtr = 1;
            }
        }
    }

    private XContentBuilder createUniqueDocument() throws IOException {
        XContentBuilder b = jsonBuilder().startObject();

        b.field("id1", secureRandom.nextLong());
        b.field("id2", secureRandom.nextLong());
        b.field("id3",  secureRandom.nextLong());
        b.field("id4",     secureRandom.nextLong());
        b.field("date1",  new Instant().getMillis());
        b.field("date2", new Instant().getMillis());
        b.field("id5", secureRandom.nextLong());
        b.field("id6", secureRandom.nextLong());
        b.field("id7", secureRandom.nextLong());

        b.field("name1", generateString(1));
        b.field("summary1", generateString(50));

        b.field("idList1", generateList(5));
        b.field("idList2", generateList(5));

        b.field("bool1", false);

        b.field("name2", generateString(1));

        b.endObject();

        return b;
    }

    private String generateString(int numWords) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numWords; ++i) {
            sb.append(UUID.randomBase64UUID()).append(" ");
        }
        return sb.toString().trim();
    }

    private List<Long> generateList(int num) {
        List<Long> mylist = Lists.newLinkedList();
        for (int ctr = 0; ctr < num; ++ctr) {
            mylist.add(secureRandom.nextLong());
        }
        return mylist;
    }

    private String createIndex(int indexNum) throws ExecutionException, InterruptedException {
        String indexName = getIndexName(indexNum);
        if (indexExists(indexName)) {
            return String.format(indexExistsFormat, new Instant().toString(), indexName);
        } else {
            boolean ret = esClient.admin().indices().prepareCreate(indexName).execute().actionGet().acknowledged();
            return String.format(indexCreatedFormat, new Instant().toString(), indexName, (ret ? "SUCCEEDED":"FAILED"));
        }
    }

    private boolean indexExists(String indexName) throws ExecutionException, InterruptedException {
        return esClient.admin().indices().prepareExists(indexName).execute().get().exists();
    }

    private String getIndexName(int indexNum) {
        return String.format("exampleidx-%03d", indexNum);
    }


    public void deleteAllIndices() throws ExecutionException, InterruptedException {
        esClient.admin().indices().prepareDelete(new String[]{}).execute().get();
    }
}
