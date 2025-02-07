/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import entity.Cart;
import entity.OrderDetail;
import entity.Orders;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import model.DAOOrderDetail;
import model.DAOOrders;

/**
 *
 * @author HP
 */
@WebServlet(name = "OrderController", urlPatterns = {"/OrderController"})
public class OrderController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        DAOOrders daoOr = new DAOOrders();
        DAOOrderDetail daoOd = new DAOOrderDetail();
        HttpSession session = request.getSession();
        int userID = (int) session.getAttribute("userID");
        System.out.println(userID);
        Cart cart = (Cart) session.getAttribute("cart");

        try (PrintWriter out = response.getWriter()) {
            String service = request.getParameter("service");

            if (service.equals("checkout")) {
                String FirstName = request.getParameter("firstName");
                String LastName = request.getParameter("lastName");
                String Country = request.getParameter("country");
                String Address = request.getParameter("address");
                String Phone = request.getParameter("phone");
                String Email = request.getParameter("email");
                String OrderNotes = request.getParameter("orderNotes");

                LocalDateTime currentTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String OrderDate = currentTime.format(formatter);
                double TotalAmount = cart.getTotalAmount();
                String status = "Chờ phê duyệt";
                Orders o = new Orders(userID, OrderDate, TotalAmount, status, FirstName, LastName, Country, Address, Phone, Email, OrderNotes);
                int n = daoOr.addOrders(o);
                int OrderID = daoOr.getOrderIdNew();
                for (int i = 0; i < cart.getItemsList().size(); i++) {
                    OrderDetail od = new OrderDetail(OrderID, cart.getItemsList().get(i).getProduct().getProductID(), cart.getItemsList().get(i).getQuantity(), cart.getItemsList().get(i).getProduct().getPrice());
                    daoOd.addOrderDetail(od);
                }
                cart.deleteAllCartItem();
                response.sendRedirect("HomePage.jsp");
            }

            if (service.equals("displayOrders")) {
                String sql = "SELECT * FROM [dbo].[Orders]";
                Vector<Orders> orderList = daoOr.getOrders(sql);
                RequestDispatcher rd = request.getRequestDispatcher("DisplayOrders.jsp");
                request.setAttribute("orderList", orderList);
                rd.forward(request, response);
            }

            if (service.equals("checkOrders")) {
                String OrderID = request.getParameter("OrderID");
                Orders od = daoOr.getOrderById(Integer.parseInt(OrderID));
                daoOr.updateOrderStatus(od);
                response.sendRedirect("OrderController?service=displayOrders");
            }

            if (service.equals("filterStatus")) {
                String opt = request.getParameter("option"); // lấy đúng tham số từ form
                String sql = "SELECT * FROM [dbo].[Orders] WHERE Status = N'" + opt + "'"; // câu SQL trực tiếp với điều kiện

                Vector<Orders> orderList = daoOr.getOrders(sql);
                RequestDispatcher rd = request.getRequestDispatcher("DisplayOrders.jsp");
                request.setAttribute("orderList", orderList);
                rd.forward(request, response);
            }

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
