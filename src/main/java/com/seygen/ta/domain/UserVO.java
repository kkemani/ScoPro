package com.seygen.ta.domain;

import org.json.simple.JSONObject;

public class UserVO {

	private long id;
	private String firstName;
	private String lastName;
	private String userId;
	private String password;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", userId=" + userId
				+ ", password=" + password + "]";
	}

	public UserVO(JSONObject jsonObj) {
		setFirstName((String) ((JSONObject) jsonObj).get("firstName"));
		setLastName((String) ((JSONObject) jsonObj).get("lastName"));
		setPassword((String) ((JSONObject) jsonObj).get("password"));
		setUserId((String) ((JSONObject) jsonObj).get("userId"));
	}

}
