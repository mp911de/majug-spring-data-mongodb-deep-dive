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
package com.example.geo;

import java.util.Arrays;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.example.SimpleConfiguration;

/**
 * @author Mark Paluch
 */
public class GeoRepository {

	private static void doWithMongo(LocationRepository repository) {

		Location nny = new Location("New New York",
				new GeoJsonPoint(40.7505474, -74.0189433));
		Location timeSquare = new Location("Time Square",
				new GeoJsonPoint(40.758895, -73.985131));
		Location carnegieHall = new Location("Carnegie Hall",
				new GeoJsonPoint(40.7505474, -74.0189433));
		Location flatironBuilding = new Location("Flatiron Building",
				new GeoJsonPoint(40.7410105, -73.9918425));
		Location columbiaU = new Location("Columbia University",
				new GeoJsonPoint(40.8075355, -73.9625727));

		repository.deleteAll();
		repository.saveAll(Arrays.asList(nny, timeSquare, carnegieHall, flatironBuilding, columbiaU));

		GeoResults<Location> near = repository.findByLocationNear(timeSquare.getLocation(), new Distance(5, Metrics.MILES));

		System.out.println(near.getAverageDistance());
		System.out.println(near.getContent());
	}

	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SimpleConfiguration.class);

		doWithMongo(context.getBean(LocationRepository.class));

		context.stop();
	}

}
