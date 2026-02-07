package market;
import java.util.Arrays;

import edu.rutgers.cs112.Comparable112;

/**
 * The Stock class represents a stock in the stock market simulation.
 * It contains attributes for the stock's ID symbol, and historical prices.
 * 
 * @author Vishal Saravanan
 * @author George Janulis
 * @author Sadhana Vasanthakumar
 */
public class Stock extends Comparable112<Stock>{  

    private String stockID; // Stock symbol (e.g., "AAPL", "GOOGL")  
    private double[] priceHistory; // Historical prices of the stock by day
  
    /**
     * Parameterized Constructor - Initializes a Stock object with the given stockID and priceHistory.
     * @param stockID
     * @param priceHistory
     */
    public Stock(String stockID, double[] priceHistory) {
        if (stockID == null || stockID.isEmpty()) {
            throw new IllegalArgumentException("Stock ID cannot be null or empty");
        } else if (priceHistory == null) {
            throw new IllegalArgumentException("Price history cannot be null");
        }
        this.stockID = stockID;  
        this.priceHistory = priceHistory;
    } 
 
    public String getStockID() {
        return stockID;
    }
 
    public void setStockID(String stockID){
        this.stockID = stockID;
    } 
 
    public double[] getPriceHistory() {
        return priceHistory;
    }   
 
    @Override
    public String toString() {
            return "Stock{" +
                    "stock=" + stockID +  
                    '}';
    }
 
    @Override
    public int compareTo(Stock s) {
        if (this.getStockID().compareTo(s.getStockID()) != 0) {
            return this.getStockID().compareTo(s.getStockID());
        } else {
            return Arrays.compare(this.priceHistory, s.priceHistory);
        }
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check for reference equality
        if (obj == null || getClass() != obj.getClass()) return false; // Check for null and class type (LLNode)
        Stock other = (Stock) obj; // Safe to cast now
        // Compare based on stockID (assuming stockID uniquely identifies a Stock)
        return this.getStockID().equals(other.getStockID()) && Arrays.equals(this.priceHistory, other.priceHistory);
    }
}
