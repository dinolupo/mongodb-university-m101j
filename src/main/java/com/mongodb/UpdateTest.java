package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.Helper.printJson;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

/**
 * Created by dino on 22/10/15.
 */
public class UpdateTest {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
        MongoClient client = new MongoClient(new ServerAddress(), options);
        MongoDatabase db = client.getDatabase("test").withReadPreference(ReadPreference.secondary());
        MongoCollection<Document> coll = db.getCollection("updateTest", Document.class);

        coll.drop();

        for (int i = 0; i < 8; i++) {
            coll.insertOne(new Document()
                    .append("_id", i)
                    .append("x", i));
        }

        // basic update
//        coll.replaceOne(eq("x", 5), new Document("_id", 5).append("x", 20).append("update", true));

        // better method with the updateOne and the $set command
//        coll.updateOne(eq("x", 5), new Document("$set", new Document("x", 20)));

        // using upsert with true will create new document if does not exists
//        coll.updateOne(eq("_id", 9), new Document("$set", new Document("x", 20)),
//                        new UpdateOptions().upsert(true));

         // update many - increment all x with _id >= 5
        coll.updateMany(gte("_id", 5), new Document("$inc", new Document("x", 1)));

        // print results
        List<Document> all = coll.find().into(new ArrayList<Document>());
        for (Document document : all) {
            printJson(document);
        }
    }
}
