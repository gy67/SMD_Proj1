package ore;

import ch.aplu.jgamegrid.*;
import ore.Element.*;
import ore.Machine.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.Properties;

public class OreSim extends GameGrid implements GGKeyListener
{
  /**
   * Enum Class for ElementType
   * Since we cannot change the test package, so we keep this class in OreSim
   */
  public enum ElementType{
    OUTSIDE("OS"), EMPTY("ET"), BORDER("BD"),
    PUSHER("P"), BULLDOZER("B"), EXCAVATOR("E"), ORE("O"),
    ROCK("R"), CLAY("C"), TARGET("T");
    private String shortType;

    ElementType(String shortType) {
      this.shortType = shortType;
    }

    public String getShortType() {
      return shortType;
    }

    public static ElementType getElementByShortType(String shortType) {
      ElementType[] types = ElementType.values();
      for (ElementType type: types) {
        if (type.getShortType().equals(shortType)) {
          return type;
        }
      }

      return ElementType.EMPTY;
    }
  }

  // attributes in OreSim controller
  private MapGrid grid;
  private GameLogger logger;
  private int nbHorzCells;
  private int nbVertCells;
  private static final int COLOR = 100;
  private final static Color borderColor = new Color(COLOR, COLOR, COLOR);
  private static final int CELL_SIZE = 30;
  private static final String SUCCESS_TITLE = "Mission Complete. Well done!";
  private static final String FAIL_TITLE = "Mission Failed. You ran out of time";
  private static final String AUTO = "auto";
  private static final String MOVEMENT_MODE = "movement.mode";
  private static final String DURATION = "duration";
  private static final String PERIOD = "simulationPeriod";
  private static final String MACHINE_MOVEMENT = "machines.movements";
  private Ore[] ores;
  private Target[] targets;
  private ArrayList<Pusher> pushers;
  private ArrayList<Bulldozer> bulldozers;
  private ArrayList<Excavator> excavators;
  private boolean isFinished = false;
  private static OreSim instance;
  private Properties properties;
  private boolean isAutoMode;
  private double gameDuration;
  private List<String> controls;
  private Statistics stats;
  private StringBuilder logResult = new StringBuilder();
  public OreSim(Properties properties, MapGrid grid)
  {
    super(grid.getNbHorzCells(), grid.getNbVertCells(), CELL_SIZE, false);
    this.grid = grid;
    logger = new GameLogger(this, logResult);
    nbHorzCells = grid.getNbHorzCells();
    nbVertCells = grid.getNbVertCells();
    this.properties = properties;
    instance = this;

    ores = new Ore[grid.getNbOres()];
    targets = new Target[grid.getNbOres()];
    this.pushers = new ArrayList<>();
    this.bulldozers = new ArrayList<>();
    this.excavators = new ArrayList<>();

    isAutoMode = properties.getProperty(MOVEMENT_MODE).equals(AUTO);
    gameDuration = Integer.parseInt(properties.getProperty(DURATION));
    setSimulationPeriod(Integer.parseInt(properties.getProperty(PERIOD)));
    controls = Arrays.asList(properties.getProperty(MACHINE_MOVEMENT).split(","));
  }


  /**
   * Returns the single instance of the OreSim class, ensuring that only one instance is created (singleton pattern).
   * If no instance has been created yet, this method will return null.
   * @return The single instance of OreSim if it exists; otherwise, null.
   */
  public static OreSim getInstance(){
    if (instance != null) {
      return instance;
    }
    return null;
  }

  /**
   * check current game finished or not
   * @return
   */
  public boolean isFinished() {
    return isFinished;
  }

  /**
   * get color of border in this game
   * @return
   */
  public static Color getBorderColor(){
    return borderColor;
  }

  /**
   * Check the number of ores that are collected
   * @return
   */

  private int checkOresDone() {
    int nbTarget = 0;
    for (int i = 0; i < grid.getNbOres(); i++)
    {
      if (ores[i].getIdVisible() == 1)
        nbTarget++;
    }

    return nbTarget;
  }

  /**
   * Sets unique IDs for each machine in the game.
   * @param pushers The list of all Pusher machines in the game.
   * @param bulldozers The list of all Bulldozer machines in the game.
   * @param excavators The list of all Excavator machines in the game.
   */
  public void setMachineID(ArrayList<Pusher> pushers, ArrayList<Bulldozer> bulldozers, ArrayList<Excavator> excavators){
    // set id for pushers
    for (int i = 0; i < pushers.size(); i++){
      // get current pusher
      Pusher pusher = pushers.get(i);
      pusher.setId(i);
    }

    // set id for bulldozers
    for (int i = 0; i < bulldozers.size(); i++){
      // get current bulldozer
      Bulldozer bulldozer = bulldozers.get(i);
      bulldozer.setId(i);
    }

    // set id for excavators
    for (int i = 0; i < excavators.size(); i++){
      // get current excavator
      Excavator excavator = excavators.get(i);
      excavator.setId(i);
    }
  }


  /**
   * Checks if the simulation has completed successfully or failed and updates the game title accordingly.
   * @param oresDone The number of ores successfully collected.
   */
  private void checkComplete(int oresDone){
    if (oresDone == grid.getNbOres()) {
      setTitle(SUCCESS_TITLE);
    } else if (gameDuration < 0) {
      setTitle(FAIL_TITLE);
    }
    isFinished = true;
  }


  /**
   * The main method to run the game
   * @param isDisplayingUI
   * @return
   */
  public String runApp(boolean isDisplayingUI) {
    GGBackground bg = getBg();
    drawBoard(bg);
    drawActors();
    stats = new Statistics(pushers, excavators, bulldozers);
    addKeyListener(this);
    if (isDisplayingUI) {
      show();
    }

    if (isAutoMode) {
      doRun();
    }

    int oresDone = checkOresDone();
    double ONE_SECOND = 1000.0;
    while(oresDone < grid.getNbOres() && gameDuration >= 0) {
      try {
        Thread.sleep(simulationPeriod);
        double minusDuration = (simulationPeriod / ONE_SECOND);
        gameDuration -= minusDuration;
        String title = String.format("# Ores at Target: %d. Time left: %.2f seconds", oresDone, gameDuration);
        setTitle(title);
        if (isAutoMode) {
          // iterate all pushers
          for (Pusher pusher : pushers) {
            pusher.autoMoveNext();
          }

          // iterate all excavators
          if (!excavators.isEmpty()) {
            for (Excavator excavator : excavators) {
              excavator.autoMoveNext();
            }
          }

          // iterate all bulldozers
          if (!bulldozers.isEmpty()) {
            for (Bulldozer bulldozer : bulldozers) {
              bulldozer.autoMoveNext();
            }
          }

          logger.updateLog();
          //updateLogResult();
        }

        oresDone = checkOresDone();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    doPause();
    checkComplete(oresDone);
    stats.updateStatistics();
    isFinished = true;

    return logResult.toString();
  }

    /**
     * Draw all different actors on the board: pusher, ore, target, rock, clay, bulldozer, excavator
     * */
  private void drawActors() {
    int oreIndex = 0;
    int targetIndex = 0;
    ConcreteActorFactory factory = new ConcreteActorFactory();

    for (int y = 0; y < nbVertCells; y++) {
      for (int x = 0; x < nbHorzCells; x++) {
        Location location = new Location(x, y);
        ElementType elementType = grid.getCell(location);
        Actor actor = factory.createActor(elementType, isAutoMode, controls);

        if (actor != null) {
          addActor(actor, location);
          // Maintain specific collections for certain actor types
          switch (elementType) {
            case PUSHER:
              pushers.add((Pusher) actor);
              break;
            case ORE:
              ores[oreIndex++] = (Ore) actor;
              break;
            case TARGET:
              targets[targetIndex++] = (Target) actor;
              break;
            case BULLDOZER:
              bulldozers.add((Bulldozer) actor);
              break;
            case EXCAVATOR:
              excavators.add((Excavator) actor);
              break;
            default:
              break;
          }
        }
      }
    }
    System.out.println("ores = " + Arrays.asList(ores));
    setPaintOrder(Target.class);
  }

  /**
   * Draw the basic board with outside color and border color
   * @param bg
   */
  private void drawBoard(GGBackground bg)
  {
    bg.clear(new Color(230, 230, 230));
    bg.setPaintColor(Color.darkGray);
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        Location location = new Location(x, y);
        ElementType elementType = grid.getCell(location);
        if (elementType != ElementType.OUTSIDE)
        {
          bg.fillCell(location, Color.lightGray);
        }
        if (elementType == ElementType.BORDER)  // Border
          bg.fillCell(location, borderColor);
      }
    }
  }

  /**
   * The method is automatically called by the framework when a key is pressed. Based on the pressed key, the pusher
   *  will change the direction and move
   * @param evt
   * @return
   */
  public boolean keyPressed(KeyEvent evt)
  {
    if (isFinished)
      return true;

    for (Pusher pusher : pushers) { // iterate all pushers
      Location next = null;
      switch (evt.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          next = pusher.getLocation().getNeighbourLocation(Location.WEST);
          pusher.setDirection(Location.WEST);
          break;
        case KeyEvent.VK_UP:
          next = pusher.getLocation().getNeighbourLocation(Location.NORTH);
          pusher.setDirection(Location.NORTH);
          break;
        case KeyEvent.VK_RIGHT:
          next = pusher.getLocation().getNeighbourLocation(Location.EAST);
          pusher.setDirection(Location.EAST);
          break;
        case KeyEvent.VK_DOWN:
          next = pusher.getLocation().getNeighbourLocation(Location.SOUTH);
          pusher.setDirection(Location.SOUTH);
          break;
      }

      Target curTarget = (Target) getOneActorAt(pusher.getLocation(), Target.class);
      if (curTarget != null) {
        curTarget.show();
      }

      if (next != null && pusher.canMove(next)) {
        pusher.setLocation(next);
        logger.updateLog();
      }
    }
    refresh();
    return true;
  }

  public boolean keyReleased(KeyEvent evt)
  {
    return true;
  }

}

