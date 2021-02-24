import java.util.ArrayList;
import java.util.List;

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

}
