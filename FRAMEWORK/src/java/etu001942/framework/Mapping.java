/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu001942.framework;

import java.lang.reflect.Method;

/**
 *
 * @author Tafitasoa-P15B-140
 */
public class Mapping {
    String className;
    String method;
    
    public String getClassName() {
        return this.className;
    }
    public void setClassName(String classe){
        this.className = classe;
    }
    public String getMethod() {
        return this.method;
    }
    public void setMethod(String me){
        this.method = me;
    }
    
    public Mapping(String cl,String met){
        this.setClassName(cl);
        this.setMethod(met);
    }
}
