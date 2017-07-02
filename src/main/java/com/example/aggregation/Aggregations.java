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
package com.example.aggregation;

import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Query;

import com.example.SimpleConfiguration;

/**
 * @author Mark Paluch
 */
public class Aggregations {

	private static void doWithMongo(MongoOperations operations) {

		operations.remove(new Query(), Employee.class);

		Employee bender = new Employee();

		bender.setName("Bender");
		bender.setFavoriteDrinks(Arrays.asList("Beer", "Booze"));
		bender.setMood("Bite my shiny metal ass!");
		bender.setBirthDate(LocalDate.parse("2996-01-01"));
		bender.setAge(4);

		Employee leela = new Employee();

		leela.setName("Mrs. Leela Fry");
		leela.setFavoriteDrinks(Collections.singletonList("Slurm"));
		leela.setMood("I don't have good depth perception");
		leela.setBirthDate(LocalDate.parse("2975-07-29"));
		leela.setAge(25);

		operations.insertAll(Arrays.asList(bender, leela));

	}

	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SimpleConfiguration.class);

		doWithMongo(context.getBean(MongoOperations.class));

		context.stop();
	}

	@Document
	@Data
	static class Employee {

		String name;
		List<String> favoriteDrinks;
		String mood;
		LocalDate birthDate;
		int age;
	}

}
