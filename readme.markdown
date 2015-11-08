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


### QUIZ: JAVA DRIVER: QUERYING WITH A FILTER - Given a collection named "scores" of documents with two fields -- type and score -- what is the correct line of code to find all documents where type is "quiz" and score is greater than 20 and less than 90. Select all that apply.
#### Result:
```java
// longer using document query
scores.find(new Document("type", "quiz").append("score", new Document("$gt", 20).append("$lt", 90)));
// concise method using Filters static methods
scores.find(Filters.and(Filters.eq("type", "quiz"), Filters.gt("score", 20), Filters.lt("score", 90)));
```


### QUIZ: JAVA DRIVER: QUERYING WITH A PROJECTION - Given a variable named "students" of type MongoCollection<Document>, which of the following lines of code could be used to find all documents in the collection, retrieving only the "phoneNumber" field.
#### Result:
```java
// longer using document projections
students.find().projection(new Document("phoneNumber", 1).append("_id", 0));
// concise method using Projections static methods
students.find().projection(Projections.fields(Projections.include("phoneNumber"), Projections.excludeId());
```


### QUIZ: JAVA DRIVER: QUERYING WITH SORT, SKIP AND LIMIT - Supposed you had the following documents in a collection named things.
```javascript
{ "_id" : 0, "value" : 10 }
{ "_id" : 1, "value" : 5 }
{ "_id" : 2, "value" : 7 }
{ "_id" : 3, "value" : 20 }
```
If you performed the following query in the Java driver:
```java
collection.find().sort(new Document("value", -1)).skip(2).limit(1)
```

which document would be returned?
#### Result:
The document with _id=2


### QUIZ: JAVA DRIVER: UPDATE AND REPLACE - In the following code fragment, what is the Java expression in place of xxxx that will set the field "examiner" to the value "Jones" for the document with _id of 1. Please use the dollar-sign set operator.

```java
// update using $set
scores.updateOne(new Document("_id", 1), xxxx);
```
#### Result:
```java
new Document("$set", new Document("examiner", "Jones"))
```

### QUIZ: JAVA DRIVER: DELETE - Given a collection with the following documents, how many will be affected by the below update statement?
```java
{ _id: 0, x: 1 }
{ _id: 1, x: 1 }
{ _id: 2, x: 1 }
{ _id: 3, x: 2 }
{ _id: 4, x: 2 }

collection.deleteOne(Filters.eq("x", 1))
```
#### Result: 
1


## Indexes

### create indexes from shell (1 is ascending, -1 is descending)
db.students.createIndex( {student_id: 1, class_id: -1} );

### explain queries explain() or explain(true) to execute also the query
db.students.explain().find({student_id: 5});

### show indexes
db.students.getIndexes()

### drop indexes
db.students.dropIndex({student_id: 1})

### multi key indexes for arrays
only one element of the index can be an array
read the isMultiKey value of explain to see if it is multi key
index are automatically upgraded to multi key index once you insert a record of that type


### QUIZ: MULTIKEY INDEXES: Suppose we have a collection foo that has an index created as follows:
```javascript
db.foo.createIndex( { a:1, b:1 } )
```
#### Result:
```javascript
yes - db.foo.insert( { a : "grapes", b : "oranges" } )
NO  - db.foo.insert( { a : [ 1, 2, 3 ], b : [ 5, 6, 7 ] } )
yes - db.foo.insert( { a : ["apples", "oranges" ], b : "grapes" } )
yes - db.foo.insert( { a : "grapes", b : [ 8, 9, 10 ] } )
```

### dot notation and multikey

on document like this:
```javascript
{
"_id" : ObjectId("423423423432"),
"student_id" : 23232,
"scores" : [ 
			{
				"type": "exam",
				"score": 30.23232
			},
			{
				"type": "exam",
				"score": 354.323232
			},
			{
				"type": "exam",
				"score": 399.34677
			},
			{
				"type": "exam",
				"score": 127.949494
			}
		]
}
```

#### use:
db.students.createIndex({'scores.score': 1});

#### elemMatch find dcuments with array that contains at least one of that values
db.students.explain().find({'scores': {$elemMatch: {type:'exam', score: {$gt: 99.0}}}}

if execute with explain(true) you find the number of documents examined in the parameter:
```docsExamined```

### QUIZ: DOT NOTATION AND MULTIKEY: Suppose you have a collection called people in the database earth with documents of the following form:
```javascript
{
	"_id" : ObjectId("551458821b87e1799edbebc4"),
	"name" : "Eliot Horowitz",
	"work_history" : [
		{
			"company" : "DoubleClick",
			"position" : "Software Engineer"
		},
		{
			"company" : "ShopWiki",
			"position" : "Founder & CTO"
		},
		{
			"company" : "MongoDB",
			"position" : "Founder & CTO"
		}
	]
}
```
Type the command that you would issue in the Mongo shell to create an index on company, descending.

#### Result:
```javascript
db.people.createIndex({'work_history.company':-1});
```

### Index Creation Option, Unique
db.stuff.createIndex({thing: 1}, {unique: true})

_id is unique, but the db does not show it.


### QUIZ: INDEX CREATION OPTION, UNIQUE - Please provide the mongo shell command to create a unique index on student_id, class_id, ascending for the collection students.
#### Result:
```javascript
db.students.createIndex({student_id:1, class_id:1}, {unique:true})
```

### Index Creation, Sparse 
{ a: 1, b: 2, c: 5 }
{ a:10, b:5, c: 10 }
{ a: 13, b:17 }
{ a: 7, b: 23 }

you have to add {sparse: true} to create index on unique elements that can be null on more than one value, example on c in the example above

when using sort() cannot be use on spars index because some documents are missing from the index

### Index Creation, Background
_____________________________________________________________________
__foreground__								__background__
fast											slow
block writes and readers in db			don't block readred/writers

option is {background: true} to use it background

### Using Explain
how executing the query and what indexes are used

> mode 1
var exp = db.example.explain()
exp.help()

> mode 2
var cursor = db.example.find({a: 99});
cursor.explain();

### Explain Verbosity
- queryPlanner - DEFAULT
- executionStats
- allPlansExecution

var exp = db.example.explain("executionStats");
notes:
> nReturned
> executionTimeMillis
> totalKeys...

var exp = db.example.explain("allPlansExecution");
> run the query optimizer directly

### Covered Queries
__It means that documents inspected are 0 and retrieved only using indexes__

### QUIZ: COVERED QUERIES - You would like to perform a covered query on the example collection. You have the following indexes:
```javascript
{ name : 1, dob : 1 }
{ _id : 1 }
{ hair : 1, name : 1 }
```
Which of the following is likely to be a covered query? Check all that apply.
### Result:

```javascript
db.example.find( { name : { "$in" : [ "Bart", "Homer" ] } }, {_id : 0, dob : 1, name : 1} )
db.example.find( { _id : 1117008 }, { _id : 0, name : 1, dob : 1 } )
db.example.find( { name : { "$in" : [ "Bart", "Homer" ] } }, {_id : 0, hair : 1, name : 1} )
db.example.find( { name : { "$in" : ["Alfred", "Bruce" ] } }, { name : 1, hair : 1 } )"
```

### When it an index used
1 b,c
2 c,b
  d,e
  e,f
3 a,b,c

example query plan that runs simultaneusly:
1 - best results
3 - threshold (sorted)

__CACHE__ used for queries of that shape

it will rebuilded (after evicted from the cache):
> threshold writes (example after 1000 writes)
> rebuild the index
> +/- (index added or removed)
> restart the mongod process


### QUIZ: WHEN IS AN INDEX USED? - Given collection foo with the following index:
```javascript
db.foo.createIndex( { a : 1, b : 1, c : 1 } )
```
Which of the following queries will use the index?
#### Result:
```javascript
OK - db.foo.find( { c : 1 } ).sort( { a : 1, b : 1 } )
NO - db.foo.find({c:1}).sort({a:-1, b:1})
OK - db.foo.find( { a : 3 } )
NO - db.foo.find( { b : 3, c : 4 } )
```

### Geospatioal Index - 2D Model
- 'location':[x, y]
- ensureIndex({'location':'2d', type: 1});
- find({location: {$near:[x,y]}}).limit(20)


 
### QUIZ: GEOSPATIAL INDEXES 2d
Suppose you have a 2D geospatial index defined on the key location in the collection places. Write a query that will find the closest three places (the closest three documents) to the location 74, 140.
#### Result:
```javascript
db.places.find({location: {$near:[74,140]}}).limit(3)
```

### Gesospatial Spherical
- support for a subset of GeoJson

example: 
```javascript
> db.places.find().pretty()

{
	"_id" 	: ObjectId("2342u2hh23487y2h834h"),
	"name"	: "Apple Store",
	"city" : "Palo Alto",
	"location": {
				"type": "Point",
				"coordinates" : [
				-122.1691,
				37.4434854
				]
	},
	"type": "Retail"
}

> db.places.ensureIndex({'location':'2dsphere'})
```
__example query:__

```javascript
db.places.find({
	location: {
		$near: { // search everything near a following coordinates
			$geometry: {
				type: "Point",
				coordinates: [-122.166, 37.422545]},
			$maxDistance: 2000 // meters
			}
		}
	}).pretty()
```

__$near operator NEED a 2dsphere index__


### QUIZ: GEOSPATIAL SPHERICAL - What is the query that will query a collection named "stores" to return the stores that are within 1,000,000 meters of the location latitude=39, longitude=-130? Type the query in the box below. Assume the stores collection has a 2dsphere index on "loc" and please use the "$near" operator. Each store record looks like this:

```javascript
{ "_id" : { "$oid" : "535471aaf28b4d8ee1e1c86f" }, 
	"store_id" : 8, 
	"loc" : { 
		"type" : "Point", 
		"coordinates" : [ -37.47891236119904, 4.488667018711567 ]
	} 
}
```
#### Result:
```javascript
db.stores.find({
	loc: {
		$near: {
			$geometry: {
				type: "Point",
				coordinates: [-130, 39]
			},
            $maxDistance: 1000000 
		}
	}
}
)
```

### Text Indexes: Full Text Search 

[query indexed text field with regular expressions:]
(https://docs.mongodb.org/manual/reference/operator/query/regex/?_ga=1.191208106.1032289527.1444485490#index-use)

> Example collection:

```javascript
{"_id": "12912678134", words" : "rat tree ruby."}
{"_id": "43453458134", words" : "dog shrub ruby"}
{"_id": "dh6f4734783", words" : "rat tree obsidian."}
```

> create index for text fields:

```javascript
db.sentences.ensureIndex({'words':'text'})
```

> find using index on collection:

```javascript
db.sentences.find({$text: {$search: 'dog'}})
```

> if you use a regular search, it does not find the Document with dog inside:

__this does not work:__
```javascript
db.sentences.find({'words':'dog'})
```

> Putting more words, find words using an __OR__ operator:

```javascript
db.sentences.find({$text: {$search: 'dog'}})
```

> search best matches with $meta operator and order by score:

```javascript
db.sentences.find( 
		{ $text: {$search: 'rat tree obsidian'} }, 
		{ score: {$meta: 'textScore'} }
		)
	.sort( {score: {$meta: 'textScore'}} )
```

### QUIZ: TEXT INDEXES - You create a text index on the "title" field of the movies collection, and then perform the following text search:
```javascript
> db.movies.find( { $text : { $search : "Big Lebowski" } } )
```
Which of the following documents will be returned, assuming they are in the movies collection? Check all that apply.
#### Result:
```javascript
// all the following are OK
{ "title" : "The Big Lebowski" , star: "Jeff Bridges" }
{ "title" : "Big" , star : "Tom Hanks" }
{ "title" : "Big Fish" , star: "Ewan McGregor" }
```

### Design/Using Indexed

- Goal: Efficient Read/Write Operations
	- selectivity: minimize records scanned
	- other ops: how are sorts handled?
	- 
__nGoal must be similar to indexScanned__

### QUIZ: EFFICIENCY OF INDEX USE EXAMPLE
In general, which of the following rules of thumb should you keep in mind when building compound indexes? Check all that apply. For this question, use the following definitions: 
equality field: field on which queries will perform an equality test
sort field: field on which queries will specify a sort
range field: field on which queries perform a range test

#### Result:
- Sort fields before equality fields
- __Equality fields before range fields__
- __Sort fields before range fields__
- __Equality fields before sort fields__
- Range fields before equality fields

### Logging slow queries
Mongo automatically log slow queries on text file > 100ms

### Profiler
- system.profile
	- 0 off
	- 1 log slow queries
	- 2 log all queries

> using the profile command of mongodb:

```javascript
mongod --profile 1 --slowms 2
```
 
> show results of the profiler only on test db and foo collection: 

```javascript
db.system.profile.find({ns:/test.foo/}).sort({ts:1}).pretty()
```

> milliseconds greater than 1

```javascript
db.system.profile.find({millis:{$gt:1}}).sort({ts:1}).pretty()
```

> set profiling from mongo shell
	
```javascript
db.getProfilingLevel()
db.getProfilingStatus()
db.setProfilingLevel(1, 4)
```

### QUIZ: PROFILING - Write the query to look in the system profile collection for all queries that took longer than one second, ordered by timestamp descending.

#### Result:

```javascript
db.system.profile.find({millis:{$gt:1000}}).sort({ts:-1}).pretty()
```

### Mongostat command

iostat -> mongostat

1 second

inserts, queries, updates, deletes

wiredtiger and mmv1

### QUIZ: MONGOSTAT

Which of the following statements about mongostat output are true? Check all that apply.

- the mmap column field appears for all storage engines.
- __The getmore column concerns the number of requests per time interval to get additional data from a cursor__
- only the wiredTiger storage engine reports the resident memory size of the database.
- __the faults column appears only in the mmapv1 output__
- by default, mongostat provides information in 100ms increments.

### Mongotop commmand

top --> mongotop

### Sharding

application talks to mongos that connects to multiple replicaset, every replicaset is a shard

- insert needs a shard key

- updates, remove and find broadcast to all shards if shard key is not passed

### Administrative tools

[Administrative Tools](https://docs.mongodb.org/ecosystem/tools/administration-interfaces/?_ga=1.18658105.1032289527.1444485490)

### Homework 4.1

- db.products.find( { 'brand' : "GE" } )
- __db.products.find( { 'brand' : "GE" } ).sort( { price : 1 } )__
- db.products.find( { brand : 'GE' } ).sort( { category : 1, brand : -1 } )
- __db.products.find( { $and : [ { price : { $gt : 30 } },{ price : { $lt : 50 } } ] } ).sort( { brand : 1 } )__

### Homework 4.2

- The query returns 251120 documents.
- __The query examines 251120 documents.__
- The query uses an index to determine which documents match.
- The query is a covered query.
- __The query uses an index to determine the order in which to return result documents.__

### Homework 4.3

Your assignment is to make the following blog pages fast:

1. The blog home page
2. The page that displays blog posts by tag (http://localhost:8082/tag/whatever)
3. The page that displays a blog entry by permalink (http://localhost:8082/post/permalink)

By fast, we mean that indexes should be in place to satisfy these queries such that we only need to scan the number of documents we are going to return.

#### Solution - Preliminary steps to set the profiler and show the query to optimize:

> use blog database, otherwise the query on the profiler are not shown:

```javascript
use blog
```

> Enable profiling to show queries, put 0 so that queries will be shown also on the log of mongod:

```javascript
db.setProfilingLevel(2, 0)
```

> query the profiler to show only the query of the posts collection. Use the mongo shell command: 

```javascript
db.system.profile.find({op:'query', ns:'blog.posts'}).sort( { ts : -1 } ).limit(1).pretty()
```

1. ##### Solution to optimize the blog page:

> create and index for the date field:

```javascript
db.posts.createIndex({date:-1})
```

2. ##### Solution to optimize the page that displays blog posts by tag (http://localhost:8082/tag/whatever) 

> the profile shows the following query:

```javascript
{
	"op" : "query",
	"ns" : "blog.posts",
	"query" : {
		"$query" : {
			"tags" : {
				"$in" : [
					"tune"
				]
			}
		},
		"$orderby" : {
			"date" : -1
		}
	},
 ...
``` 

> So we need to create a multikey index for tags (the match) and date (sorting)
 
```db.posts.createIndex({tags: 1, date: -1})```

3. ##### Solution to optimize the page that displays a blog entry by permalink (http://localhost:8082/post/permalink)

The profile shows the following query:

```javascript
{
	"op" : "query",
	"ns" : "blog.posts",
	"query" : {
		"$query" : {
			"permalink" : "mxwnnnqaflufnqwlekfd"
		}
	},
```

> so we need to index the permalink field

```javascript
db.posts.createIndex({permalink: 1})
```

### Homework 4.4

The correct query to show the result is:

```javascript
db.profile.find({op:'query', ns: 'school2.students'}, {_id:0, millis:1}).sort({millis:-1}).limit(1).pretty()
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