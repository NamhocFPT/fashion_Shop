/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;
import entity.Categories;
import entity.Products;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import model.DAOCategories;
import model.DAOProducts;

@WebServlet(name = "ShopController", urlPatterns = {"/ShopController"})
public class ShopController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String service = request.getParameter("service");
            DAOCategories daoCate = new DAOCategories();
            DAOProducts daoPro = new DAOProducts();
            
            // Lấy danh sách categories
            Vector<Categories> listCategories = daoCate.getCategories("SELECT * FROM [dbo].[Categories]");
            request.setAttribute("listCategories", listCategories);

            if ("pagination".equals(service)) {  // Kiểm tra service nếu có giá trị 'pagination'
                int pageid = Integer.parseInt(request.getParameter("pageid"));
                String categoryID = request.getParameter("CategoryID");
                String priceDown = request.getParameter("priceDown");
                String priceUp = request.getParameter("priceUp");

                int count = 9;
                int offset = (pageid - 1) * count;
                String sql = buildSQLQuery(categoryID, priceDown, priceUp, offset, count);
                String countSql = buildCountSQL(categoryID, priceDown, priceUp);

                // Lấy tổng số sản phẩm và số trang
                int totalProducts = daoPro.getTotalProducts(countSql);
                int totalPages = (int) Math.ceil((double) totalProducts / count);
                request.setAttribute("totalPages", totalPages);

                // Lấy danh sách sản phẩm
                Vector<Products> listProducts = daoPro.getProducts(sql);
                request.setAttribute("listProducts", listProducts);
                request.setAttribute("numberpage", pageid);
                request.setAttribute("CategoryID", categoryID);
            }
            
            // Chuyển đến trang Shop.jsp để hiển thị
            RequestDispatcher rd = request.getRequestDispatcher("Shop.jsp");
            rd.forward(request, response);
        }
    }

    // Phương thức xây dựng câu truy vấn SQL cho sản phẩm
    private String buildSQLQuery(String categoryID, String priceDown, String priceUp, int offset, int count) {
        String sql;
        if (categoryID != null) {
            sql = "SELECT * FROM Products WHERE CategoryID=" + categoryID
                    + " ORDER BY ProductID OFFSET " + offset + " ROWS FETCH NEXT " + count + " ROWS ONLY";
        } else if (priceDown != null && priceUp != null) {
            sql = "SELECT * FROM Products WHERE Price > " + priceDown + " AND Price < " + priceUp
                    + " ORDER BY ProductID OFFSET " + offset + " ROWS FETCH NEXT " + count + " ROWS ONLY";
        } else {
            sql = "SELECT * FROM Products ORDER BY ProductID OFFSET " + offset + " ROWS FETCH NEXT " + count + " ROWS ONLY";
        }
        return sql;
    }

    // Phương thức xây dựng câu truy vấn SQL đếm số lượng sản phẩm
    private String buildCountSQL(String categoryID, String priceDown, String priceUp) {
        String countSql;
        if (categoryID != null) {
            countSql = "SELECT COUNT(*) FROM Products WHERE CategoryID=" + categoryID;
        } else if (priceDown != null && priceUp != null) {
            countSql = "SELECT COUNT(*) FROM Products WHERE Price > " + priceDown + " AND Price < " + priceUp;
        } else {
            countSql = "SELECT COUNT(*) FROM Products";
        }
        return countSql;
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
