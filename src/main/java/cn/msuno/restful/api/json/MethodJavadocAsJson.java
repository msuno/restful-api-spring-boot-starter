package cn.msuno.restful.api.json;

import static cn.msuno.restful.api.json.JavadocBuilder.hasDeprecated;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_ABSOLUTE_TYPE;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_API;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_BODY;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_CONSUMES;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DEFAULT;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DEFINITIONS;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DEPRECATED;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DESCRIPTION;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_EMPTY;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_IN;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_METHOD;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_NAME;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_PARAM;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_PARAM_TYPE;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_PATH;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_PRODUCES;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_QUERY;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_REQUIRED;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_RESPONSES;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_SUMMARY;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_TYPE;
import static cn.msuno.restful.api.json.JavadocUtils.convertType;
import static cn.msuno.restful.api.json.JavadocUtils.isBlank;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MethodJavadocAsJson implements JavadocAsJson {
    private final ProcessingEnvironment processingEnv;
    
    MethodJavadocAsJson(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }
    
    @Override
    public JSONObject apply(Element method, String api) {
        if (!(method instanceof ExecutableElement)) {
            return null;
        }
        String methodJavadoc = processingEnv.getElementUtils().getDocComment(method);
        String methodName = method.getSimpleName().toString();
        if (isBlank(methodJavadoc)) {
            methodJavadoc = methodName;
        }
        JSONObject jsonDoc = new JSONObject();
        if (ELEMENT_API.equals(api)) {
            jsonDoc.putAll(getMappingParam(method));
        }
        JSONObject javaDoc = JavadocUtils.javaDoc(methodJavadoc);
        jsonDoc.put(ELEMENT_PARAM_TYPE, getParamErasures((ExecutableElement) method, api, javaDoc.getJSONObject(ELEMENT_PARAM)));
        TypeMirror returnType = ((ExecutableElement) method).getReturnType();
        String responseString = returnType.toString();
        jsonDoc.put(ELEMENT_ABSOLUTE_TYPE, responseString);
        jsonDoc.put(ELEMENT_DEPRECATED, hasDeprecated(method));
        jsonDoc.put(ELEMENT_DESCRIPTION, javaDoc.getString(ELEMENT_DESCRIPTION));
        jsonDoc.put(ELEMENT_SUMMARY, javaDoc.getOrDefault(ELEMENT_SUMMARY, methodName));
        jsonDoc.put(ELEMENT_RESPONSES, buildResponse(responseString));
        return jsonDoc;
    }
    
    private JSONObject buildResponse(String responseType){
        JSONObject res = new JSONObject();
        JSONObject code201 = new JSONObject();
        code201.put(ELEMENT_DESCRIPTION, "Created");
        res.put("201", code201);
        JSONObject code401 = new JSONObject();
        code401.put(ELEMENT_DESCRIPTION, "Unauthorized");
        res.put("401", code401);
        JSONObject code403 = new JSONObject();
        code403.put(ELEMENT_DESCRIPTION, "Forbidden");
        res.put("403", code403);
        JSONObject code404 = new JSONObject();
        code404.put(ELEMENT_DESCRIPTION, "Not Found");
        res.put("404", code404);
        JSONObject code200 = new JSONObject();
        code200.put(ELEMENT_DESCRIPTION, "OK");
        JSONObject scheme = new JSONObject();
        if (!responseType.equals(JavadocUtils.convertType(responseType))) {
            scheme.put("type", JavadocUtils.convertType(responseType));
        } else {
            scheme.put("$ref", ELEMENT_DEFINITIONS + JavadocUtils.getSimpleName(responseType));
        }
        code200.put("schema", scheme);
        res.put("200", code200);
        return res;
    }
    
    private JSONObject getMappingParam(Element elementMethod) {
        JSONObject jsonDoc = new JSONObject();
        String[] path = null;
        String[] produces = null;
        String[] consumes = null;
        RequestMethod[] method = null;
        RequestMapping requestMapping = elementMethod.getAnnotation(RequestMapping.class);
        PostMapping postMapping = elementMethod.getAnnotation(PostMapping.class);
        GetMapping getMapping = elementMethod.getAnnotation(GetMapping.class);
        DeleteMapping deleteMapping = elementMethod.getAnnotation(DeleteMapping.class);
        PutMapping putMapping = elementMethod.getAnnotation(PutMapping.class);
        if (null != requestMapping) {
            path = requestMapping.value();
            produces = requestMapping.produces();
            consumes = requestMapping.consumes();
            method = requestMapping.method();
            if (method.length < 1) {
                method = new RequestMethod[]{RequestMethod.POST,RequestMethod.GET,RequestMethod.DELETE,RequestMethod.PUT,
                        RequestMethod.PATCH, RequestMethod.OPTIONS};
            }
        }
        if (null != postMapping) {
            path = postMapping.value();
            produces = postMapping.produces();
            consumes = postMapping.consumes();
            method = new RequestMethod[]{RequestMethod.POST};
        }
        if (null != getMapping) {
            path = getMapping.value();
            produces = getMapping.produces();
            consumes = getMapping.consumes();
            method = new RequestMethod[]{RequestMethod.GET};
        }
        if (null != deleteMapping) {
            path = deleteMapping.value();
            produces = deleteMapping.produces();
            consumes = deleteMapping.consumes();
            method = new RequestMethod[]{RequestMethod.DELETE};
        }
        if (null != putMapping) {
            path = putMapping.value();
            produces = putMapping.produces();
            consumes = putMapping.consumes();
            method = new RequestMethod[]{RequestMethod.PUT};
        }
        if (null == path || path.length < 1) {
            return jsonDoc;
        }
        jsonDoc.put(ELEMENT_PATH, path);
        jsonDoc.put(ELEMENT_PRODUCES, produces);
        jsonDoc.put(ELEMENT_CONSUMES, consumes);
        jsonDoc.put(ELEMENT_METHOD, method);
        return jsonDoc;
    }
    
    /**
     * 循环构建多个参数
     */
    private JSONArray getParamErasures(ExecutableElement executableElement, String api, JSONObject methodsDoc) {
        final JSONArray jsonValues = new JSONArray();
        for (VariableElement parameter : executableElement.getParameters()) {
            jsonValues.add(getParams(parameter, api, methodsDoc));
        }
        return jsonValues;
    }
    
    /**
     * 构建接口参数
     */
    private JSONObject getParams(VariableElement variableElement, String api, JSONObject methodsDoc) {
        TypeMirror typeMirror = variableElement.asType();
        JSONObject json = new JSONObject();
        String name = variableElement.getSimpleName().toString();
        if (ELEMENT_API.equals(api)) {
            RequestParam requestParam = variableElement.getAnnotation(RequestParam.class);
            PathVariable pathVariable = variableElement.getAnnotation(PathVariable.class);
            RequestBody requestBody = variableElement.getAnnotation(RequestBody.class);
            String defaultValue = ELEMENT_EMPTY;
            boolean required = false;
            String in = ELEMENT_QUERY;
            if (null != requestParam) {
                name = requestParam.value();
                defaultValue = requestParam.defaultValue();
                required = requestParam.required();
            }
            if (null != pathVariable) {
                name = pathVariable.value();
                required = pathVariable.required();
                in = ELEMENT_PATH;
            }
            if (null != requestBody) {
                required = requestBody.required();
                in = ELEMENT_BODY;
            }
            json.put(ELEMENT_IN, in);
            json.put(ELEMENT_DEFAULT, defaultValue);
            json.put(ELEMENT_REQUIRED, required);
        }
        json.put(ELEMENT_TYPE, convertType(typeMirror.toString()));
        json.put(ELEMENT_DESCRIPTION, methodsDoc.getOrDefault(name,
                methodsDoc.getOrDefault(variableElement.getSimpleName().toString(), name)).toString());
        json.put(ELEMENT_NAME, name);
        return json;
    }
}
