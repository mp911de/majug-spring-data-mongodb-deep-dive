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

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Mark Paluch
 */
public interface EmployeeRepository extends CrudRepository<Employee, ObjectId> {

	List<Employee> findAllByFavoriteDrinksContains(String drink);

	@Query("{favoriteDrinks : ?0}")
	List<Employee> findByDrink(String drink);

	boolean existsByFavoriteDrinksContains(String drink);

	List<EmployeeProjection> findAllBy();
}
