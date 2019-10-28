package cn.msuno.restful.api.json;

import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_DOLL;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_EMPTY;
import static cn.msuno.restful.api.json.JavadocUtils.ELEMENT_SYMBOL;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.QualifiedNameable;

class PackageFilter {
    
    private final Set<String> rootPackages = new HashSet<>();
    private final Set<String> packages = new HashSet<>();
    private final Set<String> negatives = new HashSet<>();
    
    PackageFilter() {
    }
    
    PackageFilter(String commaDelimitedPackages) {
        for (String pkg : commaDelimitedPackages.split(ELEMENT_SYMBOL)) {
            pkg = pkg.trim();
            if (!pkg.isEmpty()) {
                rootPackages.add(pkg);
            }
        }
        packages.addAll(rootPackages);
    }
    
    boolean test(Element element) {
        final String elementPackage = getPackage(element);
        
        if (negatives.contains(elementPackage)) {
            return false;
        }
        
        if (packages.isEmpty() || packages.contains(elementPackage)) {
            return true;
        }
        
        for (String p : rootPackages) {
            if (elementPackage.startsWith(p + ELEMENT_DOLL)) {
                // Element的包是包含的包的子包。
                packages.add(elementPackage);
                return true;
            }
        }
        
        negatives.add(elementPackage);
        return false;
    }
    
    private static String getPackage(Element e) {
        while (e.getKind() != ElementKind.PACKAGE) {
            e = e.getEnclosingElement();
            if (e == null) {
                return ELEMENT_EMPTY;
            }
        }
        return ((QualifiedNameable) e).getQualifiedName().toString();
    }
    
    boolean allowAllPackages() {
        return packages.isEmpty();
    }
}
