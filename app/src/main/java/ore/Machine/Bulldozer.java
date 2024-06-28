package ore.Machine;

import ch.aplu.jgamegrid.Location;
import ore.Element.Clay;
import ore.Element.Ore;
import ore.Element.Rock;
import ore.OreSim;

import java.util.List;

public class Bulldozer extends Machine {
    private int moveCount = 0;
    private int removeClay = 0;
    private static final String FILE_NAME = "sprites/bulldozer.png";
    public Bulldozer()
    {
        super(true, FILE_NAME);  // Rotatable
    }
    public void setupBulldozer(boolean isAutoMode, List<String> controls) {
        if (isAutoMode) {
            setControls(controls);
        }
    }

    @Override
    public boolean shouldMove(String machineType) {
        return OreSim.ElementType.BULLDOZER.getShortType().equals(machineType);
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
        return removeClay;
    }

    // Check if we can move the bulldozer into the location
    @Override
    public boolean canMove(Location location) {
        if (!super.canMove(location)) {
            return false;
        }

        Ore ore = (Ore)gameGrid.getOneActorAt(location, Ore.class);
        Rock rock = (Rock)gameGrid.getOneActorAt(location, Rock.class);
        Excavator excavator = (Excavator)gameGrid.getOneActorAt(location, Excavator.class);
        Pusher pusher = (Pusher) gameGrid.getOneActorAt(location, Pusher.class);
        Clay clay = (Clay)gameGrid.getOneActorAt(location, Clay.class);

        if (ore != null || rock != null || excavator != null || pusher != null) {
            return false;
        }
        // Test if there is a clay， count
        else if (clay != null) {
            clay.removeSelf();
            removeClay++;
        }
        return true;
    }
}
