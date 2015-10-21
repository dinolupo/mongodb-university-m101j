## MongoDB University Courseware  

### Create base Java project with Maven Archetype
```mvn archetype:generate```

### Execute Java project with Maven Exec
```mvn clean compile exec:java -Dexec.mainClass=com.mongodb.App```

### Write a query that retrieves documents from a users collection where the name has a "q" in it, and the document has an email field.
```javascript
db.users.find({"name":{$regex:"q"}, email: {$exists:true}});
```
-----

### Finds documents with a score between 50 and 60, inclusive
```javascript
db.scores.find({ score : { $gte : 50 , $lte : 60 } } );
```
-----
### How would you find all documents in the scores collection where the score is less than 50 or greater than 90?
```javascript
db.scores.find({$or:[{score:{$lt:50}},{score:{$gt:90}}]});
```
-----
### What will the following query do?
```javascript
db.scores.find( { score : { $gt : 50 }, score : { $lt : 60 } } );
```
#### Result:Find all documents with score less than 60.
Because the parser substitutes the first json object with the second one.
The solution to find value between is to merge condition like this 
```db.scores.find({ score : { $gte : 50 , $lte : 60 } } );```

or use the ```$and``` operator

-----
### QUERYING INSIDE ARRAYS - Which of the following documents would be returned by this query?
```javascript
db.products.find( { tags : "shiny" } );
```
#### Result:
```javascript
{ _id : 42 , name : "Whizzy Wiz-o-matic", tags : [ "awesome", "shiny" , "green" ] }
{ _id : 1040 , name : "Snappy Snap-o-lux", tags : "shiny" }
```
-----
### USING $IN AND $ALL - Which of the following documents matches this query?
```javascript
db.users.find( { friends : { $all : [ "Joe" , "Bob" ] }, favorites : { $in : [ "running" , "pickles" ] } } )
```
#### Result:
```javascript
{ name : "Cliff" , friends : [ "Pete" , "Joe" , "Tom" , "Bob" ] , favorites : [ "pickles", "cycling" ] }
```
-----
### QUERIES WITH DOT NOTATION - Suppose a simple e-commerce product catalog called catalog with documents that look like this:
```javascript
{ product : "Super Duper-o-phonic", 
price : 100000000000,
reviews : [ { user : "fred", comment : "Great!" , rating : 5 },
{ user : "tom" , comment : "I agree with Fred, somewhat!" , rating : 4 } ],
... }
```
Write a query that finds all products that cost more than 10,000 and that have a rating of 5 or better.
#### Result:
```javascript
db.catalog.find( { "price": { $gt: 10000 }, "reviews.rating": {$gte: 5} });
```
-----
### USING THE $SET COMMAND - For the users collection, the documents are of the form
```javascript
{
"_id" : "myrnarackham",
"phone" : "301-512-7434",
"country" : "US"
}
```
Please set myrnarackham's country code to "RU" but leave the rest of the document (and the rest of the collection) unchanged. 

#### Result:
```javascript
db.users.update({"_id":"myrnarackham"},{$set:{"country":"RU"}})
```
-----
### USING THE $UNSET COMMAND - Write an update query that will remove the "interests" field in the following document in the users collection.
```javascript
{
"_id" : "myrnarackham",
"phone" : "301-512-7434",
"country" : "US"
}
```
Do not simply empty the array. Remove the key : value pair from the document. 

#### Result:
```javascript
db.users.update({"_id":"jimmy"},{$unset:{interests:1}})
```
-----
### USING $PUSH, $POP, $PULL, $PULLALL, $ADDTOSET - Suppose you have the following document in your friends collection:
```javascript
{ _id : "Mike", interests : [ "chess", "botany" ] }
```

What will the result of the following updates be?

```javascript
db.friends.update( { _id : "Mike" }, { $push : { interests : "skydiving" } } );
db.friends.update( { _id : "Mike" }, { $pop : { interests : -1 } } );
db.friends.update( { _id : "Mike" }, { $addToSet : { interests : "skydiving" } } );
db.friends.update( { _id : "Mike" }, { $pushAll: { interests : [ "skydiving" , "skiing" ] } } );
```

#### Result:
```javascript
{ _id : "Mike", interests : ["botany", "skydiving", "skydiving", "skiing"] }
```
-----
### UPSERTS - After performing the following update on an empty collection
```javascript
db.foo.update( { username : 'bar' }, { '$set' : { 'interests': [ 'cat' , 'dog' ] } } , { upsert : true } );
```
What could be a document in the collection?

#### Result:
```javascript
{ "_id" : ObjectId("507b78232e8dfde94c149949"), "interests" : [ "cat", "dog" ], "username" : "bar" }
```
-----
### MULTI-UPDATE - Recall the schema of the scores collection:
```javascript
{
"_id" : ObjectId("50844162cb4cf4564b4694f8"),
"student" : 0,
"type" : "exam",
"score" : 75
}
```
Give every document with a score less than 70 an extra 20 points. 

#### Result:
```javascript
db.scores.update({score:{$lt:70}},{$inc:{score: 20}},{multi:true})
```
-----
### REMOVING DATA - Recall the schema of the scores collection:
```javascript
{
"_id" : ObjectId("50844162cb4cf4564b4694f8"),
"student" : 0,
"type" : "exam",
"score" : 75
}
```
Delete every document with a score of less than 60. 

#### Result:
```javascript
db.scores.remove({score:{$lt:60}})
```
-----
### JAVA DRIVER: REPRESENTING DOCUMENTS - How would you create a document using the Java driver with this JSON structure:
```javascript
{
"_id" : "user1",
"interests" : [ "basketball", "drumming"]
}
```
#### Result:
```java
new Document("_id", "user1").append("interests", Arrays.asList("basketball", "drumming"));
```
-----
### QUIZ: JAVA DRIVER: FIND, FINDONE, AND COUNT - In the following code snippet:
```java
MongoClient client = new MongoClient();
MongoDatabase database = client.getDatabase("school");
MongoCollection@<Document> people = database.getCollection("people");
Document doc;
// xxxx
System.out.println(doc);
```
Please enter the simplest one line of Java code that would be needed in place of // xxxx to make it print one document from the people collection.

#### Result:
```java
doc = people.find().first();
```


-----
### template
```javascript
db.users.findOne()
```
#### Result:
```javascript
{"demo":1}
```