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


}
