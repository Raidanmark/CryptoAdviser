import java.util.Date;

public class PriceData {
    private Date date;
    private double price;

    public PriceData(Date date, double price) {
        this.date = date;
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }
}
