/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import annotation.Url;

/**
 *
 * @author rindra
 */
public class Dept {
    String mat;
    String tay;
    
    @Url(name="dept-findAll")
    public Dept[] findAll(){
        return null;
    }
    
    @Url(name="dept-addDept")
    public void addDept(){
        
    }
}
