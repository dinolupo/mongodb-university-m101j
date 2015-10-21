package com.mongodb;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.halt;

/**
 * Created by dino on 15/10/15.
 */
public class HelloWorldSparkFreemarkerStyle {
    public static void main(String[] args) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setClassForTemplateLoading(HelloWorldFreemarkerStyle.class, "/");

        get("/hello", (req, res) -> {
                StringWriter writer = new StringWriter();
                try {
                    Template helloTemplate = configuration.getTemplate("hello.ftl");
                    Map<String, Object> helloMap = new HashMap<String, Object>();
                    helloMap.put("name", "Freemarker");
                    helloTemplate.process(helloMap, writer);
                    System.out.println(writer);
                } catch (Exception e) {
                    halt(500);
                    e.printStackTrace();
                }
                return writer;
            }
        );
    }
}
