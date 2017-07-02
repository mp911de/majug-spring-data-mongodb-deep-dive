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
package com.example.conversion;

import lombok.Data;
import lombok.Value;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Query;

import com.example.SimpleConfiguration;

/**
 * @author Mark Paluch
 */
public class Conversions {

	private static void doWithMongo(MongoOperations operations) {

		operations.remove(new Query(), Employee.class);

		Employee bender = new Employee();

		bender.setName("Bender");
		bender.setFavoriteDrinks(Arrays.asList("Beer", "Booze"));
		bender.setMood(Mood.of("Bite my shiny metal ass!"));
		bender.setBirthDate(LocalDate.parse("2996-01-01"));

		operations.insert(bender);

		Employee employee = operations.query(Employee.class).matching(new Query()).first().get();

		System.out.println(employee.getMood());

		org.bson.Document employeeDocument = operations.query(Employee.class)
				.inCollection("employee")
				.as(org.bson.Document.class)
				.matching(new Query())
				.first()
				.get();

		System.out.println(employeeDocument.get("mood"));
	}

	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CustomConfiguration.class);

		doWithMongo(context.getBean(MongoOperations.class));

		context.stop();
	}

	@Document
	@Data
	static class Employee {

		String name;
		List<String> favoriteDrinks;
		Mood mood;
		LocalDate birthDate;
	}

	@Value(staticConstructor = "of")
	static class Mood {
		String mood;
	}

	@Configuration
	static class CustomConfiguration extends SimpleConfiguration {

		@Override
		public CustomConversions customConversions() {
			return new MongoCustomConversions(Arrays.asList(MoodToStringConverter.INSTANCE, StringToMoodConverter.INSTANCE));
		}
	}

	enum MoodToStringConverter implements Converter<Mood, String> {

		INSTANCE;

		@Override
		public String convert(Mood source) {
			return source.getMood();
		}
	}

	enum StringToMoodConverter implements Converter<String, Mood> {

		INSTANCE;

		@Override
		public Mood convert(String source) {
			return Mood.of(source);
		}
	}

}
