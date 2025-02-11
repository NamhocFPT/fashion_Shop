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
import jakarta.servlet.http.HttpSession;
import java.util.Vector;
import model.DAOUsers;
import java.util.logging.Logger;

/**
 *
 * @author HP
 */
@WebServlet(name="LoginController", urlPatterns={"/LoginController"})
public class LoginController extends HttpServlet {
   
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
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
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String sql = "SELECT * FROM [dbo].[Users]";
            DAOUsers dao = new DAOUsers();
            Vector<Users> vector = dao.getUsers(sql);
            boolean checkLogin = false;
            for (Users u : vector) {
                logger.info("Database Username: " + u.getUsername() + ", Database Password: " + u.getPassword());
                logger.info("Input Username: " + username + ", Input Password: " + password);
                logger.info("UserID: " + u.getUserID() + "Username: " + u.getUsername() + "Password" + u.getPassword() + "Role: " + u.getRoleID());
                if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                    checkLogin = true;
                    if (u.getRoleID() == 3) {
                        RequestDispatcher rd = request.getRequestDispatcher("HomePage.jsp");
                        HttpSession session = request.getSession(true);
                        session.setAttribute("username", username);
                        session.setAttribute("password", password);
                        session.setAttribute("userID", u.getUserID());
                        logger.info("User ID: " + u.getUserID());
                        session.setAttribute("role", u.getRoleID());
                        rd.forward(request, response);
                    } else if (u.getRoleID() == 2) {
                        RequestDispatcher rd = request.getRequestDispatcher("HomePageEmp.jsp");
                        HttpSession session = request.getSession(true);
                        session.setAttribute("username", username);
                        session.setAttribute("password", password);
                        session.setAttribute("userID", u.getUserID());
                        logger.info("User ID: " + u.getUserID());
                        session.setAttribute("role", u.getRoleID());
                        rd.forward(request, response);
                    } else if (u.getRoleID() == 1) {
                        RequestDispatcher rd = request.getRequestDispatcher("HomePageAdmin.jsp");
                        HttpSession session = request.getSession(true);
                        session.setAttribute("username", username);
                        session.setAttribute("password", password);
                        session.setAttribute("userID", u.getUserID());
                        logger.info("User ID: " + u.getUserID());
                        session.setAttribute("role", u.getRoleID());
                        rd.forward(request, response);
                    }
                }
            }

            if (!checkLogin) {
                RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu sai");
                rd.forward(request, response);
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
