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

    public void setShoeID(int shoeID) {
        this.shoeID = shoeID;
    }

    public Prices getPriceID() {
        return price;
    }

    public void setPriceID(Prices pricesID) {
        this.price = pricesID;
    }

    public Sizes getSizeID() {
        return size;
    }

    public void setSizeID(Sizes sizesID) {
        this.size = sizesID;
    }

    public Brands getBrand() {
        return brand;
    }

    public void setBrand(Brands brand) {
        this.brand = brand;
    }

    public String getShoeName() {
        return shoeName;
    }

    public void setShoeName(String shoeName) {
        this.shoeName = shoeName;
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
