/**
 * This class represents a Minibus type of Bus in the system. Minibuses have a fixed seat layout
 * with double occupancy per row. There are no refunds allowed for Minibus voyages.
 *
 * Properties inherited from Bus:
 *  - ID: Unique identifier for the bus.
 *  - from: Origin city of the voyage.
 *  - to: Destination city of the voyage.
 *  - rows: Number of rows in the bus.
 *  - price: Base price per seat.
 *  - revenue: Current revenue for the bus.
 *  - capacity: Total number of seats in the bus (automatically calculated as rows * 2).
 *  - seats: Boolean array representing seat availability (true for occupied, false for vacant).
 *
 */
public class Minibus extends Bus {

    /**
     * Constructor for the Minibus class. Initializes the object's properties specific to Minibuses.
     *
     * @param ID The unique identifier for the bus.
     * @param from The origin city of the voyage.
     * @param to The destination city of the voyage.
     * @param rows The number of rows in the bus.
     * @param price The base price per seat.
     * @param revenue The initial revenue for the bus (typically 0).
     * @param refundCut Since refunds are not allowed, this parameter is set to 0 for Minibuses.
     * @param premiumFee Since Minibuses don't have premium seats, this parameter is set to 0.
     */
    public Minibus(int ID, String from, String to, int rows, double price, double revenue, int refundCut, int premiumFee) {
        super(ID, from, to, rows, price, revenue, refundCut, premiumFee);
        setCapacity(2 * rows);
        seats = new boolean[capacity];
        initializeSeats(this.seats, capacity);
    }

    /**
     * Initializes the seat availability array for the Minibus. Sets all seats to vacant (false).
     *
     * @param seats The boolean array to be used for seat availability (true for occupied, false for vacant).
     * @param capacity The total number of seats in the bus.
     */
    public void initializeSeats(boolean[] seats,int capacity) {
        // false: boş, true : dolu
        for (int i = 0; i < capacity; i++) {
            seats[i] = false;
        }
    }

    /**
     * Updates the bus revenue by adding the specified amount.
     *
     * @param amount The amount to be added to the bus revenue.
     */
    public void increaseRevenue(double amount){
        setRevenue(getRevenue() + amount);
    }

    /**
     * Updates the bus revenue by subtracting the specified amount (not allowed for Minibuses).
     *
     * @param amount The amount to be subtracted from the bus revenue (ignored for Minibuses).
     */
    public void decreaseRevenue(double amount){
        setRevenue(getRevenue() - amount);
    }

    /**
     * Writes the Minibus seating plan to the output file. The plan uses "X" to represent occupied seats
     * and "*" for vacant seats, with a space in between seats on the same row and a newline after each row.
     *
     * @param args The command line arguments passed to the program (unused in this method).
     */
    public void writeBusPlan(String[] args){
        int capacity = getCapacity();
        boolean[] seats = getSeats();
        for (int i= 0; i < capacity; i++){
            if (seats[i] == false && (i+1) % 2 == 1){
                FileOutput.writeToFile(args[1], "* ", true, false);
            }
            else if (seats[i] == false && (i+1) % 2 == 0){
                FileOutput.writeToFile(args[1], "*", true, true);
            }
            else if(seats[i] == true && (i+1) % 2 == 1){
                FileOutput.writeToFile(args[1], "X ", true, false);
            }
            else if(seats[i] == true &&  (i+1) % 2 == 0){
                FileOutput.writeToFile(args[1], "X", true, true);
            }
        }
    }
}
