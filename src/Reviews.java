public class Reviews {
    private int reviewID;
    private Customers customer;
    private Shoes shoe;
    private int numberRating;
    private String comment;

    public Reviews(int reviewID, Customers customer, Shoes shoe, int numberRating, String comment) {
        this.reviewID = reviewID;
        this.customer = customer;
        this.shoe = shoe;
        this.numberRating = numberRating;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

}