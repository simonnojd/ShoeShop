import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Repository {

    private List<Shoes> shoeList = new ArrayList<>();
    private Properties properties = new Properties();
    private int counter = 1;

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
             ResultSet resultSet = statement.executeQuery("SELECT password, first_name FROM customers");)
             {
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
            chooseShoe(choice);
        }
        else System.out.println("Uppgifterna finns inte i systemet");
    }

    public List<Shoes> getAllShoes() {

        try (Connection connection = DriverManager.getConnection(properties.getProperty("connectionString"),
                properties.getProperty("name"),
                properties.getProperty("password"));
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM shoes JOIN prices ON price_id = prices.id JOIN sizes ON size_id = sizes.id JOIN brands ON brand_id = brands.id");)
        {
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
        for (int i = 1; i < shoeList.size(); i++) {
            if (i == choice){
                return shoeList.get(i - 1);
            }
        }
        return null;
    }

    public void addShoeToOrder() {

    }
}