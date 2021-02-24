import java.util.ArrayList;
import java.util.List;

public class Shoes {
    private int shoeID;
    private Prices price;
    private Sizes size;
    private Brands brand;
    private String shoeName;
    private List<Colors> colorsList = new ArrayList<>();
    private List<Categories> categoriesList = new ArrayList<>();
    private List<Reviews> reviewsList = new ArrayList<>();

    public Shoes(int shoeID, Prices price, Sizes size, Brands brand, String shoeName) {
        this.shoeID = shoeID;
        this.price = price;
        this.size = size;
        this.brand = brand;
        this.shoeName = shoeName;
    }

    public void addToColorList(Colors color){
        colorsList.add(color);
    }

    public void addToCategoryList(Categories category){
        categoriesList.add(category);
    }

    public void addToReviewsList(Reviews reviews) { reviewsList.add(reviews);}

    public List<Colors> getColorsList() {
        return colorsList;
    }

    public List<Categories> getCategoriesList() {
        return categoriesList;
    }

    public List<Reviews> getReviewsList() {
        return reviewsList;
    }

    public int getShoeID() { return shoeID; }

    public Prices getPriceID() {
        return price;
    }


    public Sizes getSizeID() {
        return size;
    }

    public Brands getBrand() {
        return brand;
    }

    public String getShoeName() {
        return shoeName;
    }


    @Override
    public String toString() {
        return "Shoes: " +
                "\nprice: " + price +
                "\nsize: " + size +
                "\nbrand: " + brand +
                "\nshoeName: " + shoeName +
                "\ncolorsList: " + colorsList +
                "\ncategoriesList: " + categoriesList;
    }
}
