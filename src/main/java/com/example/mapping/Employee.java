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
package com.example.mapping;

import static org.springframework.data.mongodb.core.index.GeoSpatialIndexType.*;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(language = "en")
@Data
public class Employee {

	@Id String id;

	@Indexed(sparse = true, name = "name_index") String name;

	@Version long version;

	@GeoSpatialIndexed(type = GEO_2DSPHERE) GeoJsonPoint location;

	@TextIndexed String resume;

	@DBRef Employee boss;

	List<String> favoriteDrinks;
	String mood;
	@Field("date_of_birth") LocalDate birthDate;
}
