/*
 * Copyright 2017 the original author or authors.
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
package com.example.driver;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.SimpleConfiguration;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * @author Mark Paluch
 */
public class PlainMongoDB {

	private static void doWithMongo(MongoClient client) {

		MongoDatabase database = client.getDatabase("test");

		MongoCollection<Document> employee = database.getCollection("employee");

		Document document = new Document().append("name", "Bender")
				.append("favoriteDrinks", Arrays.asList("Beer", "Booze"))
				.append("mood", "Bite my shiny metal ass!")
				.append("date_of_birth", Date.valueOf(LocalDate.parse("2996-01-01")));

		employee.insertOne(document);

		List<Document> into = employee.find(new Document("name", "Bender")).into(new ArrayList<>());

		for (Document resultDocument : into) {
			System.out.println(resultDocument);
		}
	}

	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SimpleConfiguration.class);

		doWithMongo(context.getBean(MongoClient.class));

		context.stop();
	}
}
