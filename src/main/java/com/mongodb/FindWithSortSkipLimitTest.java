package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.Helper.*;

/**
 * Created by dino on 22/10/15.
 */
public class FindWithSortSkipLimitTest {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
        MongoClient client = new MongoClient(new ServerAddress(), options);
        MongoDatabase db = client.getDatabase("test").withReadPreference(ReadPreference.secondary());
        MongoCollection<Document> coll = db.getCollection("findWithFilterAndProjection", Document.class);

        coll.drop();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++)
            coll.insertOne(new Document()
                    .append("i", i)
                    .append("j", j));
        }

        Bson projection = fields(include("i", "j"), excludeId() );

        // longest method
//        Bson sort = new Document("i", 1).append("j", -1);

        // shortes method using static builders
        Bson sort = orderBy(ascending("i"), descending("j"));

        // add projection to filter contents of the result array
        List<Document> all = coll.find()
                .projection(projection)
                .sort(sort)
                .skip(20)
                .limit(50)
                .into(new ArrayList<Document>());

        for (Document document : all) {
            printJson(document);
        }


    }
}
