import java.util.Date;

public class OutOfStock {
    private int outOfStockID;
    private Shoes shoe;
    private Date outOfStockDate;

    public OutOfStock(int outOfStockID, Shoes shoe, Date outOfStockDate) {
        this.outOfStockID = outOfStockID;
        this.shoe = shoe;
        this.outOfStockDate = outOfStockDate;
    }

    public int getOutOfStockID() {
        return outOfStockID;
    }

    public void setOutOfStockID(int outOfStockID) {
        this.outOfStockID = outOfStockID;
    }

    public Shoes getShoe() {
        return shoe;
    }

    public void setShoe(Shoes shoe) {
        this.shoe = shoe;
    }

    public Date getOutOfStockDate() {
        return outOfStockDate;
    }

    public void setOutOfStockDate(Date outOfStockDate) {
        this.outOfStockDate = outOfStockDate;
    }
}
