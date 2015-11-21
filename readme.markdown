# MongoDB University Courseware
-----

## Week One - Introduction


### Create base Java project with Maven Archetype
```mvn archetype:generate```

### Execute Java project with Maven Exec
```mvn clean compile exec:java -Dexec.mainClass=com.mongodb.App```

### Write a query that retrieves documents from a users collection where the name has a "q" in it, and the document has an email field.
```javascript
db.users.find({"name":{$regex:"q"}, email: {$exists:true}});
```
-----
## Week 2 - CRUD

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
## Week 3 - Schema Design

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


## Week 4 - Performance (Indexes)

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
block writes and readers in db			do not block readred and writers

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
db.example.find( { name : { "$in" : ["Alfred", "Bruce" ] } }, { name : 1, hair : 1 } )
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
{"_id": "12912678134", words : "rat tree ruby."}
{"_id": "43453458134", words : "dog shrub ruby"}
{"_id": "dh6f4734783", words : "rat tree obsidian."}
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

##### Solution 1 to optimize the blog page:

> create and index for the date field:

```javascript
db.posts.createIndex({date:-1})
```

##### Solution 2 to optimize the page that displays blog posts by tag (http://localhost:8082/tag/whatever)

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

##### Solution 3 to optimize the page that displays a blog entry by permalink (http://localhost:8082/post/permalink)

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

## Week 5 - Aggregatoin Framework

### Aggregation example

> Given item of the following type:

```javascript
{
	"_id" : ObjectId("5645f710a89e99f870431083"),
	"name" : "Vaio",
	"category" : "Laptops",
	"manufacturer" : "Sony",
	"price" : 499
}
```
> a simple aggregation query is the following:

```javascript
use agg
db.products.aggregate([
    {$group:
     {
	 _id:"$manufacturer",
	 num_products:{$sum:1}
     }
    }
])
```
> With results like this:

```javascript
{ "_id" : "Amazon", "num_products" : 2 }
{ "_id" : "Sony", "num_products" : 1 }
{ "_id" : "Samsung", "num_products" : 2 }
{ "_id" : "Google", "num_products" : 1 }
{ "_id" : "Apple", "num_products" : 4 }
```

### Quiz: Simple Aggregation Example - Write the aggregation query that will find the number of products by category of a collection that has the form:
```javascript
{
	"_id" : ObjectId("50b1aa983b3d0043b51b2c52"),
	"name" : "Nexus 7",
	"category" : "Tablets",
	"manufacturer" : "Google",
	"price" : 199
}
```
Have the resulting key be called "num_products":
#### Result:
```javascript
> db.products.aggregate([
	{$group:
		{"_id":"$category", num_products:{$sum:1}}
	}
])

{ "_id" : "Laptops", "num_products" : 2 }
{ "_id" : "Cell Phones", "num_products" : 1 }
{ "_id" : "Tablets", "num_products" : 7 }
```

### Aggregation Pipeline

collection -> each Items of the array of the aggregate function is an element of the pipeline

NAME		-	TYPE		- INPUT/OUTPUT ORDER OF TRANSFORMATION
---				---			---
$project	- reshape 	- 1:1
$match 	- filtering 	- n:1
$group 	- aggregate 	- n:1
$sort		- sort			- 1:1
$skip		- skips		- n:1
$limit		- limit		- n:1
$unwind	- normalize	- 1:n
$out		- output		- 1:1

Not covered in course: $redact and $geonar

### Compound filter example: use _id Document instead of a key

```javascript
db.products.aggregate([
    {$group:
     {
	 _id: {
	     "manufacturer":"$manufacturer",
	     "category" : "$category"},
	 num_products:{$sum:1}
     }
    }
])
```

### $group expressions

- $sum
- $avg
- $min
- $max
- $push		--> duplicate values
- $addToSet	--> do not duplicate values, so better
- $first	-->	NEED SORT otherwise arbitrary
- $last	-->	NEED SORT otherwise arbitrary


###  Quiz: Using $avg
```javascript
> db.zips.findOne()
{
	"_id" : "01002",
	"city" : "CUSHMAN",
	"loc" : [
		-72.51565,
		42.377017
	],
	"pop" : 36963,
	"state" : "MA"
}
```
#### Result:

```javascript
db.zips.aggregate([{"$group":{"_id":"$state", "average":{$avg: "$pop"}}}])
```

Finding the max population per state:

```javascript
db.zips.aggregate([{"$group":{"_id":"$state", "pop":{$max: "$pop"}}}])
```

### Using $addToSet

```javascript
db.products.aggregate([
    {$group:
     {
	 _id: {
	     "maker":"$manufacturer"
	 },
	 categories:{$addToSet:"$category"}
     }
    }
])

{ "_id" : { "maker" : "Amazon" }, "categories" : [ "Tablets" ] }
{ "_id" : { "maker" : "Sony" }, "categories" : [ "Laptops" ] }
{ "_id" : { "maker" : "Samsung" }, "categories" : [ "Tablets", "Cell Phones" ] }
{ "_id" : { "maker" : "Google" }, "categories" : [ "Tablets" ] }
{ "_id" : { "maker" : "Apple" }, "categories" : [ "Laptops", "Tablets" ] }
```


### Quiz: Using $addToSet - Write an aggregation query that will return the postal codes that cover each city. The results should look like this:
```javascript
		{
			"_id" : "CENTREVILLE",
			"postal_codes" : [
				"22020",
				"49032",
				"39631",
				"21617",
				"35042"
			]
		},
```
#### Result:
```javascript
db.zips.aggregate([{$group:{_id:"$city", postal_codes:{$addToSet:"$_id"}}}])
```

### QUIZ - Double Group example

```javascript
> db.test.find()
{ "_id" : 0, "a" : 0, "b" : 0, "c" : 21 }
{ "_id" : 1, "a" : 0, "b" : 0, "c" : 54 }
{ "_id" : 4, "a" : 1, "b" : 0, "c" : 22 }
{ "_id" : 5, "a" : 1, "b" : 0, "c" : 5 }
{ "_id" : 6, "a" : 1, "b" : 1, "c" : 87 }
{ "_id" : 3, "a" : 0, "b" : 1, "c" : 17 }
{ "_id" : 7, "a" : 1, "b" : 1, "c" : 97 }
{ "_id" : 2, "a" : 0, "b" : 1, "c" : 52 }
> db.fun.aggregate([{$group:{_id:{a:"$a", b:"$b"}, c:{$max:"$c"}}}, {$group:{_id:"$_id.a", c:{$min:"$c"}}}])
> db.test.aggregate([{$group:{_id:{a:"$a", b:"$b"}, c:{$max:"$c"}}}, {$group:{_id:"$_id.a", c:{$min:"$c"}}}])
{ "_id" : 1, "c" : 22 }
{ "_id" : 0, "c" : 52 }
> db.test.aggregate([{$group:{_id:{a:"$a", b:"$b"}, c:{$max:"$c"}}}])
{ "_id" : { "a" : 0, "b" : 1 }, "c" : 52 }
{ "_id" : { "a" : 1, "b" : 1 }, "c" : 97 }
{ "_id" : { "a" : 1, "b" : 0 }, "c" : 22 }
{ "_id" : { "a" : 0, "b" : 0 }, "c" : 54 }
```


### QUIZ $project EXAMPLE

```javascript
> db.zips.aggregate([{$project:{_id:0, city:{$toLower:"$city"}, pop:1, state:1, zip:"$_id"}}])
{ "city" : "cushman", "pop" : 36963, "state" : "MA", "zip" : "01002" }
{ "city" : "barre", "pop" : 4546, "state" : "MA", "zip" : "01005" }
{ "city" : "brimfield", "pop" : 3706, "state" : "MA", "zip" : "01010" }
{ "city" : "blandford", "pop" : 1240, "state" : "MA", "zip" : "01008" }
{ "city" : "belchertown", "pop" : 10579, "state" : "MA", "zip" : "01007" }
{ "city" : "chester", "pop" : 1688, "state" : "MA", "zip" : "01011" }
{ "city" : "chesterfield", "pop" : 177, "state" : "MA", "zip" : "01012" }
{ "city" : "westover afb", "pop" : 1764, "state" : "MA", "zip" : "01022" }
{ "city" : "chicopee", "pop" : 23396, "state" : "MA", "zip" : "01013" }
{ "city" : "cummington", "pop" : 1484, "state" : "MA", "zip" : "01026" }
{ "city" : "mount tom", "pop" : 16864, "state" : "MA", "zip" : "01027" }
```

### QUIZ

```javascript
db.zips.aggregate([
    {$match:
     {
	 state:"NY"
     }
    },
    {$group:
     {
	 _id: "$city",
	 population: {$sum:"$pop"},
	 zip_codes: {$addToSet: "$_id"}
     }
    },
    {$project:
     {
	 _id: 0,
	 city: "$_id",
	 population: 1,
	 zip_codes:1
     }
    }

])

{ "city" : "cushman", "pop" : 36963, "state" : "MA", "zip" : "01002" }
{ "city" : "barre", "pop" : 4546, "state" : "MA", "zip" : "01005" }
{ "city" : "brimfield", "pop" : 3706, "state" : "MA", "zip" : "01010" }
{ "city" : "blandford", "pop" : 1240, "state" : "MA", "zip" : "01008" }
{ "city" : "belchertown", "pop" : 10579, "state" : "MA", "zip" : "01007" }
{ "city" : "chester", "pop" : 1688, "state" : "MA", "zip" : "01011" }
{ "city" : "chesterfield", "pop" : 177, "state" : "MA", "zip" : "01012" }
{ "city" : "westover afb", "pop" : 1764, "state" : "MA", "zip" : "01022" }
{ "city" : "chicopee", "pop" : 23396, "state" : "MA", "zip" : "01013" }
```


### Quiz: Using $match - Again, thinking about the zipcode collection, write an aggregation query with a single match phase that filters for zipcodes with greater than 100,000 people.
#### Result:
```javascript
> db.zips.aggregate([{$match:{pop:{$gt:100000}}}])
{ "_id" : "10021", "city" : "NEW YORK", "loc" : [ -73.958805, 40.768476 ], "pop" : 106564, "state" : "NY" }
{ "_id" : "10025", "city" : "NEW YORK", "loc" : [ -73.968312, 40.797466 ], "pop" : 100027, "state" : "NY" }
{ "_id" : "11226", "city" : "BROOKLYN", "loc" : [ -73.956985, 40.646694 ], "pop" : 111396, "state" : "NY" }
{ "_id" : "60623", "city" : "CHICAGO", "loc" : [ -87.7157, 41.849015 ], "pop" : 112047, "state" : "IL" }
```

### Quiz: Using $sort - Again, considering the zipcode collection, which has documents that look like this,

```javascript
{
	"city" : "ACMAR",
	"loc" : [
		-86.51557,
		33.584132
	],
	"pop" : 6055,
	"state" : "AL",
	"_id" : "35004"
}
```

Write an aggregation query with just a sort stage to sort by (state, city), both ascending. Assume the collection is called zips.

#### Result:

```javascript
db.zips.aggregate([{$sort: {state:1, city:1}}])
```

### Example with #skip and $limit

```javascript
db.zips.aggregate([
    {$match:
     {
	 state:"NY"
     }
    },
    {$group:
     {
	 _id: "$city",
	 population: {$sum:"$pop"},
     }
    },
    {$project:
     {
	 _id: 0,
	 city: "$_id",
	 population: 1,
     }
    },
    {$sort:
     {
	 population:-1
     }
    },
    {$skip: 10},
    {$limit: 5}
])
```




### Unwind example

```javascript
use agg;
db.items.drop();
db.items.insert({_id:'nail', 'attributes':['hard', 'shiny', 'pointy', 'thin']});
db.items.insert({_id:'hammer', 'attributes':['heavy', 'black', 'blunt']});
db.items.insert({_id:'screwdriver', 'attributes':['long', 'black', 'flat']});
db.items.insert({_id:'rock', 'attributes':['heavy', 'rough', 'roundish']});
db.items.aggregate([{$unwind:"$attributes"}]);

{ "_id" : "nail", "attributes" : "hard" }
{ "_id" : "nail", "attributes" : "shiny" }
{ "_id" : "nail", "attributes" : "pointy" }
{ "_id" : "nail", "attributes" : "thin" }
{ "_id" : "hammer", "attributes" : "heavy" }
{ "_id" : "hammer", "attributes" : "black" }
{ "_id" : "hammer", "attributes" : "blunt" }
{ "_id" : "screwdriver", "attributes" : "long" }
{ "_id" : "screwdriver", "attributes" : "black" }
{ "_id" : "screwdriver", "attributes" : "flat" }
{ "_id" : "rock", "attributes" : "heavy" }
{ "_id" : "rock", "attributes" : "rough" }
{ "_id" : "rock", "attributes" : "roundish" }

```

### Unwind to count the blog tags

```javascript
use blog;
db.posts.aggregate([
    /* unwind by tags */
    {"$unwind":"$tags"},
    /* now group by tags, counting each tag */
    {"$group":
     {"_id":"$tags",
      "count":{$sum:1}
     }
    },
    /* sort by popularity */
    {"$sort":{"count":-1}},
    /* show me the top 10 */
    {"$limit": 10},
    /* change the name of _id to be tag */
    {"$project":
     {_id:0,
      'tag':'$_id',
      'count' : 1
     }
    }
    ])

{ "count" : 13, "tag" : "elbow" }
{ "count" : 12, "tag" : "bonsai" }
{ "count" : 12, "tag" : "grass" }
{ "count" : 11, "tag" : "star" }
{ "count" : 11, "tag" : "quail" }
{ "count" : 11, "tag" : "feeling" }
{ "count" : 11, "tag" : "maria" }
{ "count" : 11, "tag" : "lily" }
{ "count" : 11, "tag" : "oval" }
{ "count" : 11, "tag" : "toy" }    

```

### HOMEWORK 5.1 - Finding the most frequent author of comments on your blog

To help you verify your work before submitting, the author with the fewest comments is Cody Strouth and he commented 68 times.


```javascript
> db.posts.findOne()
{
	"_id" : ObjectId("564787a568755b42533b3241"),
	"body" : "empty_post",
	"permalink" : "cxzdzjkztkqraoqlgcru",
	"author" : "machine",
	"title" : "US Constitution",
	"tags" : [
		"january",
		"mine",
		"modem",
		"literature",
		"saudi arabia",
		"rate",
		"package",
		"respect",
		"bike",
		"cheetah"
	],
	"comments" : [
		{
			"body" : "empty_comment",
			"email" : "eAYtQPfz@kVZCJnev.com",
			"author" : "Kayce Kenyon"
		},
...

		{
			"body" : "empty_comment",
			"email" : "gqEMQEYg@iiBqZCez.com",
			"author" : "Jesusa Rickenbacker"
		}
	]
}
```

### Result:

```javascript
db.posts.aggregate([{$unwind:"$comments"},{$group:{"_id":"$comments.author", "count":{$sum:1}}}, {$sort:{"count":-1}},{$limit:1}])
```

### Homework 5.2

Please calculate the average population of cities in California (abbreviation CA) and New York (NY) (taken together) with populations over 25,000.

--Filter the documents by matching state to NY and CA.
--Group by city & state and do sum on it.
--Filter the documents greater than 25000.
--group by null and do average on it.

#### GOOD SOLUTION

```javascript
db.zips.aggregate([
	 {$match:{$or:[{state:"NY"}, {state:"CA"}]}},
	 {"$group":
	     {"_id"	: {state:"$state", city: "$city"},
			 	"population"	: {$sum:"$pop"}}
		},
		{$match: {population:{$gt:25000}}},
		{$group: { _id: null, "avg":{$avg:"$population"}}}
])
```

**The following is not exact because there are some cities with multiple ZIP codes, so they should be aggregated first and summed up before calculating the average**

```javascript
db.zips.aggregate([{$match:{$or:[{state:"NY"}, {state:"CA"}], pop:{$gt:25000}}}, {$group: { _id: null, "avg":{$avg:"$pop"}} }])
```

#### Result:
44805

### Homework 5.3

```javascript
db.grades.aggregate([
	{ $unwind : "$scores"},
	{ $match: {$or: [ {"scores.type":"exam"}, 	{"scores.type":"homework"} ] } },
	{ $group :  { _id: {"class_id":"$class_id", "student_id":"$student_id"}, "average": {$avg:"$scores.score"}}},
	{ $group : { _id: "$_id.class_id", "class_avg": {"$avg":"$average"}}},
	{$sort:{"class_avg":-1}},
	{$limit: 1}
	])
```

#### Result:
id: 1

-----

### Homework 5.4

```javascript
db.zips.aggregate([ {$project: { first_char: {$substr : ["$city",0,1]}, city: "$city", pop:"$pop"  }}, {$match:{"first_char":{$in:["0","1","2","3","4","5","6","7","8","9"]}}}, {$group:{_id:null, "total":{"$sum":"$pop"} }} ])

{ "_id" : null, "total" : 298015 }
```

-----

## Week 6 - Application Engineering

### Write Concern

```
W	|	j	|	notes
-------------------
1		0		(default) fast - small window of vulnerability, 
				journal may be not written to database
1		1		slow - no vulnerabiliy
0		unack write, present for historical reason (not recommended)
```

#### Quiz
Provided you assume that the disk is persistent, what are the w and j settings required to guarantee that an insert or update has been written all the way to disk.
#### Result:
w=1, j=1

### Network Errors

consider w=1 and j=1

```
Application --------------------> mongod
            <-- network error ---
```

You do not know if the write is succesful in the following cases:

- if network reset or crash between receiving the call and sending the response
- mongodb crashes between the write operation and sending the response

### Replication

- Availailabity
- Fault Tolerance

```
Nodes
   *          *            *
Primary   Secondary    Secondary
(down)    |____________________|
                    |
            (primary election)
  
  *	          *             *
(down)     Primary      Secondary
		
```

**Minumum number of nodes to make an election of a new Primary node is 3**

### Replica Set Elections

#### Types of Replica Set Nodes: 

- Regular
- Arbiter (only voting purpouses)
- Delayed Node / Regular (cannot become a Primary but votes) Priority = 0
- Hidden (use for analytics, Priority=0 cannot become the primary and can partecipate in election)

### Write Consistency

- There is only one primary at a time, and by default all writes and reads goes from the driver to that primary. 
- If a primary goes down, there is a small amount of time, during the election of a new primary, where you cannot do any write until a new primary is up
- you can achieve an *Eventual Consistency* if you set the replica set to accept read from another Secondary node. The replica is **Asynchronous** so you may be read another previous value between write and read if you do this.

> command to accept reads from secondary nodes and achieve *Eventual Consistency*:

`> rs.slaveOk()`

### Creating a replica set

create a replica set on the same machine for learning purpouse, normally you create one mongod for each machine running on the same port
> create_replica.sh 

```bash
#!/usr/bin/env bash

mkdir -p /data/rs1 /data/rs2 /data/rs3
mongod --replSet m101 --logpath "1.log" --dbpath /data/rs1 --port 27017 --oplogSize 64 --fork --smallfiles
mongod --replSet m101 --logpath "2.log" --dbpath /data/rs2 --port 27018 --oplogSize 64 --smallfiles --fork
mongod --replSet m101 --logpath "3.log" --dbpath /data/rs3 --port 27019 --oplogSize 64 --smallfiles --fork
``` 

To tell the mongod to communicate with each other we have to create a configuration and execute the configuration. We have to use the same id that we gave to the command before:

> init_replica.js

```javascript
config = { _id: "m101", members:[
          { _id : 0, host : "localhost:27017"},
          { _id : 1, host : "localhost:27018"},
          { _id : 2, host : "localhost:27019"} ]
};

rs.initiate(config);
rs.status();
```

### Replica Set Internals

- _oplog_ is present on all nodes, and all Secondary reads from the Primary oplog 

### Failover and Rollback

- A secondary node that has been a primary before, Rollbacks writes that are not present in the current primary node.

### Review Implications of Replication

- Seed List - The driver should know and connect to at least one node of the replica to use that.
- Write Concern - set w and j accordingly. w can be higher than 1, and it waits at least w node to acknowledge
- Read Preferences - read from primary or secondaries
- Errors can happen - networks, etc. so check exceptions

### Sharding

Sharding is how we handle scaling out. EVERY SHARD IS A REPLICA SET.

`mongos` is the driver that distributes the writes to different shards.

mongos distributes the writes by a shard key, and you can use the following approaches:

1. range-based approach
2. hash-based approach

The hash-based approach may be slower if you do range-based queries.

### Building a Sharding Environment

> Script to create a sharded environment on localhost 

```bash
# Andrew Erlichson
# MongoDB
# script to start a sharded environment on localhost

# clean everything up
echo "killing mongod and mongos"
killall mongod
killall mongos
echo "removing data files"
rm -rf /data/config
rm -rf /data/shard*


# start a replica set and tell it that it will be shard0
echo "starting servers for shard 0"
mkdir -p /data/shard0/rs0 /data/shard0/rs1 /data/shard0/rs2
mongod --replSet s0 --logpath "s0-r0.log" --dbpath /data/shard0/rs0 --port 37017 --fork --shardsvr --smallfiles
mongod --replSet s0 --logpath "s0-r1.log" --dbpath /data/shard0/rs1 --port 37018 --fork --shardsvr --smallfiles
mongod --replSet s0 --logpath "s0-r2.log" --dbpath /data/shard0/rs2 --port 37019 --fork --shardsvr --smallfiles

sleep 5
# connect to one server and initiate the set
echo "Configuring s0 replica set"
mongo --port 37017 << 'EOF'
config = { _id: "s0", members:[
          { _id : 0, host : "localhost:37017" },
          { _id : 1, host : "localhost:37018" },
          { _id : 2, host : "localhost:37019" }]};
rs.initiate(config)
EOF

# start a replicate set and tell it that it will be a shard1
echo "starting servers for shard 1"
mkdir -p /data/shard1/rs0 /data/shard1/rs1 /data/shard1/rs2
mongod --replSet s1 --logpath "s1-r0.log" --dbpath /data/shard1/rs0 --port 47017 --fork --shardsvr --smallfiles
mongod --replSet s1 --logpath "s1-r1.log" --dbpath /data/shard1/rs1 --port 47018 --fork --shardsvr --smallfiles
mongod --replSet s1 --logpath "s1-r2.log" --dbpath /data/shard1/rs2 --port 47019 --fork --shardsvr --smallfiles

sleep 5

echo "Configuring s1 replica set"
mongo --port 47017 << 'EOF'
config = { _id: "s1", members:[
          { _id : 0, host : "localhost:47017" },
          { _id : 1, host : "localhost:47018" },
          { _id : 2, host : "localhost:47019" }]};
rs.initiate(config)
EOF

# start a replicate set and tell it that it will be a shard2
echo "starting servers for shard 2"
mkdir -p /data/shard2/rs0 /data/shard2/rs1 /data/shard2/rs2
mongod --replSet s2 --logpath "s2-r0.log" --dbpath /data/shard2/rs0 --port 57017 --fork --shardsvr --smallfiles
mongod --replSet s2 --logpath "s2-r1.log" --dbpath /data/shard2/rs1 --port 57018 --fork --shardsvr --smallfiles
mongod --replSet s2 --logpath "s2-r2.log" --dbpath /data/shard2/rs2 --port 57019 --fork --shardsvr --smallfiles

sleep 5

echo "Configuring s2 replica set"
mongo --port 57017 << 'EOF'
config = { _id: "s2", members:[
          { _id : 0, host : "localhost:57017" },
          { _id : 1, host : "localhost:57018" },
          { _id : 2, host : "localhost:57019" }]};
rs.initiate(config)
EOF


# now start 3 config servers
echo "Starting config servers"
mkdir -p /data/config/config-a /data/config/config-b /data/config/config-c 
mongod --logpath "cfg-a.log" --dbpath /data/config/config-a --port 57040 --fork --configsvr --smallfiles
mongod --logpath "cfg-b.log" --dbpath /data/config/config-b --port 57041 --fork --configsvr --smallfiles
mongod --logpath "cfg-c.log" --dbpath /data/config/config-c --port 57042 --fork --configsvr --smallfiles


# now start the mongos on a standard port
mongos --logpath "mongos-1.log" --configdb localhost:57040,localhost:57041,localhost:57042 --fork
echo "Waiting 60 seconds for the replica sets to fully come online"
sleep 60
echo "Connnecting to mongos and enabling sharding"

# add shards and enable sharding on the test db
mongo <<'EOF'
db.adminCommand( { addshard : "s0/"+"localhost:37017" } );
db.adminCommand( { addshard : "s1/"+"localhost:47017" } );
db.adminCommand( { addshard : "s2/"+"localhost:57017" } );
db.adminCommand({enableSharding: "school"})
db.adminCommand({shardCollection: "school.students", key: {student_id:1}});
EOF
```

> test the sharded environment

```javascript
db=db.getSiblingDB("school");
types = ['exam', 'quiz', 'homework', 'homework'];
// 10,000 students
for (i = 0; i < 10000; i++) {

    // take 10 classes
    for (class_counter = 0; class_counter < 10; class_counter ++) {
	scores = []
	    // and each class has 4 grades
	    for (j = 0; j < 4; j++) {
		scores.push({'type':types[j],'score':Math.random()*100});
	    }

	// there are 500 different classes that they can take
	class_id = Math.floor(Math.random()*501); // get a class id between 0 and 500

	record = {'student_id':i, 'scores':scores, 'class_id':class_id};
	db.students.insert(record);

    }

}
```

### Implications of Sharding

- Every document must include a _shard key_
- shard key is **immutable**
- index that starts with the shard key
- no unique index unless it is the shard key

Drivers will connect to multiple mongos process

### Choosing a shard key

1. sufficient cardinality - sufficient variety of values
2. AVOID hotspotting monotonically increment - for example BSON_ID because otherwise you put always values in the last shard. For example Do not shard on order_id but on vendor or order_date or both of them.






### template
```javascript
db.users.findOne()
```
#### Result:
```javascript
{"demo":1}
```
