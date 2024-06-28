package ore;

import ch.aplu.jgamegrid.*;
import ore.Element.*;
import ore.Machine.*;

import java.util.List;

/**
 * Class for logging game actions and state.
 * Implements the LogHandler interface.
 */
public class GameLogger implements LogHandler{
    private GameGrid grid;
    private StringBuilder logResult;
    private int movementIndex;

    public GameLogger(GameGrid grid, StringBuilder logResult){
        this.grid = grid;
        this.logResult = logResult;
    }

    /**
     * Update the log with the current state of the game grid.
     * The log includes movement index, locations of various actors (pushers, ores, targets, etc.),
     * and is appended to the provided StringBuilder.
     */
    public void updateLog() {
        movementIndex++;
        List<Actor> pushers = grid.getActors(Pusher.class);
        List<Actor> ores = grid.getActors(Ore.class);
        List<Actor> targets = grid.getActors(Target.class);
        List<Actor> rocks = grid.getActors(Rock.class);
        List<Actor> clays = grid.getActors(Clay.class);
        List<Actor> bulldozers = grid.getActors(Bulldozer.class);
        List<Actor> excavators = grid.getActors(Excavator.class);

        logResult.append(movementIndex).append("#");
        logResult.append(OreSim.ElementType.PUSHER.getShortType()).append(actorLocations(pushers)).append("#");
        logResult.append(OreSim.ElementType.ORE.getShortType()).append(actorLocations(ores)).append("#");
        logResult.append(OreSim.ElementType.TARGET.getShortType()).append(actorLocations(targets)).append("#");
        logResult.append(OreSim.ElementType.ROCK.getShortType()).append(actorLocations(rocks)).append("#");
        logResult.append(OreSim.ElementType.CLAY.getShortType()).append(actorLocations(clays)).append("#");
        logResult.append(OreSim.ElementType.BULLDOZER.getShortType()).append(actorLocations(bulldozers)).append("#");
        logResult.append(OreSim.ElementType.EXCAVATOR.getShortType()).append(actorLocations(excavators));

        logResult.append("\n");
    }

    /**
     * Transform the list of actors to a string of location for a specific kind of actor.
     * @param actors
     * @return
     */
    private String actorLocations(List<Actor> actors) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean hasAddedColon = false;
        boolean hasAddedLastComma = false;
        for (int i = 0; i < actors.size(); i++) {
            Actor actor = actors.get(i);
            if (actor.isVisible()) {
                if (!hasAddedColon) {
                    stringBuilder.append(":");
                    hasAddedColon = true;
                }
                stringBuilder.append(actor.getX() + "-" + actor.getY());
                stringBuilder.append(",");
                hasAddedLastComma = true;
            }
        }

        if (hasAddedLastComma) {
            stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");
        }

        return stringBuilder.toString();
    }
}

