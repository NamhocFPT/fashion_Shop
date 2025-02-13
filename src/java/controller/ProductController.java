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
@WebServlet(name="ProductController", urlPatterns={"/ProductController"})
public class ProductController extends HttpServlet {
   
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
            DAOProducts daoPro = new DAOProducts();
            if (service.equals("pagination")) {
                DAOCategories daoCate = new DAOCategories();
                
                Vector<Categories> listCategories = daoCate.getCategories("SELECT * FROM [dbo].[Categories]");
                request.setAttribute("listCategories", listCategories);
                int pageid = Integer.parseInt(request.getParameter("pageid"));
                String CategoryID = request.getParameter("CategoryID");
                String priceDown = request.getParameter("priceDown");
                String priceUp = request.getParameter("priceUp");
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
                RequestDispatcher rd = request.getRequestDispatcher("DisplayProducts.jsp");
                rd.forward(request, response);
            }

            if (service.equals("updateProducts")) {
                String submit = request.getParameter("submit");
                if (submit == null) {
                    String ProductID = request.getParameter("pid");
                    RequestDispatcher rd = request.getRequestDispatcher("UpdateProducts.jsp");
                    Products pro = daoPro.getProductById(Integer.parseInt(ProductID));
                    request.setAttribute("pro", pro);
                    rd.forward(request, response);
                    
                } else {
                    int ProductID = Integer.parseInt(request.getParameter("ProductID"));
                    String ProductName = request.getParameter("ProductName");
                    double Price = Double.parseDouble(request.getParameter("Price"));
                    int Stock = Integer.parseInt(request.getParameter("Stock"));
                    int CategoryID = Integer.parseInt(request.getParameter("CategoryID"));
                    String Description = request.getParameter("Description");
                    String ImageURL = request.getParameter("ImageURL");
                    int n = daoPro.updateProduct(new Products(ProductID, ProductName, Price, Stock, CategoryID, Description, ImageURL));
                    response.sendRedirect("ProductController?service=pagination&pageid=1");
                }
            }
            
            if (service.equals("addProduct")) {
                String submit = request.getParameter("submit");
                if (submit == null) {
                    response.sendRedirect("AddProduct.jsp");
                } else {
                    String ProductName = request.getParameter("ProductName");
                    double Price = Double.parseDouble(request.getParameter("Price"));
                    int Stock = Integer.parseInt(request.getParameter("Price"));
                    int CategoryID = Integer.parseInt(request.getParameter("CategoryID"));
                    String Description = request.getParameter("Description");
                    String ImageURL = request.getParameter("ImageURL");
                    int n = daoPro.addProduct(new Products(ProductName, Price, Stock, CategoryID, Description, ImageURL));
                    response.sendRedirect("ProductController?service=pagination&pageid=1");
                }
            }
            
            if (service.equals("deleteProducts")) {
                int ProductID = Integer.parseInt(request.getParameter("pid"));
                int n = daoPro.deleteProduct(ProductID);
                response.sendRedirect("ProductController?service=pagination&pageid=1");
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
