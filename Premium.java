/**
 * This class represents a Premium type of Bus in the system. Premium buses have a fixed seat layout
 * with three seats per row, with a premium seat fee charged for the center seat in each row.
 *
 * Properties inherited from Bus:
 *  - ID: Unique identifier for the bus.
 *  - from: Origin city of the voyage.
 *  - to: Destination city of the voyage.
 *  - rows: Number of rows in the bus.
 *  - price: Base price per seat (may be overridden to include premium fee).
 *  - revenue: Current revenue for the bus.
 *  - capacity: Total number of seats in the bus (automatically calculated as rows * 3).
 *  - seats: Boolean array representing seat availability (true for occupied, false for vacant).
 *  - refundCut: Percentage of ticket price deducted during a refund for Premium buses.
 *  - premiumFee: Additional fee charged for premium seats (center seat in each row).
 *
 */
public class Premium extends Bus{
    /**
     * Constructor for the Premium class. Initializes the object's properties specific to Premium buses.
     *
     * @param ID The unique identifier for the bus.
     * @param from The origin city of the voyage.
     * @param to The destination city of the voyage.
     * @param rows The number of rows in the bus.
     * @param price The base price per seat (may be overridden to include premium fee).
     * @param revenue The initial revenue for the bus (typically 0).
     * @param refundCut The percentage of ticket price deducted during a refund for Premium buses.
     * @param premiumFee The additional fee charged for premium seats (center seat in each row).
     */
    public Premium(int ID, String from, String to, int rows, double price, double revenue, int refundCut, int premiumFee) {
        super(ID, from, to, rows, price, revenue, refundCut, premiumFee);
        setCapacity(3 * rows);
        seats = new boolean[capacity];
        initializeSeats(this.seats, capacity);
    }

    /**
     * Initializes the seat availability array for the Premium bus. Sets all seats to vacant (false).
     *
     * @param seats The boolean array to be used for seat availability (true for occupied, false for vacant).
     * @param capacity The total number of seats in the bus.
     */
    public void initializeSeats(boolean[] seats, int capacity) {
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
     * Updates the bus revenue by subtracting the specified amount.
     *
     * @param amount The amount to be subtracted from the bus revenue.
     */
    public void decreaseRevenue(double amount){
        setRevenue(getRevenue() - amount);
    }

    /**
     * Writes the Premium bus seating plan to the output file. The plan uses "X" to represent occupied seats
     * and "*" for vacant seats, with separators ("|" and spaces) to visually group seats within each row.
     * Premium seats (center seat) are marked with a "P" within the asterisk (*).
     *
     * @param args The command line arguments passed to the program (unused in this method).
     */
    public void writeBusPlan(String[] args){
        int capacity = getCapacity();
        boolean[] seats = getSeats();
        for (int i = 0; i < capacity;i++){

            if (seats[i] == false && (i+1) % 3 == 0 ){
                FileOutput.writeToFile(args[1], "*", true, true);
            }
            else if(seats[i] == false && (i+1) % 3 == 2){
                FileOutput.writeToFile(args[1], " * ", true, false);
            }
            else if(seats[i] == false && (i+1) % 3 == 1){
                FileOutput.writeToFile(args[1], "* |", true, false);
            }
            else if(seats[i] == true && (i+1) % 3 == 0) {
                FileOutput.writeToFile(args[1], "X", true, true);
            }
            else if(seats[i] == true && (i+1) % 3 == 2){
                FileOutput.writeToFile(args[1], " X ", true, false);
            }
            else if (seats[i] == true && (i+1) % 3 == 1 ) {
                FileOutput.writeToFile(args[1], "X |", true, false);
            }
        }
    }

}
