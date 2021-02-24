import java.util.Date;

public class OrderInfo {
    private int orderInfoID;
    private Orders order;
    private int quantity;
    private Shoes shoe;
    private Date orderDate;

    public OrderInfo(int orderInfoID, Orders order, int quantity, Shoes shoe, Date orderDate) {
        this.orderInfoID = orderInfoID;
        this.order = order;
        this.quantity = quantity;
        this.shoe = shoe;
        this.orderDate = orderDate;
    }


    public Orders getOrder() {
        return order;
    }

    public int getQuantity() {
        return quantity;
    }

    public Shoes getShoe() {
        return shoe;
    }


}
