/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import entity.Users;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class DAOUsers extends DBConnection {

    public int addUser(Users user) {
        int n = 0;
        String sql = "INSERT INTO [dbo].[Users]\n"
                + "           ([Username]\n"
                + "           ,[Password]\n"
                + "           ,[RoleID])\n"
                + "     VALUES\n"
                + "           (?\n"
                + "           ,?\n"
                + "           ,?)";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, user.getUsername());
            pre.setString(2, user.getPassword());
            pre.setInt(3, user.getRoleID());

            n = pre.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }
    
    public ArrayList<Users> getUsers(String sql) {
        ArrayList<Users> vector = new ArrayList<>();
        Statement state;
        try {
            state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                int UserID = rs.getInt(1);
                String Username = rs.getString(2);
                String Password = rs.getString(3);
                int RoleID = rs.getInt(4);
                vector.add(new Users(UserID, Username, Password, RoleID));
            }
         } catch (SQLException ex) {
            Logger.getLogger(DAOUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }
    
    public Users getUserByUserId(int UserID) {
        String sql = "SELECT * FROM Users WHERE UserID = " + UserID;
        Users u = null;
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                String Username = rs.getString(2);
                String Password = rs.getString(3);
                int RoleID = rs.getInt(4);
                u = new Users(UserID, Username, Password, RoleID);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOUsers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }
    
    public int updateUser(Users u) {
        int n = 0;
        String sql = "UPDATE [dbo].[Users]\n"
                + "   SET [Username] = ?,[Password] = ?,[RoleID] = ?\n"
                + " WHERE UserID=?";
        try {
            //  ? --> field , index start 1 : first : ProductName; second:  SupplierID ...
            PreparedStatement pre = conn.prepareStatement(sql);
            
            pre.setString(1, u.getUsername());
            pre.setString(2, u.getPassword());
            pre.setInt(3, u.getRoleID());
            pre.setInt(4, u.getUserID());
            
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public int deleteUser(int UserID) {
        int n = 0;        
        String sql = "delete from Users where UserID=" + UserID;
        try {
            Statement state = conn.createStatement();
            n = state.executeUpdate(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }
    
    public static void main(String[] args) {
        DAOUsers dao = new DAOUsers();
        int n = dao.updateUser(dao.getUserByUserId(1));
        System.out.println(n);
    }
}
