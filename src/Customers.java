import java.util.ArrayList;
import java.util.List;

public class Customers {
    private int customerID;
    private String firstName;
    private String lastName;
    private Locations location;
    private String password;
    private List<Orders> ordersList = new ArrayList<>();

    public Customers(int customerID, String firstName, String lastName, Locations location, String password) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.password = password;
    }

    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public void addOrderList(Orders o) {
        ordersList.add(o);
    }

    public void printOrderList() {
        for (Orders o : ordersList) {
            System.out.println(o.getOrderID());
        }
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Locations getLocation() {
        return location;
    }

    public void setLocation(Locations location) {
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}