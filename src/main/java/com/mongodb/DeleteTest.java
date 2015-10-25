package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.Helper.printJson;
import static com.mongodb.client.model.Filters.*;

/**
 * Created by dino on 22/10/15.
 */
public class DeleteTest {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
        MongoClient client = new MongoClient(new ServerAddress(), options);
        MongoDatabase db = client.getDatabase("test").withReadPreference(ReadPreference.secondary());
        MongoCollection<Document> coll = db.getCollection("deleteTest", Document.class);

        coll.drop();

        for (int i = 0; i < 8; i++) {
            coll.insertOne(new Document()
                    .append("_id", i)
                    .append("x", i));
        }

        // delete many
//        coll.deleteMany(gt("_id", 4));

        // delete many
        coll.deleteOne(eq("_id", 4));

         //  print results
        List<Document> all = coll.find().into(new ArrayList<Document>());
        for (Document document : all) {
            printJson(document);
        }
    }
}
