package com.r2dbc.postgresql.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("cities")
public class City {

	@Id
	private int id;
	private String name;
	private int population;

}
