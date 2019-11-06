package cn.msuno.restful.api.json;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;

import cn.msuno.restful.api.bean.Swagger;

public class JavadocUtils {
    
    private static final Pattern blockSeparator = Pattern.compile("\\s", Pattern.MULTILINE);
    private static final Set<String> beanType = new HashSet<>();
    private static final Pattern description = Pattern.compile("\\s*@", Pattern.MULTILINE);
    
    
    public static final String PACKAGES_OPTION = "javadoc.packages";
    public static final String JAVADOC_RESOURCE_SUFFIX = "_javadoc.json";
    public static final String ELEMENT_PARAM_TYPE = "parameters";
    public static final String ELEMENT_ENUM_CONSTANTS = "enumConstants";
    public static final String ELEMENT_FIELDS = "fields";
    public static final String ELEMENT_PROPERTIES = "properties";
    public static final String ELEMENT_METHODS = "methods";
    public static final String ELEMENT_NAME = "name";
    public static final String ELEMENT_DEPRECATED = "deprecated";
    public static final String ELEMENT_DOC = "doc";
    public static final String ELEMENT_TITLE = "title";
    public static final String ELEMENT_CONSUMES = "consumes";
    public static final String ELEMENT_IN = "in";
    public static final String ELEMENT_DESCRIPTION = "description";
    public static final String ELEMENT_REQUIRED = "required";
    public static final String ELEMENT_PRODUCES = "produces";
    public static final String ELEMENT_METHOD = "method";
    public static final String ELEMENT_PATH = "path";
    public static final String ELEMENT_DEFAULT = "default";
    public static final String ELEMENT_TYPE = "type" ;
    public static final String ELEMENT_ABSOLUTE_TYPE = "absoluteType";
    public static final String ELEMENT_API = "ELEMENT_API";
    public static final String ELEMENT_QUERY = "query";
    public static final String ELEMENT_BODY = "body";
    public static final String UTF_8 = "UTF-8";
    public static final String ELEMENT_JSON = "json";
    public static final String ELEMENT_JSON_RES = "json.res";
    public static final String ELEMENT_PARAM = "param";
    public static final String ELEMENT_SUMMARY = "summary";
    public static final String ELEMENT_RESPONSES = "responses";
    public static final String ELEMENT_TAGS = "tags";
    public static final String ELEMENT_TAG = "tag";
    public static final String ELEMENT_IGNORE = "ignore";
    public static final String ELEMENT_SWAGGER = "swagger";
    
    /** base string */
    public static final String ELEMENT_DOT = ".";
    public static final String ELEMENT_DOLL = "$";
    public static final String ELEMENT_START = "*";
    public static final String ELEMENT_EMPTY = "";
    public static final String ELEMENT_SYMBOL = ",";
    public static final String ELEMENT_E = "[]";
    public static final String ELEMENT_LT = "<";
    public static final String ELEMENT_GT = ">";
    public static final String ELEMENT_REF = "$ref";
    public static final String ELEMENT_JSON_PATH = "./json";
    public static final String ELEMENT_JSON_RES_PATH = "./json/res";
    public static final String ELEMENT_USING = "Using";
    public static final String ELEMENT_DEFINITIONS = "#/definitions/";
    public static final String ELEMENT_ITEMS = "items";
    public static final String ELEMENT_SCHEMA = "schema";
    public static final String ELEMENT_OPERATIONID = "operationId";
    public static final String ELEMENT_200 = "200";
    public static final String ELEMENT_201 = "201";
    public static final String ELEMENT_401 = "401";
    public static final String ELEMENT_403 = "403";
    public static final String ELEMENT_404 = "404";
    public static final String ELEMENT_OK = "OK";
    public static final String ELEMENT_NOT_FOUND = "Not Found";
    public static final String ELEMENT_FORBIDDEN = "Forbidden";
    public static final String ELEMENT_UNAUTHORIZED = "Unauthorized";
    public static final String ELEMENT_CREATED = "Created";
    
    /** base type */
    public static final String ELEMENT_AB_LONG = "java.lang.Long";
    public static final String ELEMENT_AB_INTEGER = "java.lang.Integer";
    public static final String ELEMENT_AB_STRING = "java.lang.String";
    public static final String ELEMENT_AB_LIST = "java.util.List";
    public static final String ELEMENT_AB_DOUBLE = "java.lang.Double";
    public static final String ELEMENT_INT = "int";
    public static final String ELEMENT_T = "T";
    public static final String ELEMENT_INTEGER = "integer";
    public static final String ELEMENT_FORMAT = "format";
    public static final String ELEMENT_INT32 = "int32";
    public static final String ELEMENT_INT64 = "int64";
    public static final String ELEMENT_NUMBER = "number";
    public static final String ELEMENT_DOUBLE = "double";
    public static final String ELEMENT_LONG = "long";
    public static final String ELEMENT_STRING = "string";
    public static final String ELEMENT_ARRAY = "array";
    public static final String ELEMENT_OBJECT = "object";
    public static final String ELEMENT_BOOLEAN = "boolean";
    public static final String ELEMENT_AB_BOOLEAN = "java.lang.Boolean";
    public static final String ELEMENT_CHAR = "char";
    public static final String ELEMENT_AB_CHAR = "java.lang.Character";
    public static final String ELEMENT_FLOAT = "float";
    public static final String ELEMENT_AB_FLOAT = "java.lang.Float";
    public static final String ELEMENT_SHORT = "short";
    public static final String ELEMENT_AB_SHORT = "java.lang.Short";
    public static final String ELEMENT_BYTE = "byte";
    public static final String ELEMENT_AB_BYTE = "java.lang.Byte";
    
    
    private JavadocUtils() {
        throw new AssertionError("not instantiable");
    }
    
    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
    
    /**
     * java类型转前端类型
     * @param type  java.lang.Integer : integer
     * @return  integer
     */
    public static String convertType(String type) {
        Map<String, String> map = ImmutableMap.<String, String> builder()
                .put(ELEMENT_AB_INTEGER, ELEMENT_INTEGER)
                .put(ELEMENT_AB_STRING, ELEMENT_STRING)
                .put(ELEMENT_INT, ELEMENT_INTEGER)
                .put(ELEMENT_AB_LIST, ELEMENT_ARRAY)
                .put(ELEMENT_LONG, ELEMENT_INTEGER)
                .put(ELEMENT_AB_LONG, ELEMENT_INTEGER)
                .put(ELEMENT_DOUBLE, ELEMENT_NUMBER)
                .put(ELEMENT_AB_DOUBLE, ELEMENT_NUMBER)
                .put(ELEMENT_CHAR, ELEMENT_STRING)
                .put(ELEMENT_AB_CHAR, ELEMENT_STRING)
                .put(ELEMENT_FLOAT, ELEMENT_NUMBER)
                .put(ELEMENT_AB_FLOAT, ELEMENT_NUMBER)
                .put(ELEMENT_SHORT, ELEMENT_NUMBER)
                .put(ELEMENT_AB_SHORT, ELEMENT_NUMBER)
                .put(ELEMENT_BYTE, ELEMENT_INT32)
                .put(ELEMENT_AB_BYTE, ELEMENT_INT32)
                //.put(ELEMENT_BOOLEAN, ELEMENT_BOOLEAN)
                .put(ELEMENT_AB_BOOLEAN, ELEMENT_BOOLEAN)
                .build();
        return map.getOrDefault(type, type);
    }
    
    /**
     * methods javadoc 转json
     * @param json  javadoc json
     * @return {"summary":"测试接口\\n","return\\n":"","param":{"password":"密码\\n","name":"用户名\\n"},"description":"获取swagger配置接口\\n"}
     */
    static JSONObject javaDoc(String json){
        JSONObject res = new JSONObject();
        res.put(ELEMENT_PARAM, new JSONObject());
        Pattern pattern = Pattern.compile("\\s*@", Pattern.MULTILINE);
        Pattern space = Pattern.compile("\\s", Pattern.MULTILINE);
        String[] split = pattern.split(json);
        res.put(ELEMENT_DESCRIPTION, split[0].trim());
        for (int i = 1; i < split.length; i ++) {
            String[] stars = space.split(split[i].trim(), 2);
            if (stars.length != 2) {
                res.put(stars[0], ELEMENT_EMPTY);
            } else if (ELEMENT_PARAM.equals(stars[0].trim())) {
                JSONObject jo = res.getJSONObject(ELEMENT_PARAM);
                String[] jos = space.split(stars[1], 2);
                if (jos.length != 2) {
                    jo.put(jos[0].trim(), ELEMENT_EMPTY);
                } else {
                    jo.put(jos[0].trim(), jos[1].trim());
                }
                res.put(ELEMENT_PARAM, jo);
            } else {
                res.put(stars[0], stars[1].trim());
            }
        }
        return res;
    }
    
    public static boolean baseType(String str){
        for (String s : Arrays.asList(ELEMENT_INTEGER, ELEMENT_NUMBER, ELEMENT_ARRAY, ELEMENT_STRING, ELEMENT_INT, ELEMENT_INT32, ELEMENT_INT64, ELEMENT_BOOLEAN)) {
            if (s.equals(str)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取简易名称
     * @param name  cn.msuno.starter.Response&lt;java.util.List&lt;cn.msuno.starter.User&gt;&gt; : Response«List«User»»
     * @return  Response«List«User»»
     */
    public static String getSimpleName(String name) {
        String[] split = name.split(ELEMENT_LT);
        for (int i = 0; i < split.length; i ++) {
            String str = split[i].trim().replace(ELEMENT_GT,ELEMENT_EMPTY);
            String simple = str.substring(str.lastIndexOf(ELEMENT_DOT)+1);
            name = name.replace(str, simple);
        }
        name = name.replace(ELEMENT_LT, "«");
        name = name.replace(ELEMENT_GT, "»");
        return name;
    }
    
    /**
     * 获取下一层结构
     * @param type  cn.msuno.starter.Response&lt;java.util.List&lt;cn.msuno.starter.User&gt;&gt; : java.util.List&lt;cn.msuno.starter.User&gt;
     * @return  java.util.List&lt;cn.msuno.starter.User&gt;
     */
    public static String setRef(String type){
        if (!type.contains(ELEMENT_LT)) {
            return type;
        }
        return type.substring(type.indexOf(ELEMENT_LT)+1, type.lastIndexOf(ELEMENT_GT));
    }
    
    /**
     * 是否是自定义类
     * @param clz   Class.class
     * @return true / false
     */
    public static boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }
    
    /**
     * 判断开头是否是数组
     * @param str   java.util.List&lt;cn.msuno.starter.User&gt;
     * @return  true / false
     */
    public static boolean isArray(String str) {
        return str.startsWith(ELEMENT_AB_LIST);
    }
    
    public static Map<String, String> buildTag(JSONObject json) {
        String doc = json.getString(ELEMENT_DOC);
        String[] split = description.split(doc);
        String name = json.getString(ELEMENT_NAME);
        if (name.length() > name.indexOf(ELEMENT_DOT)) {
            name = name.substring(name.lastIndexOf(ELEMENT_DOT) + 1);
        }
        Map<String, String> tag = new HashMap<>();
        
        tag.put(ELEMENT_DESCRIPTION, split[0] == null ? name : split[0].trim());
        for (int i = 1; i < split.length; i ++) {
            String[] sp = blockSeparator.split(split[i].trim(), 2);
            if (ELEMENT_TAG.equals(sp[0]) && sp.length > 1) {
                name = sp[i];
            }
        }
        tag.put(ELEMENT_NAME, name);
        return tag;
    }
    
    public static JSONObject definitions(String type, JSONObject jsonObject, Set<String> set){
        if (null == jsonObject || jsonObject.isEmpty()) {
            return null;
        }
        JSONObject res = new JSONObject();
        for (Map.Entry<String, Object> entry: jsonObject.entrySet()) {
            JSONObject object = JSONObject.parseObject(entry.getValue().toString());
            if (Objects.isNull(object) && !isBlank(entry.getValue().toString())) {
                object = (JSONObject)entry.getValue();
                continue;
            }
            String strType = object.getString(ELEMENT_TYPE);
            if (isBlank(strType)) {
                continue;
            }
            if (ELEMENT_T.equals(strType)){
                String s = JavadocUtils.setRef(type);
                if (JavadocUtils.isArray(s)) {
                    s = JavadocUtils.getSimpleName(JavadocUtils.setRef(s));
                    object.put(ELEMENT_TYPE, ELEMENT_ARRAY);
                    JSONObject oj = new JSONObject();
                    oj.put(ELEMENT_REF, ELEMENT_DEFINITIONS + s);
                    object.put(ELEMENT_ITEMS, oj);
                } else {
                    object.put(ELEMENT_REF, ELEMENT_DEFINITIONS + JavadocUtils.getSimpleName(s));
                    object.remove(ELEMENT_TYPE);
                }
            } else if (JavadocUtils.baseType(strType)) {
                String chs = JavadocUtils.setRef(strType);
                if (JavadocUtils.isArray(strType)) {
                    object.put(ELEMENT_TYPE, ELEMENT_ARRAY);
                    JSONObject oj = new JSONObject();
                    oj.put(ELEMENT_REF, ELEMENT_DEFINITIONS + JavadocUtils.getSimpleName(chs));
                    object.put(ELEMENT_ITEMS, oj);
                } else {
                    object.put(ELEMENT_REF, ELEMENT_DEFINITIONS + JavadocUtils.getSimpleName(chs));
                    object.remove(ELEMENT_TYPE);
                }
                set.add(strType);
            }
            res.put(entry.getKey(), object);
        }
        return res;
    }
    
    public static void path(Swagger swagger, JSONObject json) {
        JSONArray methods = json.getJSONArray(ELEMENT_METHODS);
        for (int i = 0; i < methods.size(); i ++) {
            JSONObject jsonObject = methods.getJSONObject(i);
            JSONArray childPath = jsonObject.getJSONArray(ELEMENT_PATH);
            JSONArray mds = jsonObject.getJSONArray(ELEMENT_METHOD);
            String absoluteType = jsonObject.getString(ELEMENT_ABSOLUTE_TYPE);
            jsonObject.remove(ELEMENT_PATH);
            jsonObject.remove(ELEMENT_METHOD);
            jsonObject.remove(ELEMENT_ABSOLUTE_TYPE);
            jsonObject.put(ELEMENT_TAGS, Collections.singletonList(buildTag(json).get(ELEMENT_NAME)));
            JSONArray parameters = jsonObject.getJSONArray(ELEMENT_PARAM_TYPE);
            for (int m = 0; m < parameters.size(); m ++) {
                JSONObject object = parameters.getJSONObject(m);
                String type = object.getString(ELEMENT_TYPE);
                if (JavadocUtils.baseType(type)) {
                    beanType.add(type);
                    String chs = JavadocUtils.setRef(type);
                    if (JavadocUtils.isArray(type)) {
                        JSONObject ob = new JSONObject();
                        ob.put(ELEMENT_TYPE, ELEMENT_ARRAY);
                        JSONObject oj = new JSONObject();
                        oj.put(ELEMENT_REF, ELEMENT_DEFINITIONS + JavadocUtils.getSimpleName(chs));
                        ob.put(ELEMENT_ITEMS, oj);
                        object.put(ELEMENT_SCHEMA, ob);
                    } else {
                        JSONObject ob = new JSONObject();
                        ob.put(ELEMENT_REF, ELEMENT_DEFINITIONS + JavadocUtils.getSimpleName(chs));
                        object.put(ELEMENT_SCHEMA, ob);
                    }
                    object.remove(ELEMENT_TYPE);
                }
            }
            beanType.add(absoluteType);
            for (int k = 0; k < childPath.size(); k ++) {
                for (int j = 0; j < mds.size(); j++) {
                    JSONObject o = new JSONObject();
                    jsonObject.put(ELEMENT_OPERATIONID, mds.getString(j).toLowerCase() + ELEMENT_USING + mds.getString(j).toUpperCase());
                    if (jsonObject.containsKey(ELEMENT_CONSUMES)  && ELEMENT_E.equals(jsonObject.getString(ELEMENT_CONSUMES))) {
                        jsonObject.put(ELEMENT_CONSUMES, ELEMENT_EMPTY);
                    }
                    if (jsonObject.containsKey(ELEMENT_PRODUCES) && ELEMENT_E.equals(jsonObject.getString(ELEMENT_PRODUCES))) {
                        jsonObject.put(ELEMENT_PRODUCES, ELEMENT_EMPTY);
                    }
                    o.put(mds.getString(j).toLowerCase(), jsonObject);
                    swagger.setPath(childPath.getString(k), o);
                }
            }
        }
        
    }
    
    public static void buildDefinitions(Swagger swagger, Set<String> bean, Map<String, JSONObject> json){
        Set<String> set = new HashSet<>();
        for (String type : bean) {
            if (ELEMENT_AB_LIST.equals(type) || type.startsWith(ELEMENT_AB_LIST) || !JavadocUtils.baseType(type) || !JavadocUtils.convertType(type).equals(type)) {
                continue;
            }
            String key = JavadocUtils.getSimpleName(type);
            String[] split = type.split(ELEMENT_LT);
            JSONObject jsonObject = json.get(split[0].trim().replace(ELEMENT_GT, ELEMENT_EMPTY));
            JSONObject properties = definitions(type, jsonObject.getJSONObject(ELEMENT_PROPERTIES), set);
            if (properties != null) {
                jsonObject.put(ELEMENT_PROPERTIES, properties);
            }
            jsonObject.remove(ELEMENT_DOC);
            jsonObject.remove(ELEMENT_NAME);
            jsonObject.remove(ELEMENT_DEPRECATED);
            jsonObject.put(ELEMENT_TYPE, ELEMENT_OBJECT);
            jsonObject.put(ELEMENT_TITLE, key);
            swagger.setDefinition(key, jsonObject);
            for (int i = 1; i < split.length; i ++) {
                String s = split[i].replace(ELEMENT_GT, ELEMENT_EMPTY);
                set.add(s);
            }
        }
        if (!set.isEmpty()) {
            buildDefinitions(swagger, set, json);
        }
    }
    
    public static Swagger build(Set<JSONObject> json, Map<String, JSONObject> map) {
        Swagger swagger = new Swagger();
        json.forEach(v -> {
            swagger.setTag(buildTag(v));
            path(swagger, v);
            buildDefinitions(swagger, beanType, map);
        });
        return swagger;
    }
    
}
