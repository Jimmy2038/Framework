package util;

import annotation.Url;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
public class Find_Annotation {
    
    public static List<AnnotatedMethod> findAnnotatedMethods() throws Exception {
        List<AnnotatedMethod> annotatedMethods = new ArrayList<>();
        List<String> classNames = getClassesInPackage("");
         for (String className : classNames) {
            char [] c = new char[className.length()-1];
            int co=0;
            for (int i=1 ; i <className.length();i++){
                c[co]=className.charAt(i);
                co++;
            }
          String nom_du_class =new String(c);
            Class<?> clazz = Class.forName(nom_du_class);
                    for (Method method : clazz.getDeclaredMethods()) {
                        Annotation annotation = method.getAnnotation(Url.class);
                        if (annotation != null) {
                            String annotationValue=method.getAnnotation(Url.class).name();
                            annotatedMethods.add(new AnnotatedMethod(clazz.getName(), method,annotationValue));
                        }
                    }
         }
        

    return annotatedMethods;
    }
        public static List<String> getClassesInPackage(String packageName) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<String> classNames = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File file = new File(resource.getFile());
            if (file.isDirectory()) {
                classNames.addAll(getClassesInDirectory(packageName, file));
            }
        }
        return classNames;
    }
    

    private static List<String> getClassesInDirectory(String packageName, File directory) {
        List<String> classNames = new ArrayList<>();
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classNames.addAll(getClassesInDirectory(packageName + "." + file.getName(), file));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                classNames.add(className);
            }
        }
        return classNames;
    }
    
    public static class AnnotatedMethod {
         String className;
        Method method;
        String value;

        public AnnotatedMethod(String className, Method method, String value) {
            this.className = className;
            this.method = method;
            this.value = value;
        }

        
        
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
        
        
        public String getClassName() {
            return className;
        }
        
        public Method getMethod() {
            return method;
        }
    }
}