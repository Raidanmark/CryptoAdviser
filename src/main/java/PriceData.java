import java.util.Date;

public class PriceData {
    private Date date;  // Variable to store the date of the price data
    private double price; // Variable to store the price value

    // Constructor to initialize PriceData with date and price
    public PriceData(Date date, double price) {
        this.date = date; // Assign the provided date to the date variable
        this.price = price; // Assign the provided price to the price variable
    }

    // Getter method to retrieve the date
    public Date getDate() {
        return date; // Return the date of the price data
    }

    // Getter method to retrieve the price
    public double getPrice() {
        return price; //Return the price value
    }
}
