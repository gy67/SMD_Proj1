package ore.Machine;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ore.Element.*;
import ore.OreSim;

import java.awt.*;
import java.util.List;



public class Pusher extends Machine {
    private int moveCount = 0;
    private static final String FILE_NAME = "sprites/pusher.png";
    public Pusher()
    {
        super(true, FILE_NAME);  // Rotatable
    }
    public void setupPusher(boolean isAutoMode, List<String> controls) {
        if (isAutoMode) {
            setControls(controls);
        }
    }

    public boolean shouldMove(String machineType) {
        return OreSim.ElementType.PUSHER.getShortType().equals(machineType);
    }

    @Override
    protected void incrementMoveCount() {
        moveCount++;
    }

    @Override
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Check if we can move the pusher into the location
     * @param location
     * @return
     */
    @Override
    public boolean canMove(Location location) {
        if (!super.canMove(location)) {
            return false;
        }

        Rock rock = (Rock)gameGrid.getOneActorAt(location, Rock.class);
        Clay clay = (Clay)gameGrid.getOneActorAt(location, Clay.class);
        Bulldozer bulldozer = (Bulldozer)gameGrid.getOneActorAt(location, Bulldozer.class);
        Excavator excavator = (Excavator)gameGrid.getOneActorAt(location, Excavator.class);

        if (rock != null || clay != null || bulldozer != null || excavator != null)
            return false;
        else // Test if there is an ore
        {
            Ore ore = (Ore)gameGrid.getOneActorAt(location, Ore.class);
            if (ore != null)
            {
                // Try to move the ore
                ore.setDirection(getDirection());
                if (moveOre(ore))
                    return true;
                else
                    return false;
            }
        }
        return true;
    }

    /**
     * When the pusher pushes the ore in 1 direction, this method will be called to check if the ore can move in that direction
     *  and if it can move, then it changes the location
     * @param ore
     * @return
     */
    private boolean moveOre(Ore ore)
    {
        Location next = ore.getNextMoveLocation();
        // Test if try to move into border
        Color c = gameGrid.getBg().getColor(next);;
        Rock rock = (Rock)gameGrid.getOneActorAt(next, Rock.class);
        Clay clay = (Clay)gameGrid.getOneActorAt(next, Clay.class);
        if (c.equals(OreSim.getBorderColor()) || rock != null || clay != null)
            return false;

        // Test if there is another ore
        Ore neighbourOre =
                (Ore)gameGrid.getOneActorAt(next, Ore.class);
        if (neighbourOre != null)
            return false;

        // Reset the target if the ore is moved out of target
        Location currentLocation = ore.getLocation();
        List<Actor> actors =  gameGrid.getActorsAt(currentLocation);
        if (actors != null) {
            for (Actor actor : actors) {
                if (actor instanceof Target) {
                    Target currentTarget = (Target) actor;
                    currentTarget.show();
                    ore.show(0);
                }
            }
        }

        // Move the ore
        ore.setLocation(next);

        // Check if we are at a target
        Target nextTarget = (Target) gameGrid.getOneActorAt(next, Target.class);
        if (nextTarget != null) {
            ore.show(1);
            nextTarget.hide();
        } else {
            ore.show(0);
        }

        return true;
    }
}
