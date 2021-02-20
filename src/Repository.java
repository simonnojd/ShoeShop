import javax.xml.transform.Result;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Repository {

    private List<Shoes> shoeList = new ArrayList<>();
    private Properties properties = new Properties();
    private int counter = 1;
    private Customers customers;
    private  Locations locations;
    private Orders orders;
    private int customerID;
    private int orderID;
    private Connection connection;

    public Repository() {
        try {
            properties.load(new FileInputStream("src/Properties.properties"));
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkLogin(String username, String password) {
        try (Connection connection = DriverManager.getConnection(properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT password, first_name FROM customers");) {
            while (resultSet.next()) {
                if (resultSet.getString("first_name").equalsIgnoreCase(username) &&
                        resultSet.getString("password").equalsIgnoreCase(password)) {
                    System.out.println("Välkommen till skoaffären " + username);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void promptUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Skriv användarnamn:");
        String username = scanner.nextLine().trim();

        System.out.println("Skriv lösenord:");
        String password = scanner.nextLine().trim();

        if (checkLogin(username, password)) {
            getAllShoes();
            System.out.println("\n\nVälj Sko nummer:");
            int choice = scanner.nextInt();
            int shoeID = chooseShoe(choice).getShoeID();
            System.out.println("Du har valt " + chooseShoe(choice).getShoeName());
            System.out.println("Välj 1 om du vill beställa skon, välj 2 om du vill sätta betyg på skon");
            switch (scanner.nextInt()){
                case 1:
                    System.out.println("Hur många exemplar vill du beställa?");
                    int amountOfShoes = scanner.nextInt();
                     customerID = getCustomer(username).getCustomerID();
                    System.out.println("Customer id: " + customerID);
                     orderID = getOrder(getCustomer(username)).getOrderID();
                    System.out.println("Order id " + orderID);
                    addShoeToOrder(customerID, orderID, shoeID, amountOfShoes);
                    break;

                case 2: {
                    System.out.println("Sätt en kommentar på skon");
                    scanner.nextLine();
                    String comment = scanner.nextLine().trim();
                    System.out.println("Betygsätt skon mellan 1-4");
                    int grade = scanner.nextInt();
                    rateShoe(getCustomer(username).getCustomerID(), shoeID, grade, comment);
                    break;

                }
            }

        } else System.out.println("Uppgifterna finns inte i systemet");
    }

    public Orders getOrder(Customers customers){
        int customerID = customers.getCustomerID();
        try (Connection connection = DriverManager.getConnection(properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery( "SELECT * from orders WHERE orders.customer_id = '"+customerID +"'")){
            // Nånting med selecten och loopen ställer till problem
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                orders = new Orders(id, customers);
                return orders;
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addShoeToOrder(int customerID, int orderID , int shoeID, int quantity) {

       try(Connection connection = DriverManager.getConnection(properties.getProperty("connectionString"),
               properties.getProperty("name"),
               properties.getProperty("password"));
                CallableStatement callableStatement = connection.prepareCall("CALL add_to_cart (?, ?, ?, ?)")){

           connection.setAutoCommit(false);
           callableStatement.setInt(1, customerID);
           callableStatement.setInt(2,orderID );
           callableStatement.setInt(3, shoeID);
           callableStatement.setInt(4, quantity);
           callableStatement.execute();

           connection.commit();


           connection.setAutoCommit(true);


       } catch (Exception e) {
           e.printStackTrace();
           try {
               System.out.println("Transaction is being rolled back");
               connection.rollback();
           } catch (SQLException throwables) {
               throwables.getMessage();
           }

       }
    }


    public Customers getCustomer(String userName) {
        try (Connection connection = DriverManager.getConnection(properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery( "SELECT * from customers JOIN locations ON location_id = locations.id WHERE first_name ='"+userName + "'")){

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                locations = new Locations(resultSet.getInt("customers.location_id"), resultSet.getString("locations.location_name"));
                String password = resultSet.getString("password");
                customers = new Customers(id, firstName, lastName, locations, password);
                return customers;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<Shoes> getAllShoes() {

        try (Connection connection = DriverManager.getConnection(properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM shoes JOIN prices ON price_id = prices.id JOIN sizes ON size_id = sizes.id JOIN brands ON brand_id = brands.id");) {
            while (resultSet.next()) {
                int shoeID = resultSet.getInt("shoes.id");
                Prices price = new Prices(resultSet.getInt("shoes.price_id"), resultSet.getInt("prices.price"));
                Sizes size = new Sizes(resultSet.getInt("shoes.size_id"), resultSet.getInt("sizes.size"));
                Brands brand = new Brands(resultSet.getInt("shoes.brand_id"), resultSet.getString("brands.name"));
                String shoeName = resultSet.getString("shoes.shoe_name");

                shoeList.add(new Shoes(shoeID, price, size, brand, shoeName));
            }

            for (Shoes s : shoeList) {
                System.out.println(counter + ". " + s.getBrand().getBrandName() + ", " + s.getShoeName() + ", " + s.getPriceID().getPriceNumber() + "kr, storlek: " + s.getSizeID().getSizeNumber());
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shoeList;
    }

    public Shoes chooseShoe(int choice) {
        for (int i = 1; i < shoeList.size() +1; i++) {
            if (i == choice) {
                return shoeList.get(i - 1);
            }
        }
        return null;
    }



    public void rateShoe(int customerID, int shoeID, int grade, String comment) {

        try (Connection connection = DriverManager.getConnection(properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));) {
            while (true) {
                CallableStatement statement = connection.prepareCall("CALL rate(?,?,?,?)");
                statement.setInt(1, customerID);
                statement.setInt(2, shoeID);
                if (grade >= 1 && grade < 5) {
                    statement.setInt(3, grade);
                    statement.setString(4, comment);
                    statement.execute();
                    break;
                } else {
                    System.out.println("Betyget får vara minst 1 och max 4");
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}