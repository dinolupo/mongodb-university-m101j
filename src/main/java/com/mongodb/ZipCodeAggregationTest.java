package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.Helper.printJson;
import static com.mongodb.client.model.Filters.*;
import static java.util.Arrays.asList;

/**
 * Created by dino on 14/11/15.
 */
public class ZipCodeAggregationTest {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
        MongoClient client = new MongoClient(new ServerAddress(), options);
        MongoDatabase db = client.getDatabase("agg").withReadPreference(ReadPreference.secondary());
        MongoCollection<Document> coll = db.getCollection("zips", Document.class);

        List<Document> pipeline;

        pipeline = asList(new Document("$group",
                                        new Document("_id","$state")
                                        .append("totalPopulation",
                                                new Document("$sum", "$pop"))),
                        new Document("$match",
                                        new Document("totalPopulation",
                                                new Document("$gt", 10*1000*1000)))

        );


        List<Document> all = coll.aggregate(pipeline).into(new ArrayList<Document>());

        for (Document document : all) {
            printJson(document);
        }

    }
}
