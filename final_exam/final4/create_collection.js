use test;
db.stuff.drop();
for (i = 0; i < 10000; i++) {
   var a = Math.random() < 0.2 ? 1 : Math.random()*20000;
   var c = Math.random() < 0.5 ? 1 :0;
   record = {'a':a, 'b':Math.random()*20000, 'c':c};
   db.stuff.insert(record);
}
