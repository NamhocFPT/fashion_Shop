/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import entity.Users;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Vector;
import model.DAOUsers;

/**
 *
 * @author HP
 */
@WebServlet(name="UsersController", urlPatterns={"/UsersController"})
public class UsersController extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String service = request.getParameter("service");
            DAOUsers daoUs = new DAOUsers();
            if (service.equals("displayUsers")) {
                String sql = "SELECT * FROM Users";
                Vector<Users> userList = daoUs.getUsers(sql);
                RequestDispatcher rd = request.getRequestDispatcher("DisplayUsers.jsp");
                request.setAttribute("userList", userList);
                rd.forward(request, response);
            }
            
            if (service.equals("updateUser")) {
                String submit = request.getParameter("submit");
                if (submit == null) {
                    //System.out.println("Hello ");
                    String UserID = request.getParameter("UserID");
                    RequestDispatcher rd = request.getRequestDispatcher("UpdateUsers.jsp");
                    Users u = daoUs.getUserByUserId(Integer.parseInt(UserID));
                    request.setAttribute("user", u);
                    rd.forward(request, response);
                    
                } else {
                    //System.out.println("Hello world");
                    int UserId = Integer.parseInt(request.getParameter("UserId"));
                    String Username = request.getParameter("Username");
                    String Password = request.getParameter("Password");
                    int RoleId = Integer.parseInt(request.getParameter("RoleId"));
                    //System.out.println(UserId + " " + Username + " " + Password + " " + RoleId);
                    int n = daoUs.updateUser(new Users(UserId, Username, Password, RoleId));
                    response.sendRedirect("UsersController?service=displayUsers");
                }
            }
            
            if (service.equals("deleteUser")) {
                int UserID = Integer.parseInt(request.getParameter("UserID"));
                int n = daoUs.deleteUser(UserID);
                response.sendRedirect("UsersController?service=displayUsers");
            }
            
            if (service.equals("addUser")) {
                String submit = request.getParameter("submit");
                if (submit == null) {
                    response.sendRedirect("AddUser.jsp");
                } else {
                    String Username = request.getParameter("Username");
                    String Password = request.getParameter("Password");
                    int RoleId = Integer.parseInt(request.getParameter("RoleId"));
                    daoUs.addUser(new Users(Username, Password, RoleId));
                    response.sendRedirect("UsersController?service=displayUsers");
                }
            }
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
