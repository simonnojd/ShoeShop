public class ShoesInCategories {
    private int shoesInCategoriesID;
    private Shoes shoe;
    private Categories category;

    public ShoesInCategories(int shoesInCategoriesID, Shoes shoe, Categories category) {
        this.shoesInCategoriesID = shoesInCategoriesID;
        this.shoe = shoe;
        this.category = category;
    }

    public int getShoesInCategoriesID() {
        return shoesInCategoriesID;
    }

    public void setShoesInCategoriesID(int shoesInCategoriesID) {
        this.shoesInCategoriesID = shoesInCategoriesID;
    }

    public Shoes getShoe() {
        return shoe;
    }

    public void setShoe(Shoes shoe) {
        this.shoe = shoe;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }
}
