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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class BillCrudToMongoDbIT {

    private MongoDB billDatabase;
    private MongoDB customerDatabase;
    private BillCrudToMongoDb billCrudToMongoDb;
    private CustomerCrudToMongoDb customerCrudToMongoDb;
    private static final Logger LOGGER = LogManager.getLogger(BillCrudToMongoDb.class);
    private final String collectionNameBills = "bills";
    private final String collectionNameCustomers = "customers";
    private MongoCollection<Bill> billMongoCollection;
    private MongoCollection<Customer> customerMongoCollection;

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
        CodecRegistry codecRegistryBills = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().register(Bill.class).
                        automatic(true).build()));
        CodecRegistry codecRegistryCustomers = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().register(Customer.class).
                        automatic(true).build()));

        //proper connection string format: https://docs.mongodb.com/manual/reference/connection-string/
        MongoClientSettings settings = MongoClientSettings.builder().codecRegistry(codecRegistryBills)
                .applyConnectionString(new ConnectionString( "mongodb://" + mongoContainer.getContainerIpAddress()
                        + ":" + mongoContainer.getMappedPort(27017)))
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        this.billDatabase = new MongoDB(mongoClient);
        this.billMongoCollection = billDatabase.getDatabase().getCollection(collectionNameBills, Bill.class);
        this.billCrudToMongoDb = new BillCrudToMongoDb(billDatabase);

        MongoClientSettings settingsCustomers = MongoClientSettings.builder().codecRegistry(codecRegistryCustomers)
                .applyConnectionString(new ConnectionString( "mongodb://" + mongoContainer.getContainerIpAddress()
                        + ":" + mongoContainer.getMappedPort(27017)))
                .build();

        MongoClient mongoClientCustomers = MongoClients.create(settingsCustomers);
        this.customerDatabase = new MongoDB(mongoClientCustomers);
        this.customerMongoCollection = customerDatabase.getDatabase().getCollection(collectionNameCustomers, Customer.class);
        this.customerCrudToMongoDb = new CustomerCrudToMongoDb(customerDatabase);
    }

    @Test
    void create() {
        //given
        this.billMongoCollection.drop();
        this.customerMongoCollection.drop();
        final OrderPosition orderPosition1= new OrderPosition("765f42cc-c319-41c0-9af6-f7c78cfdff7d", 3);
        final List<OrderPosition> articleList = new ArrayList<>();
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+41787894501").build();
        Customer skylerFromDB = customerCrudToMongoDb.upsert(skyler);
        final Bill bill = new Bill("5ecbbe074d75d712af925447", skylerFromDB.getUuid(), articleList, 47.55);
        //when
        Bill billFromDb = billCrudToMongoDb.create(bill);
        //then
        assertThat(billFromDb).isNotNull();
        assertThat(billFromDb.getCustomerID()).isEqualTo(skylerFromDB.getUuid());
        assertThat(billFromDb.getOrderID()).isEqualTo("5ecbbe074d75d712af925447");
        assertNotNull(bill.getId());
    }

    @Test
    void getBillByID() {
        //given
        this.billMongoCollection.drop();
        final OrderPosition orderPosition1= new OrderPosition("765f42cc-c319-41c0-9af6-f7c78cfdff7d", 3);
        final List<OrderPosition> articleList = new ArrayList<>();
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+41787894501").build();
        Customer skylerFromDB = customerCrudToMongoDb.upsert(skyler);
        final Bill bill = new Bill("5ecbbe074d75d712af925447", skylerFromDB.getUuid(), articleList, 47.55);
        //when
        Bill billCreated = billCrudToMongoDb.create(bill);
        Bill billFromDb = billCrudToMongoDb.getBillByID(billCreated.getId().toString());
        //then
        assertThat(billFromDb).isNotNull();
        assertThat(billFromDb.getCustomerID()).isEqualTo(skylerFromDB.getUuid());
        assertThat(billFromDb.getOrderID()).isEqualTo("5ecbbe074d75d712af925447");
        assertNotNull(bill.getId());
    }
}