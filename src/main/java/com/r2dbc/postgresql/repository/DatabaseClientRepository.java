package com.r2dbc.postgresql.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import com.r2dbc.postgresql.model.City;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class DatabaseClientRepository {

	private final DatabaseClient databaseClient;

	public Flux<City> findByCityName(String name) {
		return this.databaseClient.select().from(City.class).matching(where("name").like(name)).fetch().all();
	}

	public Flux<City> findAll() {
		return this.databaseClient.select().from(City.class).fetch().all();
	}

	public Mono<City> findById(Integer id) {
		return this.databaseClient.select().from(City.class).matching(where("id").is(id)).fetch().one();
	}

	public Mono<Integer> save(City p) {
		return this.databaseClient.insert().into(City.class).using(p).fetch().one().map(m -> (Integer) m.get("id"));
	}

	public Mono<Integer> update(City p) {
		return this.databaseClient.update().table(City.class).using(p).fetch().rowsUpdated();
	}

	public Mono<Integer> deleteById(Integer id) {
		return this.databaseClient.delete().from(City.class).matching(where("id").is(id)).fetch().rowsUpdated();
	}

}
