package cn.msuno.restful.api.json;

import javax.lang.model.element.Element;

import com.alibaba.fastjson.JSONObject;

public interface JavadocAsJson {
    JSONObject apply(Element e, String api);
}
