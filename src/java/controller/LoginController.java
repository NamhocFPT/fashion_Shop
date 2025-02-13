package controller;

import entity.Users;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;
import model.DAOUsers;
import java.util.logging.Logger;

@WebServlet(name="LoginController", urlPatterns={"/LoginController"})
public class LoginController extends HttpServlet {
   
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String sql = "SELECT * FROM [dbo].[Users]";
            DAOUsers dao = new DAOUsers();
            ArrayList<Users> vector = dao.getUsers(sql);
            boolean checkLogin = false;

            for (Users u : vector) {
                logger.info("Database Username: " + u.getUsername() + ", Database Password: " + u.getPassword());
                logger.info("Input Username: " + username + ", Input Password: " + password);
                logger.info("UserID: " + u.getUserID() + "Username: " + u.getUsername() + "Password" + u.getPassword() + "Role: " + u.getRoleID());

                if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                    checkLogin = true;
                    String targetPage = "";
                    if (u.getRoleID() == 3) {
                        targetPage = "HomePage.jsp";
                    } else if (u.getRoleID() == 2) {
                        targetPage = "HomePageEmp.jsp";
                    } else if (u.getRoleID() == 1) {
                        targetPage = "HomePageAdmin.jsp";
                    }
                    
                    // Handle session and redirection
                    setSessionAndRedirect(request, response, targetPage, username, password, u);
                    return; // exit the loop once logged in
                }
            }

            if (!checkLogin) {
                RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu sai");
                rd.forward(request, response);
            }
        }
    }

    private void setSessionAndRedirect(HttpServletRequest request, HttpServletResponse response, String targetPage, String username, String password, Users u) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher(targetPage);
        HttpSession session = request.getSession(true);
        session.setAttribute("username", username);
        session.setAttribute("password", password);
        session.setAttribute("userID", u.getUserID());
        logger.info("User ID: " + u.getUserID());
        session.setAttribute("role", u.getRoleID());
        rd.forward(request, response);
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
