public class Stock {
    private int stockID;
    private Shoes shoe;
    private int quantity;

    public Stock(int stockID, Shoes shoe, int quantity) {
        this.stockID = stockID;
        this.shoe = shoe;
        this.quantity = quantity;
    }

    public int getStockID() {
        return stockID;
    }

    public void setStockID(int stockID) {
        this.stockID = stockID;
    }

    public Shoes getShoe() {
        return shoe;
    }

    public void setShoe(Shoes shoe) {
        this.shoe = shoe;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
