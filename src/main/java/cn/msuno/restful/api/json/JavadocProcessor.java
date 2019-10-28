package cn.msuno.restful.api.json;

import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_API;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DOLL;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DOT;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_JSON;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_JSON_RES;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_START;
import static cn.msuno.restful.api.json.JavadocUtils.JAVADOC_RESOURCE_SUFFIX;
import static cn.msuno.restful.api.json.JavadocUtils.PACKAGES_OPTION;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.msuno.restful.api.annotation.EnableRestfulApi;

public class JavadocProcessor extends AbstractProcessor {
    
    private static final Set<TypeElement> controller = new HashSet<>();
    private static final Set<TypeElement> other = new HashSet<>();
    private static final Set<Element> alreadyProcessed = new HashSet<>();
    private static Set<String> buildPath;
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        JavadocBuilder jsonJavadocBuilder = new JavadocBuilder(processingEnv);
        final Map<String, String> options = processingEnv.getOptions();
        final String packagesOption = options.get(PACKAGES_OPTION);
        // 保留与该谓词匹配的类的Javadoc
        final PackageFilter packageFilter = packagesOption == null ? new PackageFilter() : new PackageFilter(packagesOption);
        
        // 如果为所有软件包保留Javadoc，则@RetainJavadoc注释是多余的。
        // 否则，请确保不管程序包如何，带注释的类都保留了其Javadoc。
        if (!packageFilter.allowAllPackages()) {
            for (TypeElement annotation : annotations) {
                if (isRetainJavadocAnnotation(annotation)) {
                    for (Element e : roundEnvironment.getElementsAnnotatedWith(annotation)) {
                        generateJavadoc(e);
                    }
                }
            }
        }
        
        // 扫描所有class
        for (Element e : roundEnvironment.getRootElements()) {
            if (packageFilter.test(e)) {
                generateJavadoc(e);
            }
        }
        
        //没有restful注解不编译javadoc
        if (Objects.isNull(buildPath)) {
            return false;
        }
        
        // 构建controller json
        build(controller, jsonJavadocBuilder,ELEMENT_JSON, ELEMENT_API);
        
        // 构建非controller json
        build(other, jsonJavadocBuilder, ELEMENT_JSON_RES, null);
        
        return false;
    }
    
    private  void build(Set<TypeElement> list, JavadocBuilder javadocBuilder, String path,  String api){
        for (TypeElement e : list) {
            // 注解中设计到的路径才编译javadoc
            if (!hasContains(e.getQualifiedName().toString())) {
                continue;
            }
            // 不重复编译
            if (!alreadyProcessed.add(e)) {
                continue;
            }
            // 编译class的javadoc
            JSONObject javadoc = javadocBuilder.getClassJavadocAsJsonOrNull(e, api);
            if (null != javadoc && javadoc.size() > 0) {
                try {
                    outputJsonDoc(e, javadoc, path);
                } catch (IOException ex) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Javadoc retention failed; " + ex, e);
                }
            }
        }
    }
    
    /**
     * 是否在编译路径之下
     * @param key 编译文件全路径
     * @return  true / false
     */
    private boolean hasContains(String key) {
        if (buildPath.isEmpty()) {
            return true;
        }
        for (String str : buildPath) {
            if (key.startsWith(str)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取JavaClass JavaInterface JavaEnum
     * @param element Java元素
     */
    private void generateJavadoc(Element element) {
        ElementKind kind = element.getKind();
        EnableRestfulApi restfulApi = element.getAnnotation(EnableRestfulApi.class);
        if (!Objects.isNull(restfulApi)) {
            if (Objects.isNull(buildPath)) {
                buildPath = new HashSet<>();
            }
            buildPath.addAll(Arrays.asList(restfulApi.value()));
        }
        if (kind == ElementKind.CLASS || kind == ElementKind.INTERFACE || kind == ElementKind.ENUM) {
            generateJavadocForClass(element);
        }
        for (Element enclosed : element.getEnclosedElements()) {
            generateJavadoc(enclosed);
        }
    }
    
    /**
     * 获取是restful类的class和非restful类的class
     * @param element   class元素
     */
    private void generateJavadocForClass(Element element) {
        TypeElement classElement = (TypeElement) element;
        RequestMapping requestMapping = classElement.getAnnotation(RequestMapping.class);
        RestController restController = classElement.getAnnotation(RestController.class);
        if (null == requestMapping || null == restController) {
            other.add(classElement);
        } else {
            controller.add(classElement);
        }
    }
    
    /**
     * 输出json文件到classpath
     * @param classElement  class元素
     * @param classJson     class json数据
     * @param path          输出路径
     * @throws IOException  异常
     */
    private void outputJsonDoc(TypeElement classElement, JSONObject classJson, String path) throws IOException {
        String jsonString = classJson.toString();
        FileObject resource = createJavadocResourceFile(classElement, path);
        try (OutputStream os = resource.openOutputStream()) {
            os.write(jsonString.getBytes(UTF_8));
        }
    }
    
    /**
     * 获取输入资源
     * @param classElement  class元素
     * @param path          输出路径
     * @return              返回文件对象
     * @throws IOException  异常
     */
    private FileObject createJavadocResourceFile(TypeElement classElement, String path) throws IOException {
        PackageElement packageElement = getPackageElement(classElement);
        String packageName = packageElement.getQualifiedName().toString();
        String relativeName = packageName + ELEMENT_DOT + getClassName(classElement) + JAVADOC_RESOURCE_SUFFIX;
        return processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, path, relativeName);
    }
    
    /**
     * 获取包元素
     * @param element   元素
     * @return          包元素
     */
    private static PackageElement getPackageElement(Element element) {
        if (element instanceof PackageElement) {
            return (PackageElement) element;
        }
        return getPackageElement(element.getEnclosingElement());
    }
    
    /**
     * 获取class name
     * @param typeElement   class元素
     * @return              class name
     */
    private static String getClassName(TypeElement typeElement) {
        String typeName = typeElement.getQualifiedName().toString();
        String packageName = getPackageElement(typeElement).getQualifiedName().toString();
        if (!packageName.isEmpty()) {
            typeName = typeName.substring(packageName.length() + 1);
            typeName = typeName.replace(ELEMENT_DOT, ELEMENT_DOLL);
        }
        return typeName;
    }
    
    /**
     * 是否有RetainJavadoc注解
     * @param annotation    class元素
     * @return    true / false
     */
    private static boolean isRetainJavadocAnnotation(TypeElement annotation) {
        return annotation.getQualifiedName().toString().equals(RetainJavadoc.class.getName())
                || annotation.getAnnotation(RetainJavadoc.class) != null;
    }
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
    
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(ELEMENT_START);
    }
    
    @Override
    public Set<String> getSupportedOptions() {
        return Collections.singleton(PACKAGES_OPTION);
    }
    
}
