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
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.*;

/**
 * Created by dino on 22/10/15.
 */
public class Homework_2_3 {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
        MongoClient client = new MongoClient(new ServerAddress(), options);
        MongoDatabase db = client.getDatabase("students").withReadPreference(ReadPreference.secondary());
        MongoCollection<Document> coll = db.getCollection("grades", Document.class);

        // db.grades.find({"type": "homework" }).sort({"student_id": 1}, {"score": 1}).limit(100)

//        Bson projection = fields(include("student_id", "score"), excludeId());
        Bson filter = eq("type", "homework");
        Bson sort = orderBy(ascending("student_id"), ascending("score"));

        // add projection to filter contents of the result array
        List<Document> all = coll.find(filter)
                .sort(sort)
                .into(new ArrayList<Document>());

        int firstStudentId = -1;
        int deleted = 0;
        for (Document document : all) {
            if (firstStudentId != document.getInteger("student_id")) {
                firstStudentId = document.getInteger("student_id");
                System.out.print("deleting document --> ");
                deleted++;
                printJson(document);
                coll.deleteOne(document);
            } else {
                printJson(document);
            }
        }
        System.out.println("deleted: " + deleted);


    }
}
