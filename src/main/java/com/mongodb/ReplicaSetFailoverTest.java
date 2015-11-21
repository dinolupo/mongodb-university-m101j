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
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.Arrays;

public class ReplicaSetFailoverTest {
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        MongoClient client = new MongoClient(Arrays.asList(
                new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)));

        MongoCollection<Document> test = client.getDatabase("course").getCollection("replica.test");
        test.drop();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            for (int retries = 0; retries <= 2; retries++) {
                try {
                    test.insertOne(new Document("_id", i));
                    System.out.println("Inserted document: " + i);
                    break;
                } catch (DuplicateKeyException e) {
                    System.out.println("Document already inserted: " + i);
                } catch (MongoException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Retrying");
                    Thread.sleep(5000);
                }
            }
            Thread.sleep(500);
        }
    }
}
