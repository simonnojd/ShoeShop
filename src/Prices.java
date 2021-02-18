public class Prices {
    private int priceID;
    private double priceNumber;

    public Prices(int priceID, double priceNumber) {
        this.priceID = priceID;
        this.priceNumber = priceNumber;
    }

    public int getPriceID() {
        return priceID;
    }

    public void setPriceID(int priceID) {
        this.priceID = priceID;
    }

    public double getPriceNumber() {
        return priceNumber;
    }

    public void setPriceNumber(double priceNumber) {
        this.priceNumber = priceNumber;
    }


    public String toString1() {
        return "Prices{" +
                "priceNumber=" + priceNumber +
                '}';
    }
}
