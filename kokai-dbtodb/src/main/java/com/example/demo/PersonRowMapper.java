package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PersonRowMapper implements RowMapper<Person> {

	String personId;
	String firstName;
	String lastName;
	
	public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Person pre = new Person(personId, firstName, lastName);
	
		pre.setPersonId(rs.getString("person_id"));
		pre.setFirstName(rs.getString("first_name"));
		pre.setLastName(rs.getString("last_name"));
		
	
	    return pre;
	}

}
