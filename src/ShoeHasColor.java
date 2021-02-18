public class ShoeHasColor {
    private int shoeHasColorID;
    private Shoes shoe;
    private Colors color;

    public ShoeHasColor(int shoeHasColorID, Shoes shoe, Colors color) {
        this.shoeHasColorID = shoeHasColorID;
        this.shoe = shoe;
        this.color = color;
    }

    public int getShoeHasColorID() {
        return shoeHasColorID;
    }

    public void setShoeHasColorID(int shoeHasColorID) {
        this.shoeHasColorID = shoeHasColorID;
    }

    public Shoes getShoe() {
        return shoe;
    }

    public void setShoe(Shoes shoe) {
        this.shoe = shoe;
    }

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }
}
