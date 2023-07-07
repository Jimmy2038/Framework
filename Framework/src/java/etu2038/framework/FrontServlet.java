/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu2038.framework;

import etu2038.framework.AnnotationFonction;
import etu2038.framework.Mapping;
import etu2038.framework.ModelView;
import etu2038.framework.Parametre;
import etu2038.framework.UploadFile;
import etu2038.framework.Scope;
import etu2038.framework.Authentification;
import etu2038.framework.ApiRest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.Part;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.MultipartConfig;

/**
 *
 * @author Best
 */
@MultipartConfig()
public class FrontServlet extends HttpServlet {
    Map<String, Mapping> MappingUrls = new HashMap<>();
    Map<String, Object> singleton = new HashMap<>();
    Map<String, Object> session = new HashMap<>();
    String sessionName;
    String sessionProfile;

    @Override
    public void init() throws ServletException {
        // super.init();
        sessionName = this.getInitParameter("sessionName");
        sessionProfile = this.getInitParameter("sessionProfile");
        String p = getInitParameter("test");
        try {
            List<Class<?>> annoted_classes = AnnotationFonction.getClassesWithAnnotation2(AnnotationController.class,
                    p);
            for (Class<?> c : annoted_classes) {
                Method[] methods = c.getDeclaredMethods();
                for (Method m : methods) {
                    if (m.isAnnotationPresent(etu2038.framework.Url.class)) {
                        Mapping mapping = new Mapping();
                        mapping.setClassName(c.getName());
                        mapping.setMethod(m.getName());
                        etu2038.framework.Url app = m.getAnnotation(etu2038.framework.Url.class);
                        String url = app.nom();
                        this.MappingUrls.put(url, mapping);
                    }
                }
                for (int i = 0; i < annoted_classes.size(); i++) {
                    if (annoted_classes.get(i).isAnnotationPresent(Scope.class)) {
                        singleton.put(annoted_classes.get(i).getName(), null);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("eto tompoko = " + e.getMessage());
            e.printStackTrace();
        } // To change body of generated methods, choose Tools | Templates.
    }

    public String getSetter(String argument) {
        String setter = "";
        String temp = argument.substring(0, 1).toUpperCase();
        setter = temp + argument.substring(1);
        System.out.println(setter);
        return "set" + setter;
    }

    private UploadFile fileTraitement(Collection<Part> files, Field field) {
        UploadFile file = new UploadFile();
        String name = field.getName();
        boolean exists = false;
        String filename = null;
        Part filepart = null;
        for (Part part : files) {
            if (part.getName().equals(name)) {
                filepart = part;
                exists = true;
                break;
            }
        }
        try (InputStream io = filepart.getInputStream()) {
            ByteArrayOutputStream buffers = new ByteArrayOutputStream();
            byte[] buffer = new byte[(int) filepart.getSize()];
            int read;
            while ((read = io.read(buffer, 0, buffer.length)) != -1) {
                buffers.write(buffer, 0, read);
            }
            file.setNom(this.getFileName(filepart));
            file.setBytes(buffers.toByteArray());
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] parts = contentDisposition.split(";");
        for (String partStr : parts) {
            if (partStr.trim().startsWith("filename"))
                return partStr.substring(partStr.indexOf('=') + 1).trim().replace("\"", "");
        }
        return null;
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            String query = request.getQueryString();
            String urlString = request.getRequestURL().toString();
            String url = request.getRequestURI().substring(request.getContextPath().length() + 1);
            if (this.MappingUrls.containsKey(url)) {
                out.print(urlString);
                String className = this.MappingUrls.get(url).getClassName();
                String method = this.MappingUrls.get(url).getMethod();
                Class<?> c = Class.forName(className);
                Method m = null;

                Method[] met = c.getDeclaredMethods();
                for (int i = 0; i < met.length; i++) {
                    if (met[i].getName().equals(method)) {
                        m = met[i];
                        break;
                    }
                }

                if (m.isAnnotationPresent(Authentification.class)) {
                    Authentification a = (Authentification) m.getAnnotation(Authentification.class);
                    if ( request.getSession().getAttribute(sessionName) != null) {
                        System.out.println(request.getSession().getAttribute(sessionName));
                        if ((a.profile().isEmpty() == false
                                && !a.profile().equals(request.getSession().getAttribute(sessionProfile)))) {
                            throw new Exception("privilege non accorder");
                        }
                    } else {
                        throw new Exception("pas de session");
                    }
                }

                Field[] field = c.getDeclaredFields();
                Object o = null;
                if (c.isAnnotationPresent(Scope.class)) {
                    if (this.singleton.containsKey(c.getName()) && this.singleton.get(c.getName()) != null) {
                        o = this.singleton.get(c.getName());
                    } else {
                        o = c.getConstructor().newInstance();
                        this.singleton.replace(c.getName(), null, o);
                    }
                } else {
                    o = c.getConstructor().newInstance();
                }
                System.out.println(o);
                Enumeration<String> enu = request.getParameterNames();
                List<String> liste = Collections.list(enu);
                for (int i = 0; i < field.length; i++) {
                    String fieldtab = field[i].getName() + ((field[i].getType().isArray()) ? "[]" : "");
                    for (int j = 0; j < liste.size(); j++) {
                        Method me = c.getDeclaredMethod(this.getSetter(field[i].getName()), field[i].getType());
                        if (liste.get(j).trim().equals(fieldtab.trim())) {
                            if (field[i].getType().isArray() == false) {
                                String str = request.getParameter(field[i].getName());
                                if (field[i].getType() == java.util.Date.class) {
                                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                    java.util.Date obj = s.parse(str);
                                    me.invoke(o, obj);
                                } else if (field[i].getType() == java.sql.Date.class) {
                                    java.sql.Date obj = java.sql.Date.valueOf(str);
                                    me.invoke(o, obj);
                                } else {
                                    Object obj = field[i].getType().getConstructor(String.class).newInstance(str);
                                    me.invoke(o, obj);
                                }
                            } else {
                                String[] string = request.getParameterValues(fieldtab);
                                me.invoke(o, (Object) string);
                            }
                        }
                    }
                }
                Parameter[] para = m.getParameters();
                Object[] parametres = new Object[para.length];
                for (int i = 0; i < para.length; i++) {
                    if (para[i].isAnnotationPresent(Parametre.class)) {
                        Parametre pa = para[i].getAnnotation(Parametre.class);
                        String p = para[i].getAnnotation(Parametre.class).parametre()
                                + ((para[i].getType().isArray()) ? "[]" : "");
                        for (int j = 0; j < liste.size(); j++) {
                            if (liste.get(j).trim().equals(p.trim())) {
                                if (para[i].getType().isArray() == false) {
                                    String str = request.getParameter(pa.parametre());
                                    if (para[i].getType() == java.util.Date.class) {
                                        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                        java.util.Date obj = s.parse(str);
                                        parametres[i] = obj;
                                    } else if (para[i].getType() == java.sql.Date.class) {
                                        java.sql.Date obj = java.sql.Date.valueOf(str);
                                        parametres[i] = obj;
                                    } else {
                                        Object obj = para[i].getType().getConstructor(String.class).newInstance(str);
                                        parametres[i] = obj;
                                    }
                                } else {
                                    String[] string = request.getParameterValues(p);
                                    parametres[i] = string;
                                }
                            }

                        }
                    }
                }
                try {
                    Collection<Part> files = request.getParts();
                    for (Field f : field) {
                        if (f.getType() == etu2038.framework.UploadFile.class) {
                            Method meth = c.getMethod(this.getSetter(f.getName()), f.getType());
                            Object object = this.fileTraitement(files, f);
                            meth.invoke(o, object);
                        }
                    }
                } catch (Exception e) {
                }
                Object mv = null;
                Enumeration<String> enume = request.getSession().getAttributeNames();
                List<String> listes = Collections.list(enume);
                for (int i = 0; i < listes.size(); i++) {
                    session.put(listes.get(i),request.getSession().getAttribute(listes.get(i)));
                }
                Method ses = c.getDeclaredMethod("setSession",HashMap.class);
                ses.invoke(o, session);  
                mv = m.invoke(o, parametres);
                if (mv instanceof ModelView) {
                    ModelView model = (ModelView) mv;
                    HashMap<String, Object> data = model.getData();
                    HashMap<String, Object> se = model.getSession();
                    for (Map.Entry<String, Object> zavatra : data.entrySet()) {
                        request.setAttribute(zavatra.getKey(), zavatra.getValue());  
                    }
                    for (Map.Entry<String, Object> zavatra : se.entrySet()) {
                        request.getSession().setAttribute(zavatra.getKey(), zavatra.getValue());  
                    }
                    if (model.isGson()) {
                        response.setContentType("application/json");
                        out.print(new com.google.gson.Gson().toJson(data));
                    }else{
                        if (model.isInvalidateSession()) {
                            request.getSession().invalidate();
                        }
                       for (String string : model.getRemoveSession()) {
                            request.getSession().removeAttribute(string);
                       }
                        RequestDispatcher dispat = request.getRequestDispatcher(model.getView());
                        dispat.forward(request, response);
                    }
                }
                else if(m.isAnnotationPresent(ApiRest.class)){
                    response.setContentType("application/json");
                        out.print(new com.google.gson.Gson().toJson(mv));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
