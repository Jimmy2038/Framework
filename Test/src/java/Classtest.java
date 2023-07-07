/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 package test;

import etu2038.framework.ModelView;
import etu2038.framework.Parametre;
import etu2038.framework.Scope;
import etu2038.framework.UploadFile;

import java.util.HashMap;

import etu2038.framework.AnnotationController;
import etu2038.framework.Authentification;
import etu2038.framework.ApiRest;
import etu2038.framework.Url;

/**
 *
 * @author Best
 */
@Scope()
@AnnotationController
public class Classtest {
    String nom;
    Integer phone;
    java.util.Date utilDate;
    java.sql.Date sqlDate;
    String[] genre;
    UploadFile file;
    HashMap<String,Object> session = new HashMap<>();

    public HashMap<String, Object> getSession() {
        return session;
    }
    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setPhone(Integer phone) {
        this.phone = phone;
    }
    public void setUtilDate(java.util.Date utilDate) {
        this.utilDate = utilDate;
    }
    public void setSqlDate(java.sql.Date sqlDate){
        this.sqlDate = sqlDate;
    }
    public void setGenre(String[] genre){
        this.genre = genre;
    }
    public void setFile(UploadFile file) {
        this.file = file;
    }
    public String getNom() {
        return nom;
    }
    public Integer getPhone() {
        return phone;
    }
    public java.util.Date getUtilDate() {
        return utilDate;
    }
    public java.sql.Date getSqlDate(){
        return sqlDate;
    }
    public String[] getGenre(){
        return genre;
    }
    public UploadFile getFile() {
        return file;
    }

    @Url(nom="test")
    public ModelView view(){
        ModelView model = new ModelView();
        model.addSession("SessionTEst", false);
        this.getSession().forEach((key, value) -> {
            System.out.println("Clé : " + key + ", Valeur : " + value);
        });
        model.setView("index.jsp");
        return model;
    } 

    @Url(nom="getValues")
    public ModelView getValues() {
        ModelView model = new ModelView();  
        model.setView("Test.jsp");  
        return model;
    }
    
    @Url(nom="parametre")
    public ModelView getParametre(@Parametre(parametre = "param") Integer i){
        ModelView model = new ModelView();
        System.out.println(i);
        model.setView("index.jsp");
        return model;
    }
  
    @Authentification()
    @Url(nom="getFile")
    public ModelView getFiles(){
        ModelView model = new ModelView();
        model.setView("index.jsp");
        return model;
    }

    @Authentification(profile = "admin")
    @Url(nom="findAll")
    public ModelView findAll(){ 
        ModelView model = new ModelView();
        model.setView("index.jsp");
        return model;
    }

    @Url(nom="login")
    public ModelView login(){
        ModelView model = new ModelView();
        model.addSession("isConnected", true);
        model.addSession("profile", "admin");
        model.setView("index.jsp");
        return model;
    }
    
    @Url(nom="json")
    public ModelView getJson(){ 
        ModelView model = new ModelView();
        model.setGson(true);
        model.addItem("data","data");
        model.setView("index.jsp");
        return model;
    }      
    
    @ApiRest()
    @Url(nom="restApi")
    public Classtest getApiRest(){ 
        Classtest c = new Classtest();
        c.setNom("Mendrika");
        c.setPhone(12345);
        return c;
    } 

     @Url(nom="logout")
    public ModelView logout(){
        ModelView model = new ModelView();
        model.addToRemove("isConnected","profile");
        // model.setInvalidateSession(true);
        model.setView("index.jsp");
        return model;
    }
    

}
