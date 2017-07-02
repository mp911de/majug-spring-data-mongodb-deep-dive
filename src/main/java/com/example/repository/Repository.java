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
package com.example.repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.SimpleConfiguration;

/**
 * @author Mark Paluch
 */
public class Repository {

	private static void doWithMongo(EmployeeRepository repository) {

		Employee bender = getBender();
		Employee leela = getLeela();

		repository.deleteAll();

		repository.saveAll(Arrays.asList(bender, leela));

		Iterable<Employee> all = repository.findByDrink("Slurm");

		for (Employee employee : all) {
			System.out.println(employee);
		}

		List<EmployeeProjection> employees = repository.findAllBy();

		for (EmployeeProjection employee : employees) {
			System.out.println(employee.getName() + " - " + employee.getMood());
			System.out.println(employee.getDescription());
		}
	}

	private static Employee getBender() {

		Employee bender = new Employee();

		bender.setName("Bender");
		bender.setFavoriteDrinks(Arrays.asList("Beer", "Booze"));
		bender.setMood("Bite my shiny metal ass!");
		bender.setBirthDate(LocalDate.parse("2996-01-01"));

		return bender;
	}

	private static Employee getLeela() {

		Employee leela = new Employee();

		leela.setName("Mrs. Leela Fry");
		leela.setFavoriteDrinks(Arrays.asList("Slurm"));
		leela.setMood("I don't have good depth perception");
		leela.setBirthDate(LocalDate.parse("2975-07-29"));

		return leela;
	}

	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SimpleConfiguration.class);

		doWithMongo(context.getBean(EmployeeRepository.class));

		context.stop();
	}

}
