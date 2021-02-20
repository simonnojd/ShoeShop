public class Reviews {
    private int reviewID;
    private Customers customer;
    private Shoes shoe;
    private int numberRating;

    enum rating {
        MISSNÖJD,
        GANSKA_NÖJD,
        NÖJD,
        MYCKET_NÖJD
    }
    private String comment;

    public Reviews(int reviewID, Customers customer, Shoes shoe, int numberRating, String comment) {
        this.reviewID = reviewID;
        this.customer = customer;
        this.shoe = shoe;
        this.numberRating = numberRating;
        this.comment = comment;
    }


    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

    public Shoes getShoe() {
        return shoe;
    }

    public void setShoe(Shoes shoe) {
        this.shoe = shoe;
    }

    public int getNumberRating() {
        return numberRating;
    }

    public void setNumberRating(int numberRating) {
        this.numberRating = numberRating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}