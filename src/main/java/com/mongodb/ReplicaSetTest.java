package com.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.Arrays;

public class ReplicaSetTest {

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        MongoClient client = new MongoClient(Arrays.asList(
                new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)));

        MongoCollection<Document> test = client.getDatabase("course").getCollection("replica.test");
        test.drop();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            test.insertOne(new Document("_id", i));
            System.out.println("Inserted document: " + i);
            Thread.sleep(500);
        }
    }
}
