package ore;
import java.util.Properties;

public class Driver {
    public static final String DEFAULT_PROPERTIES_PATH = "properties/game2.properties";
    private static final String MAP = "map";

    public static void main(String[] args) {
        String propertiesPath = DEFAULT_PROPERTIES_PATH;
        if (args.length > 0) {
            propertiesPath = args[0];
        }
        final Properties properties = PropertiesLoader.loadPropertiesFile(propertiesPath);

        int model = Integer.parseInt(properties.getProperty(MAP));
        MapGrid grid = new MapGrid(model);

        String logResult = new OreSim(properties, grid).runApp(true);
        System.out.println("logResult = " + logResult);
    }
}