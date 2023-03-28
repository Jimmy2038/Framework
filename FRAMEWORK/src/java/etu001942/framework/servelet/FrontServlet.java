/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu001942.framework.servelet;


import etu001942.framework.Mapping;
import etu001942.framework.models.MethodAnnotation;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrontServlet extends HttpServlet {

     HashMap<String,Mapping> mappingUrls;
     public void init()throws ServletException{
        mappingUrls = new HashMap<String,Mapping>();
        String path = "etu001942/framework/models";
        try {
            Vector<MethodAnnotation> list = MethodAnnotation.getAnnotedMethods(path);
            for(MethodAnnotation me : list){
                Mapping map_indice = new Mapping(me.getMethod().getDeclaringClass().getSimpleName(), me.getMethod().getName());
                mappingUrls.put(me.getAnnotation().name(), map_indice);
            }
        } catch (Exception ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter out = response.getWriter();
        out.println("Servlet : Front Servlet");
        out.println("");
        out.println("Context Path :"+request.getContextPath());
        out.println("");
        out.println("URL :"+request.getRequestURL());
        out.println("");
        out.println("Parametre :");
        Enumeration<String> liste = request.getParameterNames();
        for (Map.Entry<String, Mapping> entry : mappingUrls.entrySet()) {
            out.print("<br>");
            out.println("Url :  "+entry.getKey() + " ,  Class :" +entry.getValue().getClassName() + " , Method : " + entry.getValue().getMethod());
            
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

  
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

  

}
