/*
 * Copyright 2019 Roland Gisler, HSLU Informatik, Switzerland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.hslu.appe.micro;

import ch.hslu.appe.entities.LocalWarehouse;
import ch.hslu.appe.entities.OSValidator;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Configuration;
import io.micronaut.context.annotation.PropertySource;
import io.micronaut.context.env.Environment;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.Micronaut;

/**
 * Applikationskontext für micronaut.io.
 */
public class Application {

    /**
     * Privater Konstruktor.
     */
    private Application() {
    }

    /**
     * Startet den Service.
     * @param args not used.
     */
    public static void main(final String[] args) {
        ApplicationContext context = Micronaut.run(Application.class);
        LocalWarehouse localWarehouse = LocalWarehouse.getInstance();
        localWarehouse.init(5, "Bern_Bahnhof");
    }
}