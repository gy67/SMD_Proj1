package ore.Machine;

import ch.aplu.jgamegrid.*;
import ore.OreSim;

import java.awt.*;
import java.util.List;

/**
 * Abstract class representing a machine in the game.
 * Machines are Actors that can be controlled and move within the game grid.
 * Subclasses must implement methods to determine movement behavior and track move counts.
 */
public abstract class Machine extends Actor {
    private int id;
    private List<String> controls = null;
    private int autoMovementIndex = 0;
    public Machine(boolean isRotatable, String filename)
    {
        super(isRotatable, filename);  // Rotatable
    }

    public void setControls(List<String> controls) {
        this.controls = controls;
    }

    // Abstract method to determine if the machine should move based on the machine type
    public abstract boolean shouldMove(String machineType);
    // Abstract method to increment the move count
    protected abstract void incrementMoveCount();
    // Abstract method to get the move count
    public abstract int getMoveCount();

    // Set the ID of the machine.
    public void setId(int id){
        this.id = id;
    }

    // Perform the next automatic move of the machine based oncontrols.
    public void autoMoveNext() {
        if (controls != null && autoMovementIndex < controls.size()) {
            String[] currentMove = controls.get(autoMovementIndex).split("-");
            String machineType = currentMove[0]; // Assuming this is a machine identifier (e.g., "P" for Pusher)
            String moveDirection = currentMove[1]; // Assuming this is the direction of movement (e.g., "L", "U", "R", "D")
            autoMovementIndex++;

            // Check if the current machine should perform the move
            if (shouldMove(machineType)) {
                if (OreSim.getInstance().isFinished())
                    return;
                Location next = null;
                switch (moveDirection) {
                    case "L":
                        next = getLocation().getNeighbourLocation(Location.WEST);
                        setDirection(Location.WEST);
                        break;
                    case "U":
                        next = getLocation().getNeighbourLocation(Location.NORTH);
                        setDirection(Location.NORTH);
                        break;
                    case "R":
                        next = getLocation().getNeighbourLocation(Location.EAST);
                        setDirection(Location.EAST);
                        break;
                    case "D":
                        next = getLocation().getNeighbourLocation(Location.SOUTH);
                        setDirection(Location.SOUTH);
                        break;
                }

                if (next != null && canMove(next)) {
                    setLocation(next);
                    incrementMoveCount();
                }
                gameGrid.refresh();
            }
        }
    }

    public boolean canMove(Location location)
    {
        // Test if try to move into border, rock or clay
        Color c = gameGrid.getBg().getColor(location);
        if (c.equals(OreSim.getBorderColor())){
            return false;
        }
        return true;
    }



}
