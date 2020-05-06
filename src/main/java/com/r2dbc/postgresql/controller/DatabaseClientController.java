package com.r2dbc.postgresql.controller;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.r2dbc.postgresql.model.City;
import com.r2dbc.postgresql.repository.DatabaseClientRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class DatabaseClientController {

	@Autowired
	DatabaseClientRepository repository;

	@GetMapping("/v2/cities")
	public Flux<City> getAllCities() {
		return repository.findAll();
	}

	@GetMapping("/v2/cities/{id}")
	public Mono<City> getCityById(@PathVariable("id") Integer id) {
		return repository.findById(id);
	}

	@GetMapping("/v2/citiesByName")
	public Flux<City> getAllCityByName(@RequestParam("name") String name) {
		return repository.findByCityName(name);
	}

	@DeleteMapping("/v2/cities/{id}")
	public Mono<ResponseEntity<String>> deleteCity(@PathVariable("id") Integer id) {
		return repository.deleteById(id).map(n -> n > 0 ? noContent().build() : notFound().build());
	}

	@PostMapping("/v2/cities")
	public Mono<ResponseEntity<String>> createCity(@RequestBody City city) {
		return repository.save(city).map(n -> created(URI.create("/v2/cities" + n)).build());
	}

	@PutMapping("/v2/cities/{id}")
	public Mono<ResponseEntity<Object>> updateCity(@PathVariable("id") Integer id, @RequestBody City city) {
		return repository.findById(id).map(c -> {
			c.setName(city.getName());
			c.setPopulation(city.getPopulation());
			return c;
		}).flatMap(repository::update).map(n -> n > 0 ? noContent().build() : notFound().build())
				.defaultIfEmpty(notFound().build());
	}

}
