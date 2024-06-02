package com.example.demo;

public class Person {

	String personId;
	String firstName;
	String lastName;

	public Person(String personId, String firstName, String lastName) {
		super();
		this.personId = personId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Person() {
		super();
		
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
