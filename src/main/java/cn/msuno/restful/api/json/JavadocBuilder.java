package cn.msuno.restful.api.json;

import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_API;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_CONSUMES;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DEPRECATED;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DESCRIPTION;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DOC;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_ENUM_CONSTANTS;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_METHODS;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_NAME;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_PATH;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_PRODUCES;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_PROPERTIES;
import static cn.msuno.restful.api.json.JavadocUtils.isBlank;
import static javax.lang.model.element.ElementKind.ENUM_CONSTANT;
import static javax.lang.model.element.ElementKind.FIELD;
import static javax.lang.model.element.ElementKind.METHOD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

class JavadocBuilder {
    
    private final ProcessingEnvironment processingEnv;
    
    JavadocBuilder(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }
    
    JSONObject getClassJavadocAsJsonOrNull(TypeElement classElement, String api) {
        String classDoc = processingEnv.getElementUtils().getDocComment(classElement);
        String className = classElement.getQualifiedName().toString();
        if (isBlank(classDoc)) {
            classDoc = className;
        }
        
        Map<ElementKind, List<Element>> children = new EnumMap<>(ElementKind.class);
        for (Element enclosedElement : classElement.getEnclosedElements()) {
            if (!children.containsKey(enclosedElement.getKind())) {
                children.put(enclosedElement.getKind(), new ArrayList<>());
            }
            children.get(enclosedElement.getKind()).add(enclosedElement);
        }
        
        final List<Element> emptyList = Collections.emptyList();
        // 属性
        List<Element> enclosedFields = defaultIfNull(children.get(FIELD), emptyList);
        // 枚举
        List<Element> enclosedEnumConstants = defaultIfNull(children.get(ENUM_CONSTANT), emptyList);
        // 方法
        List<Element> enclosedMethods = defaultIfNull(children.get(METHOD), emptyList);
       
        if (isBlank(classDoc)) {
            return null;
        }
    
        // 属性json
        JSONObject fieldDocs = getJavacAsJson(enclosedFields, new FieldJavadocAsJson(processingEnv));
    
        JSONObject json = new JSONObject();
        json.put(ELEMENT_DOC, classDoc);
        json.put(ELEMENT_PROPERTIES, fieldDocs);
        json.put(ELEMENT_NAME, className);
        json.put(ELEMENT_DEPRECATED, hasDeprecated(classElement));
        json.put(ELEMENT_DESCRIPTION, classDoc.trim());
        if (ELEMENT_API.equals(api)) {
            RequestMapping requestMapping = classElement.getAnnotation(RequestMapping.class);
            String[] value = requestMapping.value();
            String[] consumes = requestMapping.consumes();
            String[] produces = requestMapping.produces();
            json.put(ELEMENT_PATH, value);
            json.put(ELEMENT_CONSUMES, consumes);
            json.put(ELEMENT_PRODUCES, produces);
            JSONArray enumConstantDocs = getJavacAsJson(enclosedEnumConstants, new FieldJavadocAsJson(processingEnv), api);
            JSONArray methodDocs = getJavacAsJson(enclosedMethods, new MethodJavadocAsJson(processingEnv), api);
            buildParam(methodDocs, requestMapping);
            json.put(ELEMENT_ENUM_CONSTANTS, enumConstantDocs);
            json.put(ELEMENT_METHODS, methodDocs);
        }
        
        return json;
    }
    
    private void buildParam(JSONArray array, RequestMapping requestMapping) {
        for (int i = 0; i < array.size(); i ++) {
            JSONObject object = array.getJSONObject(i);
            if (object.getJSONArray(ELEMENT_PRODUCES).isEmpty()) {
                object.put(ELEMENT_PRODUCES, requestMapping.produces());
            }
            if (object.getJSONArray(ELEMENT_CONSUMES).isEmpty()) {
                object.put(ELEMENT_CONSUMES, requestMapping.consumes());
            }
            JSONArray jsonArray = object.getJSONArray(ELEMENT_PATH);
            List<String> child = new ArrayList<>();
            for (int j = 0; j < jsonArray.size(); j ++) {
                for (String str : requestMapping.value()) {
                    child.add(str + jsonArray.getString(j));
                }
            }
            object.put(ELEMENT_PATH, child);
        }
    }
    
    private static JSONArray getJavacAsJson(List<Element> elements, JavadocAsJson createDoc, String api) {
        JSONArray jsonArray = new JSONArray();
        for (Element e : elements) {
            JSONObject eMapped = createDoc.apply(e, api);
            if (eMapped != null) {
                jsonArray.add(eMapped);
            }
        }
        return jsonArray;
    }
    
    /**
     *
     * @param elements
     * @param createDoc
     * @return
     */
    private static JSONObject getJavacAsJson(List<Element> elements, JavadocAsJson createDoc) {
        JSONObject jsonObject = new JSONObject();
        for (Element e : elements) {
            JSONObject eMapped = createDoc.apply(e, null);
            if (eMapped != null) {
                jsonObject.putAll(eMapped);
            }
        }
        return jsonObject;
    }
    
    /**
     * 判断元素或方法是否已经弃用
     * @param element   元素
     * @return  true / false
     */
    static boolean hasDeprecated(Element element){
        return element.getAnnotation(Deprecated.class) != null;
    }
    
    private static <T> T defaultIfNull(T actualValue, T defaultValue) {
        return actualValue != null ? actualValue : defaultValue;
    }
    
}
