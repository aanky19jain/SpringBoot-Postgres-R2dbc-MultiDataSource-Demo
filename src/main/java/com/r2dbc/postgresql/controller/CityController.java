package com.r2dbc.postgresql.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.r2dbc.postgresql.model.City;
import com.r2dbc.postgresql.repository.CityRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CityController {

	@Autowired
	CityRepository repository;

	@GetMapping("/v1/cities")
	public Flux<City> getAllCities() {
		return repository.findAll();
	}

	@GetMapping("/v1/cities/{id}")
	public Mono<City> getCityById(@PathVariable("id") Integer id) {
		return repository.findById(id);
	}

	@PostMapping("/v1/cities")
	public Mono<City> createCity(@RequestBody City city) {
		return repository.save(city);
	}

	@DeleteMapping("/v1/cities/{id}")
	public Mono<Void> deleteCity(@PathVariable("id") Integer id) {
		return repository.deleteById(id);
	}

	@PutMapping("/v1/cities/{id}")
	public Mono<City> updateCity(@PathVariable("id") Integer id, @RequestBody City city) {
		return repository.findById(id).map(c -> {
			c.setName(city.getName());
			c.setPopulation(city.getPopulation());
			return c;
		}).flatMap(p -> repository.save(p));
	}

}
