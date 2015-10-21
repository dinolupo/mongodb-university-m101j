package com.mongodb;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dino on 15/10/15.
 */
public class HelloWorldFreemarkerStyle {
    public static void main(String[] args) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setClassForTemplateLoading(
                HelloWorldFreemarkerStyle.class, "/");
        try {
            Template helloTemplate = configuration.getTemplate("hello.ftl");
            StringWriter writer = new StringWriter();
            Map<String, Object> helloMap = new HashMap<String, Object>();
            helloMap.put("name", "Freemarker");
            helloTemplate.process(helloMap, writer);
            System.out.println(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
