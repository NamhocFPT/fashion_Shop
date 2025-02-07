/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import entity.OrderDetail;
import java.sql.*;
import java.util.Vector;
/**
 *
 * @author HP
 */
public class DAOOrderDetail extends DBConnection{
    public int addOrderDetail(OrderDetail od) {
        int n = 0;
        String sql = """
                     INSERT INTO [dbo].[Order_Details]
                                                      ([OrderID]
                                                      ,[ProductID]
                                                      ,[Quantity]
                                                      ,[UnitPrice])
                                                VALUES
                                                      (?
                                                      ,?
                                                      ,?
                                                      ,?)
                     """;
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, od.getOrderID());
            pre.setInt(2, od.getProductID());
            pre.setInt(3, od.getQuantity());
            pre.setDouble(4, od.getUnitPrice());
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public Vector<OrderDetail> getOrderDetail(String sql) {
        Vector<OrderDetail> OrderDetailList = new Vector<OrderDetail>();
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                int OrderDetailID = rs.getInt(1);
                int OrderID = rs.getInt(2);
                int ProductID = rs.getInt(3);
                int Quantity = rs.getInt(4);
                double UnitPrice = rs.getDouble(5);

                OrderDetailList.add(new OrderDetail(OrderDetailID, OrderID, ProductID, Quantity, UnitPrice));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return OrderDetailList;
    }
}
