package ch.hslu.appe.entities;

import com.mongodb.client.MongoDatabase;

/**
 * Provides a database of type MongoDatabase.
 */
public interface Datebases {
    MongoDatabase getDatabase();
}
