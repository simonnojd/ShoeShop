public class Categories {
    private int categoryID;
    private String categoryName;

    public Categories(int categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
