/**
 * This abstract class represents a Bus in the system. It provides the common properties and functionalities
 * shared by all concrete Bus types (Minibus, Standard, Premium). Each Bus object has a unique ID, origin,
 * destination, number of rows, base price, current revenue, refund cut percentage, premium seat fee (applicable
 * to Premium buses only), and a boolean array representing the seat availability (true for occupied, false for vacant).
 *
 * Subclasses of Bus (Minibus, Standard, Premium) are responsible for implementing the abstract methods:
 *  - increaseRevenue(amount): Updates the bus revenue by adding the specified amount.
 *  - decreaseRevenue(amount): Updates the bus revenue by subtracting the specified amount.
 *  - initializeSeats(seats, capacity): Initializes the seat availability array based on the bus capacity.
 *  - writeBusPlan(args): Writes the bus seating plan details to the output file (implementation varies for each Bus type).
 *
 */
abstract class Bus {
    private int ID;
    private String from;
    private String to;
    private int rows;
    private double price;
    private double revenue = 0;
    protected int capacity;
    private int refundCut;
    private int premiumFee;
    protected boolean[] seats;

    /**
     * Constructor for the Bus class. Initializes the object's properties.
     *
     * @param ID The unique identifier for the bus.
     * @param from The origin city of the voyage.
     * @param to The destination city of the voyage.
     * @param rows The number of rows in the bus.
     * @param price The base price per seat (may be overridden for Premium buses).
     * @param revenue The initial revenue for the bus (typically 0).
     * @param refundCut The percentage of ticket price deducted during a refund.
     * @param premiumFee The additional fee charged for premium seats (applicable to Premium buses only).
     */
    public Bus(int ID, String from, String to, int rows, double price, double revenue, int refundCut, int premiumFee) {
        this.ID = ID;
        this.from = from;
        this.to = to;
        this.rows = rows;
        this.price = price;
        this.revenue = revenue;
        this.refundCut = refundCut;
        this.premiumFee = premiumFee;
    }

    /**
     * Abstract method to be implemented by subclasses. Updates the bus revenue by adding the specified amount.
     *
     * @param amount The amount to be added to the bus revenue.
     */
    abstract void increaseRevenue(double amount);

    /**
     * Abstract method to be implemented by subclasses. Updates the bus revenue by subtracting the specified amount.
     *
     * @param amount The amount to be subtracted from the bus revenue.
     */
    abstract void decreaseRevenue(double amount);

    /**
     * Abstract method to be implemented by subclasses. Initializes the seat availability array based on the bus capacity.
     *
     * @param seats The boolean array to be used for seat availability (true for occupied, false for vacant).
     * @param capacity The total number of seats in the bus.
     */
    abstract void initializeSeats(boolean[] seats, int capacity);

    /**
     * Abstract method to be implemented by subclasses. Writes the bus seating plan details to the output file
     * (implementation varies for each Bus type).
     *
     * @param args The command line arguments passed to the program (may or may not be used in this method).
     */
    abstract void writeBusPlan(String[] args);

    // Getters and Setters for all Bus properties
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRefundCut() {
        return refundCut;
    }

    public void setRefundCut(int refundCut) {
        this.refundCut = refundCut;
    }

    public int getPremiumFee() {
        return premiumFee;
    }

    public void setPremiumFee(int premiumFee) {
        this.premiumFee = premiumFee;
    }

    public boolean[] getSeats() {
        return seats;
    }

    public void setSeats(boolean[] seats) {
        this.seats = seats;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
