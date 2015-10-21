package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by dino on 21/10/15.
 */
public class FindWithFilterTest {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
        MongoClient client = new MongoClient(new ServerAddress(), options);
        MongoDatabase db = client.getDatabase("test").withReadPreference(ReadPreference.secondary());
        MongoCollection<Document> coll = db.getCollection("findWithFilterTest", Document.class);

        coll.drop();

        for(int i=0; i<10; i++) {
            coll.insertOne(new Document()
                            .append("x", new Random().nextInt(2))
                            .append("y", new Random().nextInt(100)));
        }

        // longer method to create a filter
//        Bson filter = new Document("x", 0)
//                .append("y", new Document("$gt", 10).append("$lt", 90));

        // best concise method to create a filter
        Bson filter = and(eq("x", "0"), gt("y", 10), lt("y", 90));

        List<Document> all = coll.find(filter).into(new ArrayList<Document>());
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
