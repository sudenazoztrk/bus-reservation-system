import java.util.HashMap;

/**
 * This class is the main entry point for the program. It reads bus data from an input file, processes
 * the data, and potentially writes output to another file.
 *
 * @author [Your Name] (if applicable)
 */
public class BookingSystem {

    /**
     * The main method of the program.
     *
     * @param args The command line arguments passed to the program. The first argument specifies the input file path,
     *             and the second argument specifies the output file path (if used).
     */
    public static void main(String[] args) {
        // Read bus data from the input file
        String[] items = FileInput.readFile(args[0], true, true);  // Assuming FileInput class handles file reading

        // Clear the output file
        FileOutput.writeToFile(args[1], "", false, false);  // Assuming FileOutput class handles file writing

        // Create a HashMap to store buses keyed by their ID
        HashMap<Integer, Bus> buses = new HashMap<>();

        // Create a PlayGround object to process the bus data
        PlayGround playGround = new PlayGround();
        playGround.readProcessData(args, items, buses);
    }
}