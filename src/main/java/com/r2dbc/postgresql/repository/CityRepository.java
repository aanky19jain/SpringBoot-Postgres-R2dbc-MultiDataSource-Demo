package com.r2dbc.postgresql.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.r2dbc.postgresql.model.City;

import reactor.core.publisher.Flux;

public interface CityRepository extends ReactiveCrudRepository<City, Integer> {

	@Query("SELECT * FROM cities WHERE name like $1")
	Flux<City> findAllCitiesByName(String name);

}
