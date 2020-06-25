package ch.hslu.appe.mongoDB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This Class is used to read the MongoDB configurations from a ConfigFile.
 *
 * @since 08.04.2020
 * @author Frederico Fischer
 */
public class MongoDbConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MongoDbConfig.class);
    private static final String CONNECTION_ADDRESS = "connection";
    private static final String CONFIG_FILE_NAME =  "mongodb.properties";

    // MongoDB Values
    public static final String DATABASE = "orderManagement";
    public static final String ORDER_COLLECTION = "orders";

    private final Properties properties = new Properties();


    /**
     * Liest das Konfiguratiosfile der MongoDB ein.
     */
    public MongoDbConfig() {
        this(CONFIG_FILE_NAME);
    }

    MongoDbConfig(final String configFileName) {
        try {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFileName);
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException | NullPointerException e) {
            LOG.error("Error while reading from file {}", CONFIG_FILE_NAME);
            LOG.error("Will connect now to MongoDB on localhost with port 27020");
            this.properties.put("connection", "mongodb://localhost:27020");
        }
    }

    /**
     * Liefert das Attribut "Connection" zurück.
     * (Mac = 0.0.0.0, Windows = 192.168.99.10X, productiveEnvironment = XYZ)
     * @return Connection-Adresse
     */
    String getConnectionAdress() {
        return this.properties.getProperty(CONNECTION_ADDRESS);
    }




}
