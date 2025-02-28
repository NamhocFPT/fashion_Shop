/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import entity.Cart;
import entity.CartItem;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.DAOProducts;

/**
 *
 * @author HP
 */
@WebServlet(name = "CartController", urlPatterns = {"/CartController"})
public class CartController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String service = request.getParameter("service");
            DAOProducts daoPro = new DAOProducts();
            HttpSession session = request.getSession();
            int cartItemPos = Integer.parseInt(request.getParameter("CartItemPos"));
            int productID = Integer.parseInt(request.getParameter("pid"));
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
            }

            if (service.equals("add2cart")) {
                String username = (String) session.getAttribute("username");
                if (username == null) {
                    request.getRequestDispatcher("Login.jsp").forward(request, response);
                }
                

                CartItem ci = new CartItem(daoPro.getProductById(productID), 1);
                cart.addItemToCart(ci);
                response.sendRedirect("ShopController?service=pagination&pageid=1");
            }
            session.setAttribute("cart", cart);
            if (service.equals("showShoppingCart")) {
                response.sendRedirect("Shopping-cart.jsp");
            }

            if (service.equals("deleteCartItem")) {
                CartItem ci = new CartItem(daoPro.getProductById(productID), 1);
                cart.addItemToCart(ci);
                response.sendRedirect("ShopController?service=pagination&pageid=1");
            }
            session.setAttribute("cart", cart);
            if (service.equals("showShoppingCart")) {
                response.sendRedirect("CartController");
            }

            if (service.equals("deleteCartItem")) {
                cart.updateCart(cartItemPos, cart.getItemsList().get(cartItemPos).getQuantity() - 2);
                response.sendRedirect("CartController");
            }

            if (service.equals("UpdateCart")) {
                for (int i = 0; i < cart.getItemsList().size(); i++) {
                    int newQuantity = Integer.parseInt(request.getParameter("updateQuantity_" + i));
                    cart.updateCart(i, newQuantity);
                }
                response.sendRedirect("Shopping-cart.jsp");
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
