/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu2038.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Best
 */
public class ModelView {
    String view;
    HashMap<String, Object> session = new HashMap<>();
    HashMap<String, Object> data = new HashMap<>();
    Boolean gson = false;
    Boolean invalidateSession = false;
    List<String> removeSession = new ArrayList<>();
    
    public void addToRemove(String ...data){
        for (int i = 0; i < data.length; i++) {
            this.removeSession.add(data[i]);
        }   
    }
    public void setGson(Boolean gson) {
        this.gson = gson;
    }
    public Boolean isGson(){
        return this.gson;
    }    
    public Boolean isInvalidateSession(){
        return this.invalidateSession;
    }   
    public void setInvalidateSession(Boolean invalidateSession) {
        this.invalidateSession = invalidateSession;
    }
    public void setRemoveSession(List<String> removeSession) {
        this.removeSession = removeSession;
    }
    public List<String> getRemoveSession() {
        return removeSession;
    }
    public String getView() {
        return view;
    }    
    public void setView(String view) {
        this.view = view;
    }
    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }
    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }
    public HashMap<String, Object> getSession() {
        return session;
    }
    public HashMap<String, Object> getData() {
        return data;
    }
    public void addSession(String key,Object object) {
        this.session.put(key,object);
    }
    public void addItem(String key , Object object) {
        this.getData().put(key, object);
    }
    
}
