package market;

import java.util.ArrayList;
import edu.rutgers.cs112.LL.LLNode; 

/**
 * The StockMarket simulates a single user's stock trading activities over a period of time.
 * It manages stock data, user portfolio, transaction history, and account balance.
 *
 * Linked Lists are used for transaction history and portfolio holdings, while an array
 * is used for stock data.
 *
 * @author Vishal Saravanan
 * @author George Janulis 
 * @author Sadhana Vasanthakumar 
 */
public class StockMarket {
 
    private Stock[] stockData; // All stock data for the market
    private LLNode<Holding> portfolio; // User's current stock holdings
    private LLNode<Transaction> transactions; // History of all transactions
     
    private double balance; 
    private int currentDay; 
    private int transactionCount; 
    private int numHoldings;

    /**
     * Constructs a new StockMarket instance with the specified initial balance.
     * Initializes all data structures to null and sets default values for tracking variables.
     *
     * @param initialBalance the starting balance for trading operations
     */
    public StockMarket(double initialBalance) {
        this.stockData = null;
        this.portfolio = null;
        this.transactions = null;
        this.balance = initialBalance;
        this.currentDay = 0;
        this.transactionCount = 0;
        this.numHoldings = 0;
    }
   
    /**
     * Creates and initializes the stock data array from a CSV input file.
     * Parses the file to extract stock symbols and their historical price data,
     * creating Stock objects for each symbol with complete price history.
     * 
     * The first row contains a single integer, the number of unique stocks.
     * Each subsequent row contains the symbol for a single stock, followed by its price history.
     *
     * @param inputFile the path to the CSV file containing stock data. 
     */
    public void readStockData(String inputFile) {
        // WRITE YOUR CODE HERE 
        StdIn.setFile(inputFile);
        int n = Integer.parseInt(StdIn.readLine().trim());
        stockData = new Stock[n];
        int idx = 0;
        while (StdIn.hasNextLine() && idx < n) {
            String line = StdIn.readLine();
            if (line == null || line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            String symbol = parts[0].trim();
            double[] prices = new double[parts.length - 1];
            for (int i = 1; i < parts.length; i++) prices[i - 1] = Double.parseDouble(parts[i].trim());
            stockData[idx++] = new Stock(symbol, prices);
        }
        currentDay = 0;
        int x = 1;
        int c = 2;

    }
 
    /**
     * Advances the simulation to the next day.
     * Increment currentDay by 1. 
     * Then check if currentDay is less than the length of the price history of ANY stock.
     * (all stocks should have the same price history length)
     *
     * @return true if currentDay is less than the stock price history length
     */
    public boolean nextDay() { 
        // WRITE YOUR CODE HERE 
        currentDay++;
        if (stockData == null || stockData.length == 0) return false;
        int len = stockData[0].getPriceHistory().length;
        return currentDay < len;
    }

    /**
     * Updates the account balance based on a transaction.
     * Decreases balance for buy transactions and increases for sell transactions.
     *
     * @param qty the quantity of stocks involved in the transaction
     * @param pricePerShare the price per share at the time of transaction
     * @param type the transaction type (Transaction.BUY decreases balance, Transaction.SELL increases balance)
     */
    public void updateBalance(int quantity, double pricePerShare, String type) {
        // WRITE YOUR CODE HERE
        double total = quantity * pricePerShare;
        if (Transaction.BUY.equals(type)) balance -= total;
        else if (Transaction.SELL.equals(type)) balance += total;
    }

    /**
     * Executes a buy transaction for the specified stock and quantity.
     * Attempts to buy the stock if it exists and sufficient funds are available.
     * 
     * View the assignment description for exact implementation details.
     *
     * @param stockID the unique identifier of the stock to purchase
     * @param qty the quantity of shares to buy
     * @return true if transaction successful, false otherwise
     */
    public boolean buyStock(String stockID, int qty) {
        // WRITE YOUR CODE HERE
        if (qty <= 0) return false;
        Stock s = null;
        for (Stock st : stockData) if (st != null && st.getStockID().equals(stockID)) { s = st; break; }
        if (s == null) return false;
        if (currentDay >= s.getPriceHistory().length) return false;
        double price = s.getPriceHistory()[currentDay];
        double total = qty * price;
        if (balance < total) return false;
        int holdingID = getNextHoldingID();
        Holding lot = new Holding(holdingID, s, qty, total, currentDay);
        LLNode<Holding> newNode = new LLNode<Holding>(lot);
        newNode.setNext(portfolio);
        portfolio = newNode;
        numHoldings++;
        updateBalance(qty, price, Transaction.BUY);
        addTransaction(lot, qty, currentDay, Transaction.BUY);
        return true;
    }

    /**
     * Executes a sell transaction for the specified stock and quantity.
     * Attempts to sell the stock if a holding for it exists. May have to search through and sell 
     * multiple holdings to reach the desired quantity, and if so, should sell the highest value holdings first (ignoring their cost).
     * 
     * View the assignment description for exact implementation details.
     *
     * @param stockID the unique identifier of the stock to sell
     * @param qty the quantity of shares to sell
     * @return true if transaction successful, false otherwise
     */
    public boolean sellStock(String stockID, int qty) {
        // WRITE YOUR CODE HERE
        if (qty <= 0) return false;
        Stock s = null;
        for (Stock st : stockData) if (st != null && st.getStockID().equals(stockID)) { s = st; break; }
        if (s == null) return false;
        double[] prices = s.getPriceHistory();
        if (currentDay >= prices.length) return false;
        LLNode<Holding> bestPrev = null, bestNode = null, prev = null, cur = portfolio;
        int bestQty = -1;
        while (cur != null) {
            Holding h = cur.getData();
            if (h != null && h.getStock().getStockID().equals(stockID)) {
                if (h.getQuantity() > bestQty) { bestQty = h.getQuantity(); bestNode = cur; bestPrev = prev; }
            }
            prev = cur;
            cur = cur.getNext();
        }
        if (bestNode == null) return false;
        Holding chosen = bestNode.getData();
        double price = prices[currentDay];
        if (chosen.getQuantity() > qty) {
            chosen.setQuantity(chosen.getQuantity() - qty);
            updateBalance(qty, price, Transaction.SELL);
            addTransaction(chosen, qty, currentDay, Transaction.SELL);
            return true;
        } else {
            int sold = chosen.getQuantity();
            if (bestPrev == null) portfolio = portfolio.getNext();
            else bestPrev.setNext(bestNode.getNext());
            numHoldings--;
            updateBalance(sold, price, Transaction.SELL);
            addTransaction(chosen, sold, currentDay, Transaction.SELL);
            int remaining = qty - sold;
            if (remaining > 0) return sellStock(stockID, remaining) || sold > 0;
            return true;
        }
    }  
    
    /**
     * Adds a new transaction to the transaction history linked list.
     * Creates a Transaction object with the provided details and adds it to the front
     * of the transaction history list. Automatically assigns a unique transaction ID.
     *
     * @param s the Stock object involved in the transaction
     * @param transactionDay the day when the transaction occurred
     * @param type the type of transaction, Transaction.BUY or Transaction.SELL
     */
    public void addTransaction(Holding s, int quantity, int transactionDay, String type) {
        // WRITE YOUR CODE HERE
        int id = transactionCount;
        double pricePer = s.getStock().getPriceHistory()[transactionDay];
        double total = quantity * pricePer;
        Transaction t = new Transaction(id, transactionDay, s.getStock(), quantity, pricePer, total, type);
        LLNode<Transaction> newNode = new LLNode<Transaction>(t);
        newNode.setNext(transactions);
        transactions = newNode;
        transactionCount++;
    }
    
    /**
     * Calculates the Return on Investment (ROI) for a specific stock in the portfolio.
     * ROI is calculated as ((currentValue - totalCost) / totalCost) * 100.
     *
     * @param stockID the unique identifier of the stock to calculate ROI for
     * @return the ROI as a percentage (positive for gains, negative for losses),
     *         or 0.0 if the stock is not found or original price is zero
     */
    public double calculateROI(String stockID) {
        // WRITE YOUR CODE HERE
        Stock target = null;
        for (Stock st : stockData) if (st != null && st.getStockID().equals(stockID)) { target = st; break; }
        if (target == null) return 0.0;
        if (currentDay >= target.getPriceHistory().length) return 0.0;
        double today = target.getPriceHistory()[currentDay];
        double totalCost = 0.0, totalValue = 0.0;
        LLNode<Holding> cur = portfolio;
        while (cur != null) {
            Holding h = cur.getData();
            if (h != null && h.getStock().getStockID().equals(stockID) && h.getQuantity() > 0) {
                totalCost += h.getCost();
                totalValue += h.getQuantity() * today;
            }
            cur = cur.getNext();
        }
        if (totalCost == 0.0) return 0.0;
        return ((totalValue - totalCost) / totalCost) * 100.0;
    }

    /** 
     * Iterates through all portfolio holdings to determine which stocks represent
     * the maximum and minimum total profit values.
     *
     * @return a string array containing the two stock IDs for the extremas.
     */
    public String[] findExtrema() { 
        // WRITE YOUR CODE HERE
        if (portfolio == null) return new String[]{"", ""};
        String bestID = "", worstID = "";
        double best = Double.NEGATIVE_INFINITY, worst = Double.POSITIVE_INFINITY;
        boolean any = false;
        LLNode<Holding> cur = portfolio;
        while (cur != null) {
            Holding h = cur.getData();
            if (h != null && h.getQuantity() > 0) {
                double today = h.getStock().getPriceHistory()[currentDay];
                double profit = h.getQuantity() * today - h.getCost();
                if (profit > best) { best = profit; bestID = h.getStock().getStockID(); }
                if (profit < worst) { worst = profit; worstID = h.getStock().getStockID(); }
                any = true;
            }
            cur = cur.getNext();
        }
        if (!any) return new String[]{"", ""};
        return new String[]{bestID, worstID};      
    }

    // Getter Methods
    public Stock[] getStockData() {  return stockData; } 
    public LLNode<Holding> getPortfolio() {  return portfolio; } 
    public LLNode<Transaction> getTransactions() {  return transactions; } 
    public double getBalance() {  return balance; } 
    public int getCurrentDay() {  return currentDay; } 
    public int getTransactionCount() {  return transactionCount; } 
    public int getHoldings() {  return numHoldings; }

    public int getNextHoldingID() {
        LLNode<Transaction> current = transactions;
        int ID = 1;
        while (current != null) {
            if (current.getData().getType() == Transaction.BUY) {
                ID++;
            }
            current = current.getNext();
        }
        return ID;
    }
}
