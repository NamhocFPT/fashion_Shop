/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import entity.Orders;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Vector;

/**
 *
 * @author HP
 */
public class DAOOrders extends DBConnection {

    public int addOrders(Orders od) {
        int n = 0;
        String sql = """
                     INSERT INTO [dbo].[Orders]
                                           ([UserID]
                                           ,[OrderDate]
                                           ,[TotalAmount]
                                           ,[Status]
                                           ,[FirstName]
                                           ,[LastName]
                                           ,[Country]
                                           ,[Address]
                                           ,[Phone]
                                           ,[Email]
                                           ,[OrderNotes])
                                     VALUES
                                           (?
                                           ,?
                                           ,?
                                           ,?
                                           ,?
                                           ,?
                                           ,?
                                           ,?
                                           ,?
                                           ,?
                                           ,?)
                     """;
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, od.getUserID());
            pre.setString(2, od.getOrderDate());
            pre.setDouble(3, od.getTotalAmount());
            pre.setString(4, od.getStatus());
            pre.setString(5, od.getFirstName());
            pre.setString(6, od.getLastName());
            pre.setString(7, od.getCountry());
            pre.setString(8, od.getAddress());
            pre.setString(9, od.getPhone());
            pre.setString(10, od.getEmail());
            pre.setString(11, od.getOrderNotes());
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOOrders.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public Vector<Orders> getOrders(String sql) {
        Vector<Orders> ordersList = new Vector<Orders>();
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                int OrderID = rs.getInt(1);
                int UserID = rs.getInt(2);
                String OrderDate = rs.getString(3);
                double TotalAmount = rs.getDouble(4);
                String Status = rs.getString(5);
                String FirstName = rs.getString(6);
                String LastName = rs.getString(7);
                String Country = rs.getString(8);
                String Address = rs.getString(9);
                String Phone = rs.getString(10);
                String Email = rs.getString(11);
                String OrderNotes = rs.getString(12);

                ordersList.add(new Orders(OrderID, UserID, OrderDate, TotalAmount, Status, FirstName, LastName, Country, Address, Phone, Email, OrderNotes));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOOrders.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ordersList;
    }

    public int updateOrderStatus(Orders od) {
        int n = 0;
        String sql = """
                     UPDATE [dbo].[Orders]
                        SET [Status] = N'Đã phê duyệt'
                      WHERE [OrderID] = ?
                     """;
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, od.getOrderID());
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOOrders.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public Orders getOrderById(int OrderID) {
        Orders od = null;
        String sql = "SELECT * FROM [dbo].[Orders] WHERE OrderID = " + OrderID;
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                int OrderId = rs.getInt(1);
                int UserId = rs.getInt(2);
                String OrderDate = rs.getString(3);
                double totalAmount = rs.getDouble(4);
                String status = rs.getString(5);
                String FirstName = rs.getString(6);
                String LastName = rs.getString(7);
                String Country = rs.getString(8);
                String Address = rs.getString(9);
                String Phone = rs.getString(10);
                String Email = rs.getString(11);
                String OrderNotes = rs.getString(12);
                od = new Orders(OrderId, UserId, OrderDate, totalAmount, status, FirstName, LastName, Country, Address, Phone, Email, OrderNotes);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOOrders.class.getName()).log(Level.SEVERE, null, ex);
        }
        return od;
    }

    public int getOrderIdNew() {
        String sql = "Select top(1) OrderID from [dbo].[Orders]\n"
                + "order by OrderID desc";
        PreparedStatement pre;
        int n = 0;
        try {
            pre = conn.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                n = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOOrders.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public static void main(String[] args) {
        DAOOrders dao = new DAOOrders();
        int n = dao.getOrderIdNew();
        System.out.println(n);
    }
}
