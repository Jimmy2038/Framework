package etu002038.framework.servelet;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import etu002038.framework.utilitaire.AnnotationFinder;
import etu002038.framework.utilitaire.Mapping;
import etu002038.framework.utilitaire.ModelView;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;




/**
 *
 * @author jimmy
 */
@WebServlet(name = "FrontServlet", urlPatterns = {"/FrontServlet"})
public class FrontServlet extends HttpServlet { 
 
    

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    HashMap<String,Mapping>  mappinUrls;
    
    public void init() throws ServletException{
        mappinUrls=new HashMap<String,Mapping>();
        List<AnnotationFinder.AnnotatedMethod> annotatedMethods;
        try {
            annotatedMethods = AnnotationFinder.findAnnotatedMethods();
            for (AnnotationFinder.AnnotatedMethod method : annotatedMethods) {
           // out.println("Annotated method " + method.getMethod().getName() + " in class " + method.getClassName() +" value ="+method.getValue());
           Mapping mapping = new Mapping(method.getClassName(),method.getMethod().getName());
           mappinUrls.put(method.getValue(), mapping);
            }

        } catch (Exception ex) {
         //  out.println(ex.getMessage());
        }
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
//        out.println("Servlet : FrontServlet");
//        out.println("");
//        out.println("Context Path :"+request.getContextPath());
//        out.println("");
//        out.println("URL :"+request.getRequestURL());
//        out.println("");         
               String url=request.getRequestURI();
            String [] p  = url.split("/");
            String key=p[p.length-1];
             try {
               if(mappinUrls.containsKey(key)){
                   Mapping map = mappinUrls.get(key);
                   // out.println(map.getClassName());
                   
                    // Charger la classe par son nom
                    Class<?> maClasse = Class.forName(map.getClassName());

                    // Créer une instance de la classe
                    Object instance = maClasse.newInstance();

                    // Obtenir la méthode par son nom
                    Method maMethode = maClasse.getDeclaredMethod(map.getMethod());

                    // Appeler la méthode sur l'instance
                    Object resultat = maMethode.invoke(instance);
                   out.println(resultat.getClass());
//                    out.println(ModelView.class);

                    // Traiter le résultat
                    if(resultat.getClass()==ModelView.class){
                         ModelView mv = (ModelView) resultat;
                         // Récupérer le dispatcher
                      RequestDispatcher dispatcher = request.getRequestDispatcher(mv.getView());

                        // Envoyer la demande au dispatcher
                        dispatcher.forward(request, response);

                    }
                    }
               else { throw new Exception("No key "  + key + " in HashMap");}
                } catch (Exception e) {
                    // Gérer les exceptions
                    out.println(e.getMessage());
                }
               
               
           
       
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
         
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
