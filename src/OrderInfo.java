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

    public int getOrderInfoID() {
        return orderInfoID;
    }

    public void setOrderInfoID(int orderInfoID) {
        this.orderInfoID = orderInfoID;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Shoes getShoe() {
        return shoe;
    }

    public void setShoe(Shoes shoe) {
        this.shoe = shoe;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
