package ec.model.cart;

import ec.control.Cart;
import ec.model.cart.*;
import ec.model.product.ProductBean;


import java.util.ArrayList;

public class ShoppingCart {
    private ArrayList<CartItem> Item_ordinati;
    private double prezzoTot;
    public ShoppingCart() {
        Item_ordinati = new ArrayList<CartItem>();
        prezzoTot = 0;
    }

    public synchronized boolean incrementItem(int id_Item) {
        CartItem Cart_I;
        for (int i = 0; i < Item_ordinati.size(); i++) {
            Cart_I = Item_ordinati.get(i);
            if (Cart_I.getItem().getId() == id_Item) {
                Cart_I.incrementNumItem();
                return  true;
            }
        }
        return false;
    }

    public synchronized boolean incrementCartItem(int id_Item, int quantità) {
        CartItem Cart_I;
        for (int i = 0; i < Item_ordinati.size(); i++) {
            Cart_I = Item_ordinati.get(i);
            if (Cart_I.getItem().getId() == id_Item) {
                Cart_I.incrementNumItemof(quantità);
                return  true;
            }
        }
        return false;
    }
    public synchronized void addItem (ProductBean item){
        if (!incrementItem(item.getId())) {
            Item_ordinati.add(new CartItem(item));
        }
    }
    public synchronized void addCartItem (ProductBean item, int quantità){
        if (!incrementCartItem(item.getId(), quantità)) {
            Item_ordinati.add(new CartItem(item,quantità));
        }
    }

    public synchronized void deleteItem(int id_Item) {
        CartItem Cart_I;
        for (int i = 0; i < Item_ordinati.size(); i++) {
            Cart_I = Item_ordinati.get(i);
            if (Cart_I.getItem().getId() == id_Item) {
                Cart_I.decrementNumItem();
                if (Cart_I.getNumItem() <= 0) {
                    Item_ordinati.remove(i);
                }
                return;
            }
        }

    }



    public ArrayList<CartItem> getItem_ordinati() {
        return Item_ordinati;
    }

    public void setItem_ordinati(ArrayList<CartItem> item_ordinati) {
        this.Item_ordinati = item_ordinati;
    }

    public double getPrezzoTot() {
        CartItem Cart_I;
        double prezzo = 0;
        for (int i = 0; i < Item_ordinati.size(); i++) {
            Cart_I = (CartItem) Item_ordinati.get(i);
            prezzo = prezzo + Cart_I.prezzoAllItem();
        }
        return prezzo;
    }

    public int getNumProdottiTot(){
        CartItem Cart_I;
        int numTot =0;
        for(int i=0; i<Item_ordinati.size(); i++){
            Cart_I = (CartItem) Item_ordinati.get(i);
            numTot= numTot + Cart_I.getNumItem();
        }
        return numTot;
    }

    public void setPrezzoTot(double prezzoTot) {
        this.prezzoTot = prezzoTot;
    }

    public CartItem getItem (int id){
        CartItem Cart_I;
        for (int i = 0; i < Item_ordinati.size(); i++) {
            Cart_I = Item_ordinati.get(i);
            if (Cart_I.getItem().getId() == id) {
                return Cart_I;
            }
        }
        return null;
    }

    public boolean isEmpty (){
        if(this.Item_ordinati.isEmpty()) return true;
        else return false;
    }

    public synchronized void CartItem(CartItem item){
        this.Item_ordinati.add(item);
    }

}