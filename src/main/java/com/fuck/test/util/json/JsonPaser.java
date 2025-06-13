package com.fuck.test.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class JsonPaser {

    public static Logger logger = LoggerFactory.getLogger(JsonPaser.class);


    private static ObjectMapper mapper = new ObjectMapper();


    static {
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(sdf);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    /**
     *
     *
     *
     * @param json
     * @param path path 路径,/为根节点,例/aaa/bbb
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T searchJsonByPath(String json, String path, TypeReference<T> typeReference) {
        //logger.info("searchJsonByPath,json:{},path:{}", json, path);
        JsonNode node = null;
        try {
            node = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            logger.error("json转树失败,jsonStr:{}", json);
            throw new RuntimeException();
        }

        JsonNode targetNode = node.at(path);
        if (targetNode instanceof MissingNode) {
            logger.error("searchJsonByPath目标路径不存在,json:{},path:{}", json, path);
            throw new RuntimeException();
        }

        T t = null;
        String targetStr = targetNode.toString();

        try {
            t = mapper.readValue(targetStr,typeReference);
        } catch (Exception e) {
            logger.error("searchJsonByPath转换目标对象失败,jsonStr:{},targetClass:{}", targetStr, typeReference.getType().getTypeName(),e);
            throw new RuntimeException();
        }
        return t;
    }


    public static void main(String[] args) {
        String json = "{\n" +
                "    \"firstName\": \"John\",\n" +
                "    \"lastName\": \"Doe\",\n" +
                "    \"address\": {\n" +
                "      \"street\": \"21 2nd Street\",\n" +
                "      \"city\": \"New York\",\n" +
                "      \"postalCode\": \"10021-3100\",\n" +
                "      \"coordinates\": {\n" +
                "        \"latitude\": 40.7250387,\n" +
                "        \"longitude\": -73.9932568\n" +
                "      }\n" +
                "    },\n" +
                "    \"phone\":[\"139\",\"137\"],\n" +
                "    \"grade\":[\n" +
                "        {\"name\":\"math\",\"score\":99},\n" +
                "        {\"name\":\"english\",\"score\":97}\n" +
                "    ],\n" +
                "\"age\":12," +
                "\"name\":null" +
                "  }";
        //string
        System.out.println(searchJsonByPath(json, "/firstName", new TypeReference<String>() {
        }));
        //object
        System.out.println(searchJsonByPath(json, "/address/coordinates", new TypeReference<Map>() {
        }));
        //array
        System.out.println(searchJsonByPath(json, "/phone",new TypeReference<List>() {
        }) );
        //数组元素
        System.out.println(searchJsonByPath(json, "/phone/0",new TypeReference<Integer>() {
        }));
        //Integer
        System.out.println(searchJsonByPath(json, "/age", new TypeReference<Integer>() {
        }));
        //null
        Integer x = searchJsonByPath(json, "/name", new TypeReference<Integer>() {
        });
        if (x == null) {
            System.out.println(x);
        }
        //List
        System.out.println(searchJsonByPath(json, "/grade", new TypeReference<List>() {
        }));
    }



}

