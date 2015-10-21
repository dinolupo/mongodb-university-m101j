package com.mongodb;

import static spark.Spark.get;

/**
 * Created by dino on 15/10/15.
 */
public class HelloWorldSparkStyle {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
    }
}
