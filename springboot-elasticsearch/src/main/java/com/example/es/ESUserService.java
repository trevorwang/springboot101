package com.example.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ESUserService {
    public static final String USER_INDEX = "user";
    private ElasticsearchClient client;


    public boolean indexExisted() throws IOException {
        return client.indices().exists(builder -> () -> builder.index(USER_INDEX).build()).value();
    }

    public CreateIndexResponse createIndex() throws IOException {
        return client.indices().create(builder -> () -> builder.index(USER_INDEX).build());
    }

    public boolean deleteIndex() throws IOException {
        return client.indices().delete(builder -> () -> builder.index(USER_INDEX).build()).acknowledged();
    }


    public List<BulkResponseItem> bulkIndex(List<User> users) throws IOException {
        var bulkBuilder = new BulkRequest.Builder().index(USER_INDEX);
        users.forEach(u -> {
            bulkBuilder.operations(o -> o.index(i ->
                            i.index(USER_INDEX).id(UUID.randomUUID().toString())
                                    .document(u)
                    )
            );
        });
        var res = client.bulk(bulkBuilder.build());
        return res.items();
    }


    public List<User> searchUserByName(String text) throws IOException {
        var req = new SearchRequest.Builder()
                .index(USER_INDEX)
                .query(q -> q.match( m -> m.field("name").query(text)
                        )
                )
                .build();
        var res = client.search(req, User.class);

        log.debug("hits {} --> {}", res.hits().total(), res.hits().total().value());

        return res.hits().hits().stream().map(i -> i.source()).collect(Collectors.toList());
    }
}
