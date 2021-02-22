import javax.xml.transform.Result;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Repository {

    private Properties properties = new Properties();
    private int counter = 1;
    private Customers customers;
    private Locations locations;
    private Orders orders;
    private int customerID;
    private int orderID;
    private Connection connection;
    private Shoes shoe;

    public Repository() {
        try {
            properties.load(new FileInputStream("src/Properties.properties"));
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkLogin(String username, String password) {
        try (Connection connection = addConnection();
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
        while (true) {
            if (checkLogin(username, password)) {
                switch (scanner.nextInt()) {
                    case 1:
                        getOrder(username);
                        break;
                    case 2: {
                        getAllShoes().forEach(s -> System.out.println(counter++ + ". Kategori: " + s.getCategoriesList() + ", " + s.getBrand().getBrandName()
                                + ", " + s.getShoeName() + ", Färg: " + s.getColorsList() + ", " + s.getPriceID().getPriceNumber() +
                                "kr, storlek: " + s.getSizeID().getSizeNumber()));
                        System.out.println("\n\nVälj Sko nummer:");
                        int choice = scanner.nextInt();
                        int shoeID = chooseShoe(choice).getShoeID();
                        System.out.println("Du har valt " + chooseShoe(choice).getShoeName());
                        System.out.println("Välj 1 om du vill beställa skon, välj 2 om du vill sätta betyg på skon, välj 3 om du vill se genomsnittsbetyget på skon och dess kommentarer");
                        switch (scanner.nextInt()) {
                            case 1:
                                System.out.println("Hur många exemplar vill du beställa?");
                                int amountOfShoes = scanner.nextInt();
                                customerID = getCustomer(username).getCustomerID();
                                System.out.println(addShoeToOrder(customerID, shoeID, amountOfShoes));
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

                            case 3: {
                                averageRatingOnShoe(shoeID);
                                getCommentsFromReviews(chooseShoe(choice));
                                break;
                            }
                        }

                        System.out.println("Vill du lägga till en till sko?");
                        scanner.nextLine();
                        if (scanner.nextLine().equalsIgnoreCase("Nej")) {
                            break;
                        }
                        break;
                    }
                }
            } else System.out.println("Uppgifterna finns inte i systemet");
        }
    }

    public Connection addConnection() throws SQLException {
        return DriverManager.getConnection(properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
    }

    public void getOrder(String customerName) {
        int customerID = getCustomer(customerName).getCustomerID();
        try (Connection connection = addConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * from orders JOIN order_info on orders.id = order_info.order_id WHERE orders.customer_id = '" + customerID + "'")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                orders = new Orders(id, customers);
                getCustomer(customerName).addOrderList(orders);
            }
            System.out.println(getCustomer(customerName).getOrdersList());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String addShoeToOrder(int customerID, int shoeID, int quantity) {

        try (Connection connection = addConnection();
             CallableStatement callableStatement = connection.prepareCall("CALL add_to_cart (?, ?, ?, ?)")) {

            callableStatement.setInt(1, customerID);
            callableStatement.setInt(2, orderID);
            callableStatement.setInt(3, shoeID);
            callableStatement.setInt(4, quantity);
            callableStatement.registerOutParameter(2, Types.INTEGER);
            callableStatement.execute();

            if (orderID == 0) {
                orderID = callableStatement.getInt(2);
            }
            System.out.println(orderID);

            return "Skon är tillagd";

        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println("Transaction is being rolled back");
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.getMessage();
            }

        }

        return "Detta gick inte så bra";
    }


    public Customers getCustomer(String userName) {
        try (Connection connection = addConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * from customers JOIN locations ON location_id = locations.id WHERE first_name ='" + userName + "'")) {

            while (resultSet.next()) {
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
        List<Shoes> shoeList = new ArrayList<>();
        counter = 1;

        try (Connection connection = addConnection();
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


        } catch (Exception e) {
            e.printStackTrace();
        }
        giveShoesColor(shoeList);
        giveShoesCategory(shoeList);
        getReviews(shoeList);
        return shoeList;
    }

    public Shoes chooseShoe(int choice) {
        List<Shoes> shoeList = getAllShoes();
        for (int i = 1; i < shoeList.size() + 1; i++) {
            if (i == choice) {
                return shoeList.get(i - 1);
            }
        }
        return null;
    }

    public void giveShoesColor(List<Shoes> shoeList) {
        try (Connection connection = addConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM shoe_has_color JOIN colors ON colors.id = shoe_has_color.color_id");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int shoeID = resultSet.getInt("shoe_id");
                for (Shoes s : shoeList) {
                    if (s.getShoeID() == shoeID) {
                        s.addToColorList(new Colors(resultSet.getInt("shoe_has_color.color_id"), resultSet.getString("colors.color_name")));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void giveShoesCategory(List<Shoes> shoeList) {
        try (Connection connection = addConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM shoes_in_categories JOIN categories ON categories.id = shoes_in_categories.category_id");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int shoeID = resultSet.getInt("shoe_id");
                for (Shoes s : shoeList) {
                    if (s.getShoeID() == shoeID) {
                        s.addToCategoryList(new Categories(resultSet.getInt("shoes_in_categories.category_id"), resultSet.getString("categories.name")));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void rateShoe(int customerID, int shoeID, int grade, String comment) {

        try (Connection connection = addConnection()) {
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

    public void averageRatingOnShoe(int shoeID) {

        try (Connection connection = addConnection()) {
            PreparedStatement statement = connection.prepareCall("SELECT average_rating(?)");
            statement.setInt(1, shoeID);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            int rating = resultSet.getInt(1);
            System.out.println("Genomsnitts betyget för skon är: " + rating);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getReviews(List<Shoes> shoeList) {

        try (Connection connection = addConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reviews JOIN customers on reviews.customer_id = customers.id JOIN shoes ON reviews.shoe_id = shoes.id");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("reviews.id");
                String customerName = resultSet.getString("customers.first_name");
                Customers customers = getCustomer(customerName);
                int shoeID = resultSet.getInt("reviews.shoe_id");
                int numberRating = resultSet.getInt("reviews.number_rating");
                String comment = resultSet.getString("reviews.comment");
                for (Shoes s : shoeList) {
                    if (s.getShoeID() == shoeID) {
                        shoe = s;
                        s.addToReviewsList(new Reviews(id, customers, shoe, numberRating, comment));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCommentsFromReviews(Shoes shoes) {
        List<Reviews> reviews = shoes.getReviewsList();
        for (Reviews r : reviews) {
            System.out.println("Kommentar: " + r.getComment());
        }

    }
}