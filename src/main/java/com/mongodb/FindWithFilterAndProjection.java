package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

/**
 * Created by dino on 22/10/15.
 */
public class FindWithFilterAndProjection {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
        MongoClient client = new MongoClient(new ServerAddress(), options);
        MongoDatabase db = client.getDatabase("test").withReadPreference(ReadPreference.secondary());
        MongoCollection<Document> coll = db.getCollection("findWithFilterAndProjection", Document.class);

        coll.drop();

        for (int i = 0; i < 10; i++) {
            coll.insertOne(new Document()
                    .append("x", new Random().nextInt(2))
                    .append("y", new Random().nextInt(100))
                    .append("i", i));
        }

        Bson filter = and(eq("x", 0),  gt("y", 10), lt("y", 90));

        // longest method
        //Bson projection = new Document("x", 0).append("_id", 0);
        // or
        //Bson projection = new Document("y", 1).append("i", 1).append("_id", 0);

        // shortest method
        Bson projection = fields(include("y", "i"), excludeId() );

        // add projection to filter contents of the result array
        List<Document> all = coll.find(filter)
                .projection(projection)
                .into(new ArrayList<Document>());
        for (Document document : all) {
            printJson(document);
        }

        long count = coll.count(filter);
        System.out.println("Count:" + count);
    }

    private static void printJson(Document document) {
        System.out.println(document.toJson());
    }
}
