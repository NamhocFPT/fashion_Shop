/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import entity.Categories;
import entity.Products;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Vector;
import model.DAOCategories;
import model.DAOProducts;

/**
 *
 * @author HP
 */
@WebServlet(name = "ShopController", urlPatterns = {"/ShopController"})
public class ShopController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
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

            DAOCategories daoCate = new DAOCategories();
            DAOProducts daoPro = new DAOProducts();
            Vector<Categories> listCategories = daoCate.getCategories("SELECT * FROM [dbo].[Categories]");
            request.setAttribute("listCategories", listCategories);

            if (service.equals("pagination")) {
                int pageid = Integer.parseInt(request.getParameter("pageid"));
                String CategoryID = request.getParameter("CategoryID");
                String priceDown = request.getParameter("priceDown");
                String priceUp = request.getParameter("priceUp");
                String str = request.getParameter("str");
                int count = 9;
                int offset = (pageid - 1) * count;

                String sql;
                if (CategoryID != null) {
                    sql = "SELECT * FROM Products WHERE CategoryID=" + CategoryID
                            + " ORDER BY ProductID OFFSET " + offset + " ROWS FETCH NEXT " + count + " ROWS ONLY";

                    String countSql = "SELECT COUNT(*) FROM Products WHERE CategoryID=" + CategoryID;
                    int totalProducts = daoPro.getTotalProducts(countSql);
                    int totalPages = (int) Math.ceil((double) totalProducts / count);
                    request.setAttribute("totalPages", totalPages);
                    
                } else if (priceDown != null) {
                    sql = "SELECT * FROM Products WHERE Price > " + priceDown + " AND Price < " + priceUp + 
                            " ORDER BY ProductID OFFSET " + offset + " ROWS FETCH NEXT " + count + " ROWS ONLY";
                    String countSql = "SELECT COUNT(*) FROM Products WHERE Price > " + priceDown + " AND Price < " + priceUp;
                    int totalProducts = daoPro.getTotalProducts(countSql);
                    int totalPages = (int) Math.ceil((double) totalProducts / count);
                    request.setAttribute("totalPages", totalPages);
//                } else if (str != null) {
//                    sql = "SELECT * FROM Products WHERE ProductName LIKE '%"+str+"%' ORDER BY ProductID OFFSET " + offset + " ROWS FETCH NEXT " + count + " ROWS ONLY";
//                    String countSql = "SELECT COUNT(*) FROM Products WHERE ProductName LIKE '%"+str+"%'";
//                    int totalProducts = daoPro.getTotalProducts(countSql);
//                    int totalPages = (int) Math.ceil((double) totalProducts / count);
//                    request.setAttribute("totalPages", totalPages);
                } else {
                    sql = "SELECT * FROM Products ORDER BY ProductID OFFSET " + offset + " ROWS FETCH NEXT " + count + " ROWS ONLY";
                    String countSql = "SELECT COUNT(*) FROM Products";
                    int totalProducts = daoPro.getTotalProducts(countSql);
                    int totalPages = (int) Math.ceil((double) totalProducts / count);
                    request.setAttribute("totalPages", totalPages);
                }
                
                Vector<Products> listProducts = daoPro.getProducts(sql);
                request.setAttribute("listProducts", listProducts);
                request.setAttribute("numberpage", pageid);
                request.setAttribute("CategoryID", CategoryID);
            }

//            if (service.equals("search")) {
//                String str = request.getParameter("str");
//                response.sendRedirect("ShopController?service=pagination&pageid=1&str="+str);
//                return;
//            }
            
            RequestDispatcher rd = request.getRequestDispatcher("Shop.jsp");
            rd.forward(request, response);
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
