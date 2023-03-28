/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu001942.framework.models;

import etu001942.framework.annotation.Url;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Vector;

/**
 *
 * 
 */
public class MethodAnnotation {
    Method method;
    Url annotationMethode;
    
    public Url getAnnotation() {
        return annotationMethode;
    }
    public Method getMethod(){
        return method;
    }
    
    private MethodAnnotation(Method m,Url a) {
        this.annotationMethode = a;
        this.method = m;
    }
    
    @SuppressWarnings("UseOfObsoleteCollectionType")
    public static Vector<MethodAnnotation> getAnnotedMethods(String path) throws Exception{
        Vector<MethodAnnotation> listmethodAnnot = new Vector();       
        ClassLoader classLoder = Thread.currentThread().getContextClassLoader();
        File pkg = new File(classLoder.getResources(path).nextElement().getFile());
        if (pkg.exists()) {
            for (File file : pkg.listFiles()) {
                if (!file.isDirectory()) {
                    if ( file.getName().endsWith(".class") ) {                    
                        String p = path.replace("/",".");
                        Class cl = Class.forName(p+"."+file.getName().replace(".class",""));
                        for ( Method mt : cl.getDeclaredMethods()) {
                            if (mt.isAnnotationPresent(Url.class)) {
                                Url annotation = (Url) mt.getAnnotation(Url.class);
                                MethodAnnotation ma = new MethodAnnotation(mt,annotation);
                                listmethodAnnot.addElement(ma);
                            }
                        }
                    }
                }	
            }            
        } else{
            throw new Exception("not fond");
        }
        return listmethodAnnot;
    }
}
