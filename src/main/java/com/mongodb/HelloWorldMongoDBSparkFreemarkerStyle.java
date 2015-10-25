package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.bson.Document;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.halt;

/**
 * Created by dino on 15/10/15.
 */
public class HelloWorldMongoDBSparkFreemarkerStyle {
    public static void main(String[] args) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setClassForTemplateLoading(HelloWorldFreemarkerStyle.class, "/freemarker");

        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(50).build();
        MongoClient client = new MongoClient(new ServerAddress(), options);
        MongoDatabase db = client.getDatabase("course").withReadPreference(ReadPreference.secondary());
        MongoCollection<Document> coll = db.getCollection("hello", Document.class);

        coll.drop();

        coll.insertOne(new Document("name", "MongoDB"));

        get("/", (req, res) -> {
                    StringWriter writer = new StringWriter();
                    try {
                        Template helloTemplate = configuration.getTemplate("simpleTemplate.ftl");

                        Document document = coll.find().first();

                        helloTemplate.process(document, writer);

                    } catch (Exception e) {
                        halt(500);
                        e.printStackTrace();
                    }
                    return writer;
                }
        );
    }
}
