/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Vector;

/**
 *
 * @author HP
 */
public class Cart {
    private Vector<CartItem> itemsList;
    
    public Cart() {
        itemsList = new Vector<>();
    }

    public Vector<CartItem> getItemsList() {
        return itemsList;
    }

    public void setItemsList(Vector<CartItem> itemsList) {
        this.itemsList = itemsList;
    }
    
    
    
    public CartItem getCartItemByProductID(int pid) {
        for (CartItem ci : this.itemsList) {
            if (ci.getProduct().getProductID() == pid) {
                return ci;
            }
        }
        return null;
    }
    
    public void addItemToCart(CartItem item) {
        boolean found = false;
        for (CartItem ci : this.itemsList) {
            if (ci.getProduct().getProductID() == item.getProduct().getProductID()) {
                ci.setQuantity(ci.getQuantity() + item.getQuantity());
                found = true;
                break;
            }
        }
        if(!found) {
            this.itemsList.add(item);
        }
    }
    
    public int getNumberCartItem() {
        return this.itemsList.size();
    }
    
    public double getTotalAmount() {
        double totalAmount = 0;
        for (int i = 0; i < this.itemsList.size(); i++) {
            totalAmount += this.itemsList.get(i).getProduct().getPrice() * this.itemsList.get(i).getQuantity();
        }
        return totalAmount;
    }
    
    public void deleteCartItem(int pos) {
        for (int i = 0; i < this.itemsList.size(); i++) {
            if (i == pos) {
                this.itemsList.remove(i);
                break;
            }
        }
    }
    
    public void deleteAllCartItem() {
        for (int i = this.itemsList.size() - 1; i >= 0; i--) {
            this.deleteCartItem(i);
        }
    }
    
    public void updateCart(int pos, int quantity) {
        this.getItemsList().get(pos).setQuantity(quantity);
    }
}
