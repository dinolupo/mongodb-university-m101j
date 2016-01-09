package com.mongodb.com.mongodb.finalexam;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

/**
 * Created by dino on 22/10/15.
 */
public class Final7 {
    public static void main(String[] args) {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
        MongoClient client = new MongoClient(new ServerAddress(), options);
        MongoDatabase db = client.getDatabase("final7").withReadPreference(ReadPreference.secondary());

        MongoCollection<Document> albums = db.getCollection("albums", Document.class);
        MongoCollection<Document> images = db.getCollection("images", Document.class);

        Bson projection = fields(include("_id"));

        // put all image ids into a Set
        Set<Integer> imagesIds = new HashSet<Integer>();
        images.find().projection(projection).forEach((Block<Document>) document -> {
            imagesIds.add(document.getInteger("_id"));
        });
        System.out.println("before:" + imagesIds.size());

        // remove from the Set the image id associated with an album
        albums.find().forEach((Block<Document>) document -> {
            ArrayList<Integer> idList = document.get("images", ArrayList.class);
            idList.forEach(id -> imagesIds.remove(id));
        });
        System.out.println("after:" + imagesIds.size());

        // remove unused the images
        Bson filter = in("_id", imagesIds);
        images.deleteMany(filter);


    }

}


