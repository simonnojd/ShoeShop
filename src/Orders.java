import java.util.ArrayList;
import java.util.List;

public class Orders {
    private int orderID;
    private Customers customer;
    private List<OrderInfo> orderInfoList = new ArrayList<>();

    public Orders(int orderID, Customers customer) {
        this.orderID = orderID;
        this.customer = customer;
    }

    public void printOrderInfoList() {
        for (OrderInfo oi : orderInfoList) {
            System.out.println(oi);
        }
    }

    public void addOrderInfoList(OrderInfo o) {
        orderInfoList.add(o);
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
