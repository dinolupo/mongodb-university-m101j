package com.mongodb.com.mongodb.homework;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.Helper.printJson;
import static com.mongodb.client.model.Filters.*;


/**
 * Created by dino on 22/10/15.
 */
public class Homework_3_1 {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
        MongoClient client = new MongoClient(new ServerAddress(), options);
        MongoDatabase db = client.getDatabase("school").withReadPreference(ReadPreference.secondary());
        MongoCollection<Document> coll = db.getCollection("students", Document.class);

        Bson filter = eq("scores.type", "homework");

        List<Document> all = coll.find(filter)
                .into(new ArrayList<Document>());

        ArrayList<Document> scores = null;
        for (Document document : all) {
            printJson(document);
            scores = document.get("scores", ArrayList.class);

            double lowest = Double.MAX_VALUE;
            Document lowestHomeworkScore = null;
            for (Document score : scores) {
                if (score.getString("type").matches("homework")) {
                    if (score.getDouble("score") < lowest) {
                        lowest = score.getDouble("score");
                        lowestHomeworkScore = score;
                    }
                }
            }

            scores.remove(lowestHomeworkScore);
            printJson(document);
            coll.replaceOne(new Document("_id", document.getInteger("_id")), document);


        }

    }
}
