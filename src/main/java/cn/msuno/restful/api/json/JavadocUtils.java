package cn.msuno.restful.api.json;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;

public class JavadocUtils {
    
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
    
    public static final String ELEMENT_JSON = "json";
    public static final String ELEMENT_JSON_RES = "json.res";
    public static final String ELEMENT_PARAM = "param";
    public static final String ELEMENT_SUMMARY = "summary";
    public static final String ELEMENT_RESPONSES = "responses";
    public static final String ELEMENT_TAGS = "tags";
    public static final String ELEMENT_TAG = "tag";
    public static final String ELEMENT_IGNORE = "ignore";
    
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
     * @param type  java.lang.Integer => integer
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
     * 获取简易名称， 替换'>' '<' 为 '«' '»'
     * @param name  cn.msuno.starter.Response<java.util.List<cn.msuno.starter.User>> => Response«List«User»»
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
     * @param type  cn.msuno.starter.Response<java.util.List<cn.msuno.starter.User>> => java.util.List<cn.msuno.starter.User>
     * @return  java.util.List<cn.msuno.starter.User>
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
     * @return
     */
    public static boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }
    
    /**
     * 判断开头是否是数组
     * @param str   java.util.List<cn.msuno.starter.User>
     * @return  true / false
     */
    public static boolean isArray(String str) {
        return str.startsWith(ELEMENT_AB_LIST);
    }
    
    public static void main(String[] args) {
        String sre = "获取swagger配置接口\\n @summary 测试接口\\n @param name  用户名\\n @param password 密码\\n @return\\n";
        JSONObject object = javaDoc(sre);
        System.out.println(object);
        System.out.println(baseType("number"));
    }
    
}
