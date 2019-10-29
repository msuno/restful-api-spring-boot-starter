package cn.msuno.restful.api.json;

import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_AB_LONG;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_AB_SHORT;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DESCRIPTION;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DOUBLE;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_FORMAT;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_INT32;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_INT64;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_INTEGER;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_LONG;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_NUMBER;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_SHORT;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_TYPE;
import static cn.msuno.restful.api.json.JavadocUtils.convertType;
import static cn.msuno.restful.api.json.JavadocUtils.isBlank;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

import com.alibaba.fastjson.JSONObject;

class FieldJavadocAsJson implements JavadocAsJson {
    
    private final ProcessingEnvironment processingEnv;
    
    FieldJavadocAsJson(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }
    
    @Override
    public JSONObject apply(Element field, String api) {
        String javadoc = processingEnv.getElementUtils().getDocComment(field);
        if (isBlank(javadoc)) {
            javadoc = field.getSimpleName().toString();
        }
        JSONObject jsonDoc = new JSONObject();
        JSONObject json = new JSONObject();
        json.put(ELEMENT_DESCRIPTION, javadoc.trim());
        String toString = field.asType().toString();
        String convertType = convertType(toString);
        json.put(ELEMENT_TYPE, convertType);
        if (ELEMENT_INTEGER.equals(convertType)) {
            if (toString.equals(ELEMENT_LONG) || toString.equals(ELEMENT_AB_LONG)) {
                json.put(ELEMENT_FORMAT, ELEMENT_INT64);
            } else {
                json.put(ELEMENT_FORMAT, ELEMENT_INT32);
            }
        } else if (ELEMENT_NUMBER.equals(convertType)) {
            if (toString.equals(ELEMENT_SHORT) || toString.equals(ELEMENT_AB_SHORT)) {
                json.put(ELEMENT_FORMAT, ELEMENT_INT32);
            } else {
                json.put(ELEMENT_FORMAT, ELEMENT_DOUBLE);
            }
        }
        jsonDoc.put(field.getSimpleName().toString(), json);
        return jsonDoc;
    }
}
