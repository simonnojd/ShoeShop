import java.io.FileInputStream;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Repository {

    private Properties properties = new Properties();
    private int counter = 1;
    private Customers customers;
    private Locations locations;
    private Orders orders;
    private OrderInfo orderInfo;
    private int customerID;
    private int orderID;
    private Connection connection;
    private Shoes shoe;
    private List<OrderInfo> orderInfoList = new ArrayList<>();
    private int id = 0;
    int quantity = 0;

    public Repository() {
        try {
            properties.load(new FileInputStream("src/Properties.properties"));
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Kollar att man har loggat in med rätt användarnman och lösenord
    public boolean checkLogin(String username, String password) {
        try (Connection connection = addConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT password, first_name FROM customers");) {
            while (resultSet.next()) {
                if (resultSet.getString("first_name").equalsIgnoreCase(username) &&
                        resultSet.getString("password").equalsIgnoreCase(password)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Huvudprogrammet där användaren gör val och metoder callas
    public void promptUser() {
        System.out.println("Välkommen till skoaffären!\n\n");
        int choice = 0;
        int shoeID = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Skriv användarnamn:");
        String username = scanner.nextLine().trim();

        System.out.println("Skriv lösenord:");
        String password = scanner.nextLine().trim();

        while (true) {
            if (checkLogin(username, password)) {
                printAllShoes();
                try {
                    choice = scanner.nextInt();
                    shoeID = chooseShoe(choice).getShoeID();
                } catch (Exception e) {
                    System.out.println("Måste vara en siffra");
                    System.exit(0);
                }


                System.out.println("Välj 1 om du vill beställa skon, välj 2 om du vill sätta betyg på skon, välj 3 om du vill se genomsnittsbetyget på skon och dess kommentarer");
                try {
                    switch (scanner.nextInt()) {
                        case 1:
                            System.out.println("Hur många exemplar vill du beställa?");
                            int amountOfShoes = scanner.nextInt();
                            customerID = getCustomer(username).getCustomerID();
                            addShoeToOrder(customerID, shoeID, amountOfShoes);
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
                } catch (Exception e) {
                    System.out.println("Något gick fel");
                    System.exit(0);
                }

                System.out.println("Tryck 1 om du lägga till en till sko?, 2 om du vill se din beställning, 3 om du vill avsluta.");
                try {
                    switch (scanner.nextInt()) {

                        case 1:
                            continue;
                        case 2: {
                            getOrderInfo(username);
                            printOrderList();
                            System.exit(0);
                        }
                        case 3:
                            System.exit(0);
                    }
                } catch (Exception e) {
                    System.out.println("Error");
                    System.exit(0);
                }

            } else System.out.println("Uppgifterna finns inte i systemet");
        }
    }

    // Printar alla skor till menyn
    public void printAllShoes() {
        getAllShoes().forEach(s -> System.out.println(counter++ + ". Kategori: " + s.getCategoriesList() + ", " + s.getBrand().getBrandName()
                + ", " + s.getShoeName() + ", Färg: " + s.getColorsList() + ", " + s.getPriceID().getPriceNumber() +
                "kr, storlek: " + s.getSizeID().getSizeNumber()));

        System.out.println("\n\nVälj Sko nummer:");
    }

    // Connectar till databasen
    public Connection addConnection() throws SQLException {
        return DriverManager.getConnection(properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
    }

    // Lägger in alla orders av en specifik kund i en lista
    public void getOrderInfo(String customerName) {

        int orderInfoID;

        int customerID = getCustomer(customerName).getCustomerID();
        try (Connection connection = addConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * from orders JOIN order_info on orders.id = order_info.order_id WHERE orders.customer_id = '" + customerID + "'")) {

            while (resultSet.next()) {
                orderInfoID = resultSet.getInt("order_info.id");
                quantity = resultSet.getInt("order_info.quantity");
                Date date = resultSet.getDate("order_date");
                int shoeID = resultSet.getInt("shoe_id");
                Shoes shoe = findShoeByID(shoeID);
                int id = resultSet.getInt("orders.id");
                orders = new Orders(id, customers);
                orderInfo = new OrderInfo(orderInfoID, orders, quantity, shoe, date);
                orderInfoList.add(orderInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Printar ut en användares senaste order
    public void printOrderList() {
        System.out.println("--------Kvitto----------");
        for (OrderInfo oi : orderInfoList) {
            if (oi.getOrder().getOrderID() == orderID) {
                System.out.println("Skonamn: " + oi.getShoe().getShoeName() + ", Kvantitet: " + oi.getQuantity());
            }
        }
        System.out.println("--------SLUT----------");
    }


    // Callar på SP:n i MySql som lägger till en beställning
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
            return "Skon är tillagd";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Detta gick inte så bra";
    }

    // Returnerar en Customer
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

    // Hämtar alla skor och returnerar dom i en lista
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

    // Returnerar skon baserat på användarens val
    public Shoes chooseShoe(int choice) {
        List<Shoes> shoeList = getAllShoes();
        for (int i = 1; i < shoeList.size() + 1; i++) {
            if (i == choice) {
                return shoeList.get(i - 1);
            }
        }
        return null;
    }

    // Returnerar sko baserat på id
    public Shoes findShoeByID(int shoeID) {
        List<Shoes> shoeList = getAllShoes();
        for (Shoes s : shoeList) {
            if (s.getShoeID() == shoeID) {
                return s;
            }
        }
        return null;
    }

    // Lägger till en lista av färger för varje sko
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

    // Lägger till en lista av kategorier för varje sko
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


    // Callar på SP:n i MySql som betygsätter en sko
    public void rateShoe(int customerID, int shoeID, int grade, String comment) {

        try (Connection connection = addConnection()) {
            while (true) {
                CallableStatement statement = connection.prepareCall("CALL rate(?,?,?,?, ?)");
                statement.setInt(1, customerID);
                statement.setInt(2, shoeID);
                if (grade >= 1 && grade < 5) {
                    statement.setInt(3, grade);
                    statement.setInt(4, grade);
                    statement.setString(5, comment);
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

    // Få ut genomsnittsbetyget för en sko
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

    // Lägger till en lista av reviews för varje sko
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

    // Plockar ut kommentarerna från reviewsen för en viss sko
    public void getCommentsFromReviews(Shoes shoes) {
        List<Reviews> reviews = shoes.getReviewsList();
        System.out.println("Kommentarer:");
        for (Reviews r : reviews) {
            System.out.println(r.getComment());
        }

    }
}