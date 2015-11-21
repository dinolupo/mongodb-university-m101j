/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.javadoc.Doc;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.Arrays;

public class WriteConcernTest {
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        MongoClient client = new MongoClient(Arrays.asList(
                new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)));

        client.setWriteConcern(WriteConcern.JOURNALED);

        MongoDatabase db = client.getDatabase("course");
        db.withWriteConcern(WriteConcern.JOURNALED);
        MongoCollection<Document> coll = db.getCollection("write.test");
        coll.withWriteConcern(WriteConcern.JOURNALED);

        coll.drop();

        Document doc = new Document("_id", 1);

        coll.insertOne(doc);

        try {
            coll.insertOne(doc);
        } catch (DuplicateKeyException e ) {
            System.out.println(e.getMessage());
        }

    }
}
