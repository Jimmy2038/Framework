/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu002038.framework.utilitaire;

/**
 *
 * @author jimmy
 */
public class Mapping {
    String className;
    String method;

    public Mapping(String className, String method) {
        setClassName(className);
        setMethod(method);
    }

    
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String Method) {
        this.method = Method;
    }
    
    
}
