package market;
import edu.rutgers.cs112.Comparable112;

/**
 * Records details of each buy/sell transaction.
 * 
 * @author Vishal Saravanan
 * @author George Janulis
 * @author Sadhana Vasanthakumar
 */
public class Transaction extends Comparable112<Transaction> {

    public static final String BUY = "BUY";
    public static final String SELL = "SELL";
    
    private int transactionID;
    private int date;
    private Stock stock;
    private int quantity;
    private double pricePerStock;
    private double totalPrice;
    private String type;

    /**
     * Constructor to initialize a Transaction with details.
     *
     * @param transactionID Unique identifier for the transaction.
     * @param date Day of the transaction.
     * @param stock Stock involved in the transaction.
     * @param quantity Number of shares traded.
     * @param pricePerStock Price per share at the time of transaction.
     * @param totalPrice Total value of the transaction.
     * @param type Type of transaction (BUY or SELL).
     */
    public Transaction(int transactionID, int date, Stock stock, 
            int quantity, double pricePerStock, double totalPrice, String type) {
        if ((!type.equals(BUY) && !type.equals(SELL)) || type == null) {
            throw new IllegalArgumentException("Transaction type must be either 'BUY' or 'SELL'");
        } else if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive integer");
        } else if (pricePerStock < 0) {
            throw new IllegalArgumentException("Price per stock cannot be negative");
        } else if (totalPrice < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        } else if (stock == null) {
            throw new IllegalArgumentException("Stock cannot be null");
        }
        this.transactionID = transactionID;
        this.date = date;
        this.stock = stock;
        this.quantity = quantity;
        this.pricePerStock = pricePerStock;
        this.totalPrice = totalPrice;
        this.type = type;
    }
 
    public int getTransactionID(){
        return transactionID;
    }
 
    public int getDate(){
        return date;
    }
   
    public Stock getStock(){
        return stock;
    } 
    
    public int getQuantity(){
        return quantity;
    }
  
    public double getPricePerStock(){
        return pricePerStock;
    } 
      
    public double getTotalPrice(){
        return totalPrice;
    }
     
    public String getType(){
        return type;
    } 
 
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionID='" + transactionID + '\'' +
                ", date=" + date +
                ", stock='" + stock.getStockID() + '\'' +
                ", quantity=" + quantity +
                ", pricePerStock=" + pricePerStock +
                ", totalPrice=" + totalPrice +
                ", type=" + type +
                '}';
    }
 
    @Override
    public int compareTo(Transaction t) {
        double priceDiff = this.getTotalPrice() - t.getTotalPrice();
        if (priceDiff == 0) {
            return 0;
        } else if (priceDiff > 0) {
            return 1;
        }
        return -1;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; 
        if (!(obj instanceof Transaction other)) return false;  
        return this.getTransactionID() == other.getTransactionID() && this.getDate() == other.getDate() && this.getStock().equals(other.getStock()) && this.getQuantity() == other.getQuantity() && this.getPricePerStock() == other.getPricePerStock() && this.getTotalPrice() == other.getTotalPrice() && this.getType().equals(other.getType()); //int comparison
    }
}
