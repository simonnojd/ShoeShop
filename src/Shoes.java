public class Shoes {
    private int shoeID;
    private Prices price;
    private Sizes size;
    private Brands brand;
    private String shoeName;

    public Shoes(int shoeID, Prices price, Sizes size, Brands brand, String shoeName) {
        this.shoeID = shoeID;
        this.price = price;
        this.size = size;
        this.brand = brand;
        this.shoeName = shoeName;
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


    public String toString1() {
        return "Shoes{" +
                "shoeID=" + shoeID +
                ", price=" + price +
                ", size=" + size +
                ", brand=" + brand +
                ", shoeName='" + shoeName + '\'' +
                '}';
    }
}
