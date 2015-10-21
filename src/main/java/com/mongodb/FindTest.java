package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

/**
 * Created by dino on 21/10/15.
 */
public class FindTest {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
        MongoClient client = new MongoClient(new ServerAddress(), options);
        MongoDatabase db = client.getDatabase("test").withReadPreference(ReadPreference.secondary());
        MongoCollection<Document> coll = db.getCollection("findTest", Document.class);

        coll.drop();

        for(int i=0; i<10; i++) {
            coll.insertOne(new Document("x", i));
        }

        System.out.println("Find one:");
        Document first = coll.find().first();
        printJson(first);

        System.out.println("Find all  with into: ");
        ArrayList<Document> all = coll.find().into(new ArrayList<Document>());
        for (Document document : all) {
            printJson(document);
        }


        System.out.println("Find all with iteration: ");
        MongoCursor<Document> cursor = coll.find().iterator();
        try {
            while(cursor.hasNext()) {
                Document cur = cursor.next();
                printJson(cur);
            }
        } finally {
            cursor.close();
        }

        System.out.println("Count:");
        long count = coll.count();
        System.out.println(count);

    }

    private static void printJson(Document document) {
        System.out.println(document.toJson());
    }
}
