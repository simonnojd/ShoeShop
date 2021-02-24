public class Customers {
    private int customerID;
    private String firstName;
    private String lastName;
    private Locations location;
    private String password;

    public Customers(int customerID, String firstName, String lastName, Locations location, String password) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.password = password;
    }

    public int getCustomerID() {
        return customerID;
    }

}