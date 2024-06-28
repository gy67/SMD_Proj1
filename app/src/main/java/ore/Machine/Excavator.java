package ore.Machine;

import ch.aplu.jgamegrid.Location;
import ore.Element.Clay;
import ore.Element.Ore;
import ore.Element.Rock;

import java.util.List;

public class Excavator extends Machine {
    private int moveCount = 0;
    private int removeRock = 0;
    private static final String FILE_NAME = "sprites/excavator.png";
    public Excavator()
    {
        super(true, FILE_NAME);  // Rotatable
    }
    public void setupExcavator(boolean isAutoMode, List<String> controls) {
        if (isAutoMode) {
            setControls(controls);
        }
    }

    @Override
    public boolean shouldMove(String machineType) {
        return ore.OreSim.ElementType.EXCAVATOR.getShortType().equals(machineType);
    }

    @Override
    protected void incrementMoveCount() {
        moveCount++;
    }

    @Override
    public int getMoveCount() {
        return moveCount;
    }

    public int getRemoveMaterialCount(){
        return removeRock;
    }

    // Check if we can move the excavator into the location
    @Override
    public boolean canMove(Location location) {
        if (!super.canMove(location)) {
            return false;
        }

        Ore ore = (Ore)gameGrid.getOneActorAt(location, Ore.class);
        Clay clay = (Clay)gameGrid.getOneActorAt(location, Clay.class);
        Bulldozer bulldozer = (Bulldozer)gameGrid.getOneActorAt(location, Bulldozer.class);
        Pusher pusher = (Pusher) gameGrid.getOneActorAt(location, Pusher.class);
        Rock rock = (Rock)gameGrid.getOneActorAt(location, Rock.class);

        if (ore != null || clay != null || bulldozer != null || pusher != null)
            return false;
        else // Test if there is a rock
        {
            if (rock != null) {
                rock.removeSelf();
                removeRock++;
            }
        }
        return true;
    }
}
