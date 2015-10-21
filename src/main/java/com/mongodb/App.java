package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.Document;

import static java.util.Arrays.asList;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
        MongoClient client = new MongoClient(new ServerAddress(), options);
        MongoDatabase db = client.getDatabase("test").withReadPreference(ReadPreference.secondary());
        MongoCollection<Document> coll = db.getCollection("user", Document.class);

        coll.drop();

        Document smith = new Document("name", "Smith")
                .append("age", 20)
                .append("profession", "programmer");

        Document jason = new Document("name", "Jason")
                .append("age", 25)
                .append("profession", "hacker");

        System.out.println(smith);
        coll.insertOne(smith);
        //coll.insertMany(asList(smith, jason));
        System.out.println(smith);

        smith.remove("_id");
        System.out.println(smith);
        coll.insertOne(smith);
        System.out.println(smith);

    }
}
