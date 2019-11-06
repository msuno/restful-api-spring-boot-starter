package cn.msuno.restful.api.configuration;

import static cn.msuno.restful.api.json.JavadocUtils.JAVADOC_RESOURCE_SUFFIX;
import static cn.msuno.restful.api.json.JavadocUtils.UTF_8;
import static cn.msuno.restful.api.json.JavadocUtils.isBlank;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

public class SwaggerUtils {
    
    private static Logger logger = LoggerFactory.getLogger(SwaggerUtils.class);
    
    public static List<File> listFiles(String dirPath) {
        List<File> result = new ArrayList<>();
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(dirPath);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                String path = URLDecoder.decode(url.getFile(), UTF_8);
                File file = new File(path);
                File[] files = file.listFiles((dir, name) -> name.endsWith(JAVADOC_RESOURCE_SUFFIX));
                result.addAll(Arrays.asList(files));
            }
        } catch (IOException e) {
            logger.info("get files error {}", e.getMessage());
        }
        return result;
    }
    
    public static File getFile(String name) {
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources("./" + name);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                String path = URLDecoder.decode(url.getFile(), UTF_8);
                File file = new File(path);
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String readFile(File file) {
        if (null == file || file.isDirectory()) {
            return null;
        }
        try {
            return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.info("read file error {}", e.getMessage());
        }
        return null;
    }
    
    public static JSONObject toJson(String res) {
        JSONObject obj = new JSONObject();
        if (!isBlank(res)) {
            obj.putAll(JSONObject.parseObject(res, Feature.DisableCircularReferenceDetect));
        }
        return obj;
    }
    
    public static JSONObject toJson(File file) {
        return toJson(readFile(file));
    }
}
