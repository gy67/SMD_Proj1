package ore;

import ore.Machine.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for handling and updating statistics related to pushers, excavators, and bulldozers.
 * Implements the StatisticsHandler interface.
 */
public class Statistics implements StatisticsHandler {
    private ArrayList<Pusher> pushers;
    private ArrayList<Excavator> excavators;
    private ArrayList<Bulldozer> bulldozers;
    private static final String FILE_NAME = "statistics.txt";

    public Statistics(ArrayList<Pusher> pushers, ArrayList<Excavator> excavators, ArrayList<Bulldozer> bulldozers) {
        this.pushers = pushers;
        this.excavators = excavators;
        this.bulldozers = bulldozers;
    }

    /**
     * Update the statistics by writing them to a file.
     * Each pusher's move count is written to the file.
     * Each excavator's move count and rock removal count are written to the file.
     * Each bulldozer's move count and clay removal count are written to the file.
     */
    public void updateStatistics() {
        File statisticsFile = new File(FILE_NAME);
        try (FileWriter fileWriter = new FileWriter(statisticsFile)) {
            // iterate all pushers
            for (Pusher pusher : pushers) {
                fileWriter.write(String.format("Pusher-1 Moves: %d\n", pusher.getMoveCount()));
            }

            // iterate all excavators
            if (!excavators.isEmpty()) {
                for (Excavator excavator : excavators) {
                    fileWriter.write(String.format("Excavator-1 Moves: %d\n", excavator.getMoveCount()));
                    fileWriter.write(String.format("Excavator-1 Rock removed: %d\n", excavator.getRemoveMaterialCount()));
                }
            }

            // iterate all bulldozers
            if (!bulldozers.isEmpty()) {
                for (Bulldozer bulldozer : bulldozers) {
                    fileWriter.write(String.format("Bulldozer-1 Moves: %d\n", bulldozer.getMoveCount()));
                    fileWriter.write(String.format("Bulldozer-1 Clay removed: %d\n", bulldozer.getRemoveMaterialCount()));
                }
            }

        } catch (IOException e) {
            System.out.println("Cannot write to file - e: " + e.getLocalizedMessage());
        }
    }
}


