package ch.hslu.appe.entities;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class CustomerCrudToMongoDbTestIT {

    private MongoDB datebase;
    private CustomerCrudToMongoDb customerCrudToMongoDb;
    private static final Logger LOGGER = LogManager.getLogger(CustomerCrudToMongoDbTestIT.class);
    private final String collectionName = "customers";
    private MongoCollection<Customer> collection;

    @Container
    private static GenericContainer mongoContainer =
            new GenericContainer<>("hub.edu.abiz.ch/appe/system/mongodb:stable")//"docker.io/mongo:4.1.2")
            .withExposedPorts(27017);

    @BeforeEach
    void setUp() {
        //Codec (POJO Customer.class (instead of standard document)) needs to be registered manually,
        // as micronaut does not seem to do its magic here. See
        //https://stackoverflow.com/questions/31237849/
        // cant-find-a-codec-for-class-com-mongodb-basicdbobject-error-in-java-using-mondo
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().register(Customer.class).
                        automatic(true).build()));

        //proper connection string format: https://docs.mongodb.com/manual/reference/connection-string/
        MongoClientSettings settings = MongoClientSettings.builder().codecRegistry(codecRegistry)
                .applyConnectionString(new ConnectionString( "mongodb://" + mongoContainer.getContainerIpAddress()
                        + ":" + mongoContainer.getMappedPort(27017)))
                .build();
        MongoClient mongoClient = MongoClients.create(settings);

        this.datebase = new MongoDB(mongoClient);
        this.collection = datebase.getDatabase().getCollection(collectionName, Customer.class);
        this.customerCrudToMongoDb = new CustomerCrudToMongoDb(datebase);

    }

    @Test
    void testUpsertNoId() {
        this.collection.drop();
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+41787894501").build();
        Customer customerFromDB = customerCrudToMongoDb.upsert(skyler);
        assertThat(customerFromDB).isNotNull();
        assertThat(customerFromDB.getEmail()).isEqualTo("skyler.white@breakingbad.com");
        assertThat(customerFromDB.getFirstName()).isEqualTo("Skyler");
    }

    @Test
    void testUpsertWithId() {
        this.collection.drop();
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+41787894501").build();
        Customer customerFromDB = customerCrudToMongoDb.upsert(skyler);
        String uuid = customerFromDB.getUuid();
        LOGGER.debug("Customer Id is: " + uuid );
        Customer skylerUpdated = Customer.builder().uuid(uuid).firstName("SkylerUpdated").lastName("White").
                streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+41787894501").build();
        Customer skylerUpdatedFromDB = customerCrudToMongoDb.upsert(skylerUpdated);
        assertThat(skylerUpdatedFromDB).isNotNull();
        assertThat(skylerUpdatedFromDB.getEmail()).isEqualTo("skyler.white@breakingbad.com");
        assertThat(skylerUpdatedFromDB.getFirstName()).isEqualTo("SkylerUpdated");
    }

    @Test
    void testGetOneById() {
        this.collection.drop();
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+41787894501").build();
        Customer customerFromDB = customerCrudToMongoDb.upsert(skyler);
        String uuid = customerFromDB.getUuid();
        System.out.println(uuid);
        Customer skylerUpdatedFromDB = customerCrudToMongoDb.getOneById(uuid);
        System.out.println(skylerUpdatedFromDB);
        assertThat(skylerUpdatedFromDB).isNotNull();
        assertThat(skylerUpdatedFromDB.getEmail()).isEqualTo("skyler.white@breakingbad.com");
        assertThat(skylerUpdatedFromDB.getFirstName()).isEqualTo("Skyler");
    }

    @Test
    void testGetAll() {
        this.collection.drop();
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+4177 123 4567").build();
        Customer walter = Customer.builder().firstName("Walter").lastName("White").
                streetName("Wyoming Bldv").streetNumber("1501").zip("8002").city("Albuquerque")
                .email("walter.white@breakingbad.com").phone("+41787894502").build();
        Customer jesse = Customer.builder().firstName("Jesse").lastName("Pinkman").
                streetName("Street Northwest").streetNumber("322").zip("8003").city("Albuquerque")
                .email("jesse.pinkman@breakingbad.com").phone("+41787894503").build();
        Customer skylerFromDB = customerCrudToMongoDb.upsert(skyler);
        Customer walterFromDB = customerCrudToMongoDb.upsert(walter);
        Customer jesseFromDB = customerCrudToMongoDb.upsert(jesse);
        String uuidSkyler = skylerFromDB.getUuid();
        String uuidWalter = walterFromDB.getUuid();
        String uuidJesse = jesseFromDB.getUuid();
        LOGGER.info("Skyler: {}, Walter: {}, Jesse: {} ", uuidSkyler, uuidWalter, uuidJesse);
        Collection<Customer> customers = customerCrudToMongoDb.getAll();
        System.out.println(customers);
        assertThat(customers).isNotNull();
        assertThat(customers).hasSize(3);
        assertThat(customers.toString().contains("skyler.white@breakingbad.com")).isTrue();
        assertThat(customers.toString().contains("walter.white@breakingbad.com")).isTrue();
        assertThat(customers.toString().contains("jesse.pinkman@breakingbad.com")).isTrue();
    }

    @Test
    void testGetAllEmptyList() {
        this.collection.drop();
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+41787894501").build();
        Customer customerFromDB = customerCrudToMongoDb.upsert(skyler);
        String uuid = customerFromDB.getUuid();
        customerCrudToMongoDb.delete(uuid);
        Collection<Customer> customers = customerCrudToMongoDb.getAll();
        assertThat(customers).isNotNull();
        assertThat(customers).hasSize(0);
    }

    @Test
    void testFindByLastName() {
        this.collection.drop();
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+41787894501").build();
        Customer walter = Customer.builder().firstName("Walter").lastName("White").
                streetName("Wyoming Bldv").streetNumber("1501").zip("8002").city("Albuquerque")
                .email("walter.white@breakingbad.com").phone("+41787894502").build();
        Customer jesse = Customer.builder().firstName("Jesse").lastName("Pinkman").
                streetName("Street Northwest").streetNumber("322").zip("8003").city("Albuquerque")
                .email("walter.white@breakingbad.com").phone("+41787894503").build();
        customerCrudToMongoDb.upsert(skyler);
        customerCrudToMongoDb.upsert(walter);
        customerCrudToMongoDb.upsert(jesse);
        Collection<Customer> customers = customerCrudToMongoDb.findByLastName("White");
        assertThat(customers).isNotNull();
        assertThat(customers).hasSize(2);
        assertThat(customers.toString().contains("Skyler")).isTrue();
        assertThat(customers.toString().contains("Walter")).isTrue();
    }

    @Test
    void testFindByLastNameNotAvailable() {
        this.collection.drop();
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+41787894501").build();
        Customer walter = Customer.builder().firstName("Walter").lastName("White").
                streetName("Wyoming Bldv").streetNumber("1501").zip("8002").city("Albuquerque")
                .email("walter.white@breakingbad.com").phone("+41787894502").build();
        Customer jesse = Customer.builder().firstName("Jesse").lastName("Pinkman").
                streetName("Street Northwest").streetNumber("322").zip("8003").city("Albuquerque")
                .email("walter.white@breakingbad.com").phone("+41787894503").build();
        customerCrudToMongoDb.upsert(skyler);
        customerCrudToMongoDb.upsert(walter);
        customerCrudToMongoDb.upsert(jesse);
        Collection<Customer> customers = customerCrudToMongoDb.findByLastName("McGill");
        assertThat(customers).isNotNull();
        assertThat(customers).hasSize(0);
        assertThat(customers).extracting(Customer::getLastName)
                .doesNotContain("White");
    }

    @Test
    void testFindByLastNameEmptyDatabase() {
        this.collection.drop();
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+41787894501").build();
        Customer customerFromDB = customerCrudToMongoDb.upsert(skyler);
        String uuid = customerFromDB.getUuid();
        customerCrudToMongoDb.delete(uuid);
        Collection<Customer> customers = customerCrudToMongoDb.findByLastName("White");
        assertThat(customers).isNotNull();
        assertThat(customers).hasSize(0);
    }
}
