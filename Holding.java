package market;

import edu.rutgers.cs112.Comparable112;

/**
 * A Holding represents a user's ownership of a specific stock in their portfolio.
 * It tracks the stock, quantity owned, total cost, and the day of purchase.
 * It implements Comparable112 to allow for comparison based on stock ID.
 * 
 * @author Vishal Saravanan
 * @author George Janulis
 * @author Sadhana Vasanthakumar
 */
public class Holding extends Comparable112<Holding> {
    
    private int id;
    private Stock stock;
    private int quantity; 
    private double cost; 
    private int purchaseDay;

    /**
     * Constructs a new Holding instance with the specified parameters.
     * Validates inputs to ensure they are within acceptable ranges.
     * 
     * @param ID
     * @param stock
     * @param quantity
     * @param cost
     * @param purchaseDay
     */
    public Holding(int ID, Stock stock, int quantity, double cost, int purchaseDay) {
        if (stock == null) {
            throw new IllegalArgumentException("Stock cannot be null");
        } else if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        } else if (cost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative");
        } else if (purchaseDay < 0 || purchaseDay >= stock.getPriceHistory().length) {
            throw new IllegalArgumentException("Purchase day is out of bounds");
        }
        this.id = ID;
        this.stock = stock;
        this.quantity = quantity;
        this.cost = cost;
        this.purchaseDay = purchaseDay;
    } 
 
    public int getPurchaseDay() {
        return purchaseDay;
    }

    public int getID() {
        return id;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }
 
    public void setQuantity(int qty) {
        quantity = qty;
        cost = qty * stock.getPriceHistory()[purchaseDay];
    }

    public int compareTo(Holding other) {
        return this.stock.getStockID().compareTo(other.stock.getStockID());
    }

    public double getCost() {
        return cost;
    }
 
    public String toString() {
        return "Holding{" +
                "stock=" + stock +
                ", quantity=" + quantity +
                '}';
    }
 
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Holding other = (Holding) obj;
        return id == other.id && stock.equals(other.stock) && quantity == other.quantity && cost == other.cost && purchaseDay == other.purchaseDay;
    }

}
