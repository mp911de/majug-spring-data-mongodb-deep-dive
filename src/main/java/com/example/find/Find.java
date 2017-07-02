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
package com.example.find;

import static java.util.Spliterators.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;

import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.util.CloseableIterator;

import com.example.SimpleConfiguration;

/**
 * @author Mark Paluch
 */
public class Find {

	private static void doWithMongo(MongoOperations operations) {

		Employee employee = new Employee();

		employee.setName("Bender");
		employee.setFavoriteDrinks(Arrays.asList("Beer", "Booze"));
		employee.setMood("Bite my shiny metal ass!");
		employee.setBirthDate(LocalDate.parse("2996-01-01"));

		List<Employee> employees = operations.find(query(where("name").is("Bender")), Employee.class);

		for (Employee employee1 : employees) {
			System.out.println(employee1);
		}

		CloseableIterator<Employee> iterator = operations
				.query(Employee.class)
				.matching(query(where("name").is("Bender")))
				.stream();

		Stream<Employee> targetStream = StreamSupport.stream(spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);

		targetStream.filter(employee1 -> employee1.getName().equals("Bender"))
				.map(Employee::getFavoriteDrinks)
				.flatMap(Collection::stream)
				.forEach(System.out::println);

		iterator.close();
	}

	@Document
	@Data
	static class Employee {

		String name;

		List<String> favoriteDrinks;
		String mood;
		@Field("date_of_birth") LocalDate birthDate;
	}

	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SimpleConfiguration.class);

		doWithMongo(context.getBean(MongoOperations.class));

		context.stop();
	}
}
