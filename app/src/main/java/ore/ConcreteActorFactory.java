package ore;

import ch.aplu.jgamegrid.Actor;
import ore.Element.Clay;
import ore.Element.Ore;
import ore.Element.Rock;
import ore.Element.Target;
import ore.Machine.Bulldozer;
import ore.Machine.Excavator;
import ore.Machine.Pusher;

import java.util.List;

/**
 * Factory class for creating concrete Actor instances based on the provided ElementType.
 */
public class ConcreteActorFactory{

    public Actor createActor(OreSim.ElementType type, boolean isAutoMode, List<String> controls) {
        switch (type) {
            case PUSHER:
                Pusher pusher = new Pusher();
                pusher.setupPusher(isAutoMode, controls);
                return pusher;
            case ORE:
                return new Ore();
            case TARGET:
                return new Target();
            case ROCK:
                return new Rock();
            case CLAY:
                return new Clay();
            case BULLDOZER:
                Bulldozer bulldozer = new Bulldozer();
                if (bulldozer != null) {
                    bulldozer.setupBulldozer(isAutoMode, controls);
                }
                return bulldozer;
            case EXCAVATOR:
                Excavator excavator = new Excavator();
                if (excavator != null) {
                    excavator.setupExcavator(isAutoMode, controls);
                }
                return excavator;
            default:
                // Invalid type, return null
                return null;
        }
    }
}