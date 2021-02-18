public class Sizes {
    private int sizeID;
    private double sizeNumber;

    public Sizes(int sizeID, double sizeNumber) {
        this.sizeID = sizeID;
        this.sizeNumber = sizeNumber;
    }

    public int getSizeID() {
        return sizeID;
    }

    public void setSizeID(int sizeID) {
        this.sizeID = sizeID;
    }

    public double getSizeNumber() {
        return sizeNumber;
    }

    public void setSizeNumber(double sizeNumber) {
        this.sizeNumber = sizeNumber;
    }
}
