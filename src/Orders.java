public class Orders {
    private int orderID;
    private Customers customer;

    public Orders(int orderID, Customers customer) {
        this.orderID = orderID;
        this.customer = customer;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }
}
