public class Colors {
    private int colorID;
    private String colorName;

    public Colors(int colorID, String colorName) {
        this.colorID = colorID;
        this.colorName = colorName;
    }

    @Override
    public String toString() {
        return colorName;
    }
}
