
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class simulates a program that manages bus voyages.
 * It can process a list of commands from a file and perform actions
 * such as initializing voyages, printing voyage details, selling tickets,
 * refunding tickets, and canceling voyages.
 */
public class PlayGround {


    /**
     * This method reads the input file line by line and processes each line
     * based on the command it contains. It calls helper methods to handle
     * specific commands.
     *
     * @param args The command line arguments passed to the program.
     * @param items The lines read from the input file.
     * @param buses A HashMap that stores Bus objects identified by their voyage ID.
     */
    public void readProcessData(String[] args, String[] items, HashMap<Integer, Bus> buses) {
        if (!(args.length == 2)) {
            System.out.println("ERROR: This program works exactly with two command line arguments, the first one is the " +
                    "path to the input file whereas the second one is the path to the output file. Sample usage can be " +
                    "as follows: \"java8 BookingSystem input.txt output.txt\". Program is going to terminate!");
            System.exit(0);
        }

        String inputFile = args[0];
        if (!Files.exists(Paths.get(inputFile))) {
            System.out.println("ERROR: This program cannot read from the \""+ inputFile + "\", either this program does " +
                    "not have read permission to read that file or file does not exist. Program is going to terminate!");
            System.exit(1);
        }

        String[] inputContent = FileInput.readFile(inputFile, true, true);
        if (inputContent == null) {
            System.out.println("ERROR: This program cannot read from the \""+ inputFile + "\", either this program does " +
                    "not have read permission to read that file or file does not exist. Program is going to terminate!");
            System.exit(1);
        }

        String outputFile = args[1];
        Path outputPath = Paths.get(outputFile);

        if (!Files.isWritable(outputPath)) {
            System.out.println("ERROR: This program cannot write to the \"" + outputFile + "\", please check the permissions to write that directory. Program is going to terminate!");
            System.exit(1);
        }

        int loopAmount1 = 0; // Initialize the loop counter
        for (String line : items) {
            loopAmount1 += 1;
            String[] parts = line.split("\t");
            // Check for 'INIT_VOYAGE' command and process it accordingly
            if (parts[0].contains("INIT_VOYAGE")) {
                FileOutput.writeToFile(args[1], "COMMAND: " + line, true, true);
                initVoyage(args, line, buses);
                // Handle 'Z_REPORT' command with error checking for additional parts
            } else if (parts[0].contains("Z_REPORT")) {
                FileOutput.writeToFile(args[1], "COMMAND: " + line, true, true);

                if (parts.length > 1){
                    FileOutput.writeToFile(args[1], "ERROR: Erroneous usage of \"Z_REPORT\" command!", true, true);
                    continue;
                }

                FileOutput.writeToFile(args[1], "Z Report:", true, true);
                FileOutput.writeToFile(args[1], "----------------", true, true);
                zReport(args, buses,loopAmount1,items);
                if(loopAmount1 != items.length){
                    FileOutput.writeToFile(args[1], "----------------", true, true);
                } else if (loopAmount1 == items.length) {
                    FileOutput.writeToFile(args[1], "----------------", true, false);
                }

            } else if (parts[0].contains("PRINT_VOYAGE")) {
                FileOutput.writeToFile(args[1], "COMMAND: " + line, true, true);
                printVoyage(args, parts, buses);

            } else if (parts[0].contains("SELL_TICKET")) {
                FileOutput.writeToFile(args[1], "COMMAND: " + line, true, true);
                sellTicket(args, buses, parts);

            } else if (parts[0].contains("REFUND_TICKET")) {
                FileOutput.writeToFile(args[1], "COMMAND: " + line, true, true);
                refundTicket(args, buses, parts);

            } else if (parts[0].contains("CANCEL_VOYAGE")) {
                FileOutput.writeToFile(args[1], "COMMAND: " + line, true, true);
                cancelVoyage(args, buses, parts);

            }
            else {
                FileOutput.writeToFile(args[1], "COMMAND: " + line, true, true);
                FileOutput.writeToFile(args[1], "ERROR: There is no command namely " + parts[0] + "!", true, true);
            }

            if(loopAmount1 == items.length && !parts[0].equals("Z_REPORT")){
                FileOutput.writeToFile(args[1], "Z Report:", true, true);
                FileOutput.writeToFile( args[1], "----------------", true, true);
                zReport(args, buses,loopAmount1,items);
                FileOutput.writeToFile(args[1], "----------------", true, false);
            }
        }
        if(items.length == 0){
            FileOutput.writeToFile(args[1], "Z Report:", true, true);
            FileOutput.writeToFile( args[1], "----------------", true, true);
            zReport(args, buses,loopAmount1,items);
            FileOutput.writeToFile(args[1], "----------------", true, false);
        }
    }

    /**
     * This method processes the INIT_VOYAGE command. It creates a Bus object of the
     * appropriate type (Minibus, Standard, or Premium) and adds it to the HashMap 'buses'
     * using the voyage ID as the key. The method also writes messages to the output file
     * indicating the success or failure of the operation and any validation errors encountered.
     *
     * @param args The command line arguments passed to the program (unused in this method).
     * @param line The line containing the INIT_VOYAGE command and its arguments.
     * @param buses A HashMap that stores Bus objects identified by their voyage ID.
     * @return 0 if the voyage is initialized successfully, -1 otherwise.
     */
    public int initVoyage(String[] args, String line, HashMap<Integer, Bus> buses) {
        //creates an object to every line that included init voyage and add fields to them
        //writes errors about init voyage
        String[] parts2 = line.split("\t");

        if(parts2.length < 7){
            FileOutput.writeToFile(args[1], String.format("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!"), true, true);
            return -1;
        }

        if(!isNumeric(parts2[2])){
            FileOutput.writeToFile(args[1], String.format(Locale.US,"ERROR: %s is not a positive integer, ID of a voyage must be a positive integer!", parts2[2]), true, true);
            return -1;
        }
        int id = Integer.parseInt(parts2[2]);
        if (id <= 0) {
            FileOutput.writeToFile(args[1], String.format(Locale.US,"ERROR: %d is not a positive integer, ID of a voyage must be a positive integer!", id), true, true);
            return -1;
        }
        if (buses.containsKey(id)) {
            FileOutput.writeToFile(args[1], String.format(Locale.US,"ERROR: There is already a voyage with ID of %d!", id), true, true);
            return -1;
        }
        if(!isString(parts2[3])){
            FileOutput.writeToFile(args[1], String.format("ERROR: %s is not a string, deperture of a voyage must be a string!",parts2[3]), true, true);
            return -1;
        }
        if(!isString(parts2[4])){
            FileOutput.writeToFile(args[1], String.format("ERROR: %s is not a string, arrival of a voyage must be a string!",parts2[4]), true, true);
            return -1;
        }
        String from = parts2[3];
        String to = parts2[4];
        if(!isNumeric(parts2[5])){
            FileOutput.writeToFile(args[1], String.format(Locale.US, "ERROR: %d is not a positive integer, number of seat rows of a voyage must be a positive integer!", parts2[5]), true, true);
            return -1;
        }
        int rows = Integer.parseInt(parts2[5]);
        if (rows <= 0) {
            FileOutput.writeToFile(args[1], String.format(Locale.US, "ERROR: %d is not a positive integer, number of seat rows of a voyage must be a positive integer!", rows), true, true);
            return -1;
        }
        if(!isDouble(parts2[6])){
            FileOutput.writeToFile(args[1], String.format(Locale.US,"ERROR: %s is not a positive number, price must be a positive number!", parts2[6]), true, true);
            return -1;
        }
        double price = Double.parseDouble(parts2[6]);
        if (price <= 0) {
            FileOutput.writeToFile(args[1], String.format(Locale.US,"ERROR: %s is not a positive number, price must be a positive number!", parts2[6]), true, true);
            return -1;
        }

        if (parts2[1].equals("Minibus")) {
            Bus minibus = new Minibus(id,from,to,rows,price,0,0,0);
            buses.put(id, minibus);
            FileOutput.writeToFile(args[1], String.format(Locale.US,"Voyage %d was initialized as a minibus (2) voyage from %s to %s with %.2f TL priced %d regular seats. Note that minibus tickets are not refundable.", id, from, to, price, minibus.getCapacity()), true, true);

        } else if (parts2[1].equals("Standard")) {
            if(parts2.length < 8 || parts2.length > 8){
                FileOutput.writeToFile(args[1], String.format("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!"), true, true);
                return -1;
            }

            if(!isNumeric(parts2[7])){
                FileOutput.writeToFile(args[1], String.format("ERROR: %s is not an integer, refund cut of voyage must be an integer!",parts2[7]), true, true);
                return -1;
            }

            int refundCut = Integer.parseInt(parts2[7]);
            if (refundCut < 0 || refundCut > 100) {
                FileOutput.writeToFile(args[1], String.format(Locale.US,"ERROR: %d is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!", refundCut), true, true);
                return -1;
            } else {
                Bus standard = new Standard(id,from,to,rows,price,0,refundCut,0);
                buses.put(id, standard);
                FileOutput.writeToFile(args[1], String.format(Locale.US,"Voyage %d was initialized as a standard (2+2) voyage from %s to %s with %.2f TL priced %d regular seats. Note that refunds will be %d%% less than the paid amount.", id, from, to, price, standard.getCapacity(), refundCut), true, true);
            }

        } else if (parts2[1].equals("Premium")) {
            if(parts2.length < 9 || parts2.length > 9){
                FileOutput.writeToFile(args[1], String.format("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!"), true, true);
                return -1;
            }
            if(!isNumeric(parts2[7])){
                FileOutput.writeToFile(args[1], String.format("ERROR: %s is not an integer, refund cut of voyage must be an integer!",parts2[7]), true, true);
                return -1;
            }

            int refundCut = Integer.parseInt(parts2[7]);

            if(!isNumeric(parts2[8])){
                FileOutput.writeToFile(args[1], String.format("ERROR: %s is not an integer, premium fee of voyage must be an integer!", parts2[8]), true, true);
                return -1;
            }

            int premiumFee = Integer.parseInt(parts2[8]);

            if (refundCut < 0 || refundCut > 100) {
                FileOutput.writeToFile(args[1], String.format(Locale.US,"ERROR: %d is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!", refundCut), true, true);
            } else if (premiumFee < 0) {
                FileOutput.writeToFile(args[1], String.format(Locale.US,"ERROR: %d is not a non-negative integer, premium fee must be a non-negative integer!", premiumFee), true, true);
            } else {
                Bus premium = new Premium(id,from,to,rows,price,0,refundCut,premiumFee);
                buses.put(id, premium);
                int a = premium.getCapacity() / 3;
                double premiumPrice = price * (100 + premiumFee) / 100;
                FileOutput.writeToFile(args[1], String.format(Locale.US,"Voyage %d was initialized as a premium (1+2) voyage from %s to %s with %.2f TL priced %d regular seats and %.2f TL priced %d premium seats. Note that refunds will be %d%% less than the paid amount.", id, from, to, price, 2 * a, premiumPrice, a, refundCut), true, true);
            }
        } else {
            FileOutput.writeToFile(args[1], String.format("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!"), true, true);
        }
        return 0;
    }

    /**
     * This method generates a Z report that summarizes information about all voyages in the system.
     * The method iterates through the HashMap 'buses' and calls
     * the writeBusPlan() method of each Bus object to display its seat plan.
     *
     * @param args The command line arguments passed to the program (unused in this method).
     * @param buses A HashMap that stores Bus objects identified by their voyage ID.
     * @param loopAmount1 This parameter is likely used internally for loop management and can be ignored.
     * @param items This parameter is likely used internally for loop management and can be ignored.
     * @return -1 (The return value of this method seems to have no specific meaning and could be changed to void.)
     */
    public int zReport(String[] args, HashMap<Integer, Bus> buses,int loopAmount1, String[] items) {
        //write all voyages and their voyage id, from-to,bus plan, revenue

        if (buses.isEmpty()) {
            FileOutput.writeToFile(args[1], "No Voyages Available!", true, true);
        }
        else {
            int loopAmount = 0;
            for (Map.Entry<Integer, Bus> entry : buses.entrySet()) {
                loopAmount += 1;
                Bus bus = entry.getValue();
                FileOutput.writeToFile(args[1], "Voyage " + bus.getID(), true, true);
                FileOutput.writeToFile(args[1], bus.getFrom() + "-" + bus.getTo(), true, true);
                bus.writeBusPlan(args);
                FileOutput.writeToFile(args[1], "Revenue: " + String.format(Locale.US,"%.2f",bus.getRevenue()), true, true);
                if(buses.size() > 1 && loopAmount != buses.size()){
                    FileOutput.writeToFile(args[1], "----------------", true, true);
                }
            }
        }
        return -1;

    }

    /**
     * This method processes the PRINT_VOYAGE command. It takes the voyage ID from the command arguments
     * and retrieves the corresponding Bus object from the HashMap 'buses'. If the voyage ID is valid
     * and the Bus object exists, the method calls the writeBusPlan() method of the Bus object to display
     * its seat plan. Additionally, it writes details about the voyage, such as ID, origin-destination,
     * and total revenue, to the output file. The method returns 0 if the voyage is found and printed
     * successfully, -1 otherwise.
     *
     * @param args The command line arguments passed to the program.
     * @param parts The tokens obtained by splitting the command line. parts[0] should be "PRINT_VOYAGE" and parts[1] should be the voyage ID.
     * @param buses A HashMap that stores Bus objects identified by their voyage ID.
     * @return 0 if the voyage is found and printed successfully, -1 otherwise.
     */
    public int printVoyage(String[] args, String[] parts, HashMap<Integer, Bus> buses) {

        if (parts.length < 2 || parts.length > 2) {
            FileOutput.writeToFile(args[1], "ERROR: Erroneous usage of \"PRINT_VOYAGE\" command!", true, true);
            return -1;
        }

        if(!isNumeric(parts[1])){
            FileOutput.writeToFile(args[1], "ERROR: " + parts[1] + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
            return -1;
        }

        int id = Integer.parseInt(parts[1]);

        if (id <= 0) {
            FileOutput.writeToFile(args[1], "ERROR: " + parts[1] + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
            return -1;
        }
         if (!buses.containsKey(id)) {
            FileOutput.writeToFile(args[1], String.format("ERROR: There is no voyage with ID of %d!", id), true, true);
            return -1;
        }

        Bus targetBus = buses.get(id);

        FileOutput.writeToFile(args[1], "Voyage " + targetBus.getID(), true, true);
        FileOutput.writeToFile(args[1], targetBus.getFrom() + "-" + targetBus.getTo(), true, true);
        targetBus.writeBusPlan(args);
        FileOutput.writeToFile(args[1],String.format(Locale.US ,"Revenue: %.2f",targetBus.getRevenue()), true, true);

        return 0;
    }

    /**
     * Handles the SELL_TICKET command. It extracts the voyage ID and a list of seat numbers from the command's
     * arguments. The method validates the voyage ID to ensure it exists. If it doesn't or is invalid, an error
     * message is written, and the process stops.

     * If the voyage ID is valid, the method checks each seat number to ensure it's valid and not already sold.
     * If any check fails, an error message is logged. Otherwise, the method marks the seats as sold, calculates
     * the total price (considering premium seats for Premium voyages), and increments the voyage's revenue.

     * If all goes well, the method writes a success message to the output file with the sold seat numbers and
     * the total price.

     * @param args The command-line arguments passed to the program.
     * @param buses A HashMap containing Bus objects identified by their voyage ID.
     * @param parts The command line parts, where parts[0] is "SELL_TICKET", parts[1] is the voyage ID, and parts[2]
     * is a list of seat numbers separated by commas.
     * @return 0 if the tickets are sold successfully; otherwise, -1 if there's an error.
     */
    public int sellTicket(String[] args, HashMap<Integer, Bus> buses, String[] parts) {
        if (parts.length < 3 || parts.length > 3) {
            FileOutput.writeToFile(args[1], "ERROR: Erroneous usage of \"SELL_TICKET\" command!", true, true);
            return -1;
        }
        if(!isNumeric(parts[1])){
            FileOutput.writeToFile(args[1], String.format(Locale.US,"ERROR: %s is not a positive integer, ID of a voyage must be a positive integer!", parts[1]), true, true);
            return -1;
        }

        int id = Integer.parseInt(parts[1]);

        if(id < 0){
            FileOutput.writeToFile(args[1], String.format(Locale.US,"ERROR: %d is not a positive integer, ID of a voyage must be a positive integer!", id), true, true);
            return -1;
        }

        if (!buses.containsKey(id)) {
            FileOutput.writeToFile(args[1], String.format("ERROR: There is no voyage with ID of %s!", parts[1]), true, true);
            return -1;
        }

        String[] ticketParts = parts[2].split("_");
        int loopAmount2 = 0;
        String ticketsString = "";
        Bus bus = buses.get(id);
        boolean[] seats1 = bus.getSeats();
        double totalPrice = 0;

        for (String ticket : ticketParts) {
            if(!isNumeric(ticket)){
                FileOutput.writeToFile(args[1], String.format("ERROR: %d is not a positive integer, seat number must be a positive integer!",ticket), true, true);
                return -1;
            }

            int ticket1 = Integer.parseInt(ticket);

            if (ticket1 < 0) {
                FileOutput.writeToFile(args[1], String.format("ERROR: %d is not a positive integer, seat number must be a positive integer!", ticket1), true, true);
                return -1;
            }
            if (ticket1 > bus.getCapacity()) {
                FileOutput.writeToFile(args[1], "ERROR: There is no such a seat!", true, true);
                return -1;
            }
            if (seats1[ticket1 - 1] == true) {
                FileOutput.writeToFile(args[1], "ERROR: One or more seats already sold!", true, true);
                return -1;
            }
        }
        for (String ticket : ticketParts) {
            int ticket1 = Integer.parseInt(ticket);
            loopAmount2 += 1;

            if (loopAmount2 == ticketParts.length) {
                ticketsString = ticketsString + ticket;
            } else {
                ticketsString = ticketsString + ticket + "-";
            }

            if (bus instanceof Minibus) {
                seats1[ticket1 - 1] = true;
                if (loopAmount2 == ticketParts.length) {
                    FileOutput.writeToFile(args[1], String.format(Locale.US,"Seat %s of the Voyage %d from %s to %s was successfully sold for %.2f TL.", ticketsString, bus.getID(), bus.getFrom(), bus.getTo(), (bus.getPrice() * ticketParts.length)), true, true);
                    bus.increaseRevenue(bus.getPrice() * ticketParts.length);
                }

            } else if (bus instanceof Standard) {
                seats1[ticket1 - 1] = true;
                if (loopAmount2 == ticketParts.length) {
                    FileOutput.writeToFile(args[1], String.format(Locale.US,"Seat %s of the Voyage %d from %s to %s was successfully sold for %.2f TL.", ticketsString, bus.getID(), bus.getFrom(), bus.getTo(), (bus.getPrice() * ticketParts.length)), true, true);
                    bus.increaseRevenue(bus.getPrice() * ticketParts.length);
                }

            } else if (bus instanceof Premium) {
                if (ticket1 % 3 == 1) {
                    double premiumPrice = bus.getPrice() * (bus.getPremiumFee() + 100) / 100;
                    totalPrice += premiumPrice;
                } else {
                    totalPrice += bus.getPrice();
                }

                seats1[ticket1 - 1] = true;
                if (loopAmount2 == ticketParts.length) {
                    FileOutput.writeToFile(args[1], String.format(Locale.US,"Seat %s of the Voyage %d from %s to %s was successfully sold for %.2f TL.", ticketsString, bus.getID(), bus.getFrom(), bus.getTo(), totalPrice), true, true);
                    bus.increaseRevenue(totalPrice);
                }
            }

        }
        return 0;
    }

    /**
     * Processes the "REFUND_TICKET" command. It first checks if the command has the correct number of arguments.
     * If not, an error message is written to the output file. The method then validates the voyage ID. If it's
     * invalid or corresponds to a Minibus (which doesn't allow refunds), an error message is logged.

     * If the voyage exists and allows refunds, the method checks if the seat numbers are positive integers,
     * within capacity, and not already empty. If any validation fails, an error is written to the output file.

     * Once validated, the refund is processed. Seats are marked as available, the refund amount is calculated
     * differently for Standard and Premium buses, and the voyage's revenue is decreased accordingly. A success
     * message is logged after completion.

     * @param args The command-line arguments passed to the program.
     * @param buses A HashMap of Bus objects indexed by their voyage ID.
     * @param parts The command line parts where parts[0] is "REFUND_TICKET", parts[1] is the voyage ID,
     * and parts[2] is a list of seat numbers separated by underscores.
     * @return Returns 0 if the refund is successful; otherwise, it returns -1.
     */

    public int refundTicket(String[] args, HashMap<Integer, Bus> buses, String[] parts) {
        if (parts.length > 3 || parts.length < 3){
            FileOutput.writeToFile(args[1], String.format("ERROR: Erroneous usage of \"REFUND_TICKET\" command!"), true, true);
            return -1;
        }
        if(!isNumeric(parts[1])){
            FileOutput.writeToFile(args[1], String.format(Locale.US,"ERROR: %s is not a positive integer, ID of a voyage must be a positive integer!", parts[1]), true, true);
            return -1;
        }
        int id = Integer.parseInt(parts[1]);

        if(id < 0){
            FileOutput.writeToFile(args[1], String.format(Locale.US,"ERROR: %d is not a positive integer, ID of a voyage must be a positive integer!", id), true, true);
            return -1;
        }

        String[] tickets = parts[2].split("_");
        Bus bus = buses.get(id);


        if (!buses.containsKey(id)) {
            FileOutput.writeToFile(args[1], String.format("ERROR: There is no voyage with ID of %s!", parts[1]), true, true);
            return -1;
        }

        if(bus instanceof Minibus){
            FileOutput.writeToFile(args[1],"ERROR: Minibus tickets are not refundable!\n", true, true);
            return -1;
        }

        boolean[] seats = bus.getSeats();
        int loopAmount3 = 0;
        double totalAmount = 0;
        String seatString = "";

        for (String ticket : tickets) {
            if(!isNumeric(ticket)){
                FileOutput.writeToFile(args[1], String.format("ERROR: %s is not a positive integer, seat number must be a positive integer!", ticket), true, true);
                return -1;
            }
            int ticket1 = Integer.parseInt(ticket);
            loopAmount3 += 1;

            if (ticket1 < 0) {
                FileOutput.writeToFile(args[1], String.format("ERROR: %d is not a positive integer, seat number must be a positive integer!", ticket1), true, true);
                return -1;
            }

            if (bus.getCapacity() < ticket1) {
                FileOutput.writeToFile(args[1], "ERROR: There is no such a seat!", true, true);
                return -1;
            }

            if (seats[ticket1 - 1] == false) {
                FileOutput.writeToFile(args[1], "ERROR: One or more seats are already empty!", true, true);
                return -1;
            }
        }
        int loopAmount2 = 0;

        for (String ticket : tickets) {
            int ticket1 = Integer.parseInt(ticket);
            loopAmount2 += 1;

            if (loopAmount2 == tickets.length) {
                seatString = seatString + ticket;
            } else {
                seatString = seatString + ticket + "-";
            }

            if (bus instanceof Standard) {

                double refundCost = bus.getPrice() - (bus.getRefundCut() * bus.getPrice() / 100);

                seats[ticket1 - 1] = false;
                totalAmount = refundCost * tickets.length;
            }
            else if (bus instanceof Premium) {
                if (ticket1 % 3 == 1) {
                    double premiumCost = bus.getPrice() * (bus.getPremiumFee() + 100) / 100;
                    double refundPremium = premiumCost - (premiumCost * bus.getRefundCut() / 100);
                    totalAmount += refundPremium;

                } else {
                    double refundStandard = bus.getPrice() - (bus.getPrice() * bus.getRefundCut() / 100);
                    totalAmount += refundStandard;
                }

                seats[ticket1 - 1] = false;

            }
        }
        FileOutput.writeToFile(args[1], String.format(Locale.US,"Seat %s of the Voyage %d from %s to %s was successfully refunded for %.2f TL.", seatString, bus.getID(), bus.getFrom(), bus.getTo(), totalAmount), true, true);
        bus.decreaseRevenue(totalAmount);
        return 0;
    }

    /**
     * This method handles the "CANCEL_VOYAGE" command. It checks if the voyage ID is a positive integer and
     * whether it exists in the given HashMap of buses. If these conditions aren't met, an error message is
     * written to the output file.

     * If the voyage ID is valid, the method cancels the voyage by removing it from the HashMap and writes a
     * success message to the output file. It also adjusts the revenue based on the bus type and the number
     * of tickets that were sold before cancellation.

     * @param args The command-line arguments passed to the program.
     * @param buses A HashMap of Bus objects identified by their voyage ID.
     * @param parts The command line parts where parts[0] is "CANCEL_VOYAGE" and parts[1] is the voyage ID.
     * @return Returns 0 if the voyage is successfully cancelled, or -1 if there's an error.
     */
    public int cancelVoyage(String[] args, HashMap<Integer, Bus> buses, String[] parts) {
        if(parts.length > 2 || parts.length < 2){
            FileOutput.writeToFile(args[1], String.format("ERROR: Erroneous usage of \"CANCEL_VOYAGE\" command!"), true, true);
            return -1;
        }

        if(!isNumeric(parts[1])){
            FileOutput.writeToFile(args[1], String.format("ERROR: %s is not a positive integer, ID of a voyage must be a positive integer!", parts[1]), true, true);

        }
        int id = Integer.parseInt(parts[1]);
        if (id <= 0) {
            FileOutput.writeToFile(args[1], String.format("ERROR: %d is not a positive integer, ID of a voyage must be a positive integer!", id), true, true);
            return -1;
        }
        if (!buses.containsKey(id)) {
            FileOutput.writeToFile(args[1], String.format("ERROR: There is no voyage with ID of %d!", id), true, true);
            return -1;
        }

        Bus bus = buses.get(id);
        buses.remove(id);
        FileOutput.writeToFile(args[1], String.format("Voyage %d was successfully cancelled!", id), true, true);
        FileOutput.writeToFile(args[1], "Voyage details can be found below:", true, true);
        FileOutput.writeToFile(args[1], "Voyage " + id , true, true);
        FileOutput.writeToFile(args[1], bus.getFrom() + "-" + bus.getTo(), true, true);
        bus.writeBusPlan(args);

        int ticketAmount = 0;
        boolean[] seats = bus.getSeats();
        for (boolean seat : seats){
            if(seat == true){
                ticketAmount += 1;
            }
        }

        if (bus instanceof Minibus){
            bus.decreaseRevenue(ticketAmount * bus.getPrice());
        }
        else if (bus instanceof Standard ) {
            bus.decreaseRevenue(ticketAmount * bus.getPrice());

        }
        else {
            int i = 1;
            for (boolean seat : seats){
                if( seat == true && i % 3 == 1){
                    //premium koltuk
                    bus.decreaseRevenue(bus.getPrice()* (bus.getPremiumFee() + 100) / 100);
                }
                else if(seat == true && i % 3 != 1){
                    bus.decreaseRevenue(bus.getPrice());
                }
                i+=1;
            }
        }

        FileOutput.writeToFile(args[1], String.format(Locale.US,"Revenue: %.2f",bus.getRevenue()), true, true);

        return 0;
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true; // Başarılı olduysa bu bir sayıdır
        } catch (NumberFormatException e) {
            return false; // Bir hata oluşursa bu bir sayı değildir
        }
    }

    public static boolean isString(String str) {
        // Boş ya da sadece sayılardan oluşmuyorsa bu metindir
        return !isNumeric(str) && str.length() > 0;
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true; // Dönüşüm başarılı olursa bir double'dır
        } catch (NumberFormatException e) {
            return false; // Dönüşüm başarısız olursa bir double değildir
        }
    }
}