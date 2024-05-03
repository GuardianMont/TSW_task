package ec.model.cart;

import java.util.Objects;

public class CartItem {
    //rappresentazione del prodotto nel carrello
    private int numItem;
    private ProductBean Item;
    public CartItem (ProductBean item, int numItem){
        this.Item= item;
        this.numItem = numItem;
    }
    public CartItem (ProductBean Item){
        this.Item= Item;
        numItem=1;
    }



    public void setNumItem(int numItem) {
        this.numItem = numItem;
    }

    public void setItem(ProductBean item) {
        Item = item;
    }

    public int getNumItem() {
        return numItem;
    }

    public ProductBean getItem() {
        return Item;
    }
    public void incrementNumItem(){
        setNumItem(this.getNumItem()+1);
    }

    public void decrementNumItem(){
        setNumItem(this.getNumItem()-1);
    }

    public void cancelOrder(){
        setNumItem(0);
    }

    public double prezzoAllItem(){
        return (this.Item.getPrezzo()*this.numItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return numItem == cartItem.numItem && Objects.equals(Item, cartItem.Item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numItem, Item);
    }
}
