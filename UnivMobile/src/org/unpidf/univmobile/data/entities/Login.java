package org.unpidf.univmobile.data.entities;

/**
 * Created by rviewniverse on 2015-02-13.
 */
public class Login {

	private String name;
	private String token;
	private String id;
	private String mail;

	public Login() {
	}

	public Login(String name, String token, String id, String mail) {
		this.name = name;
		this.token = token;
		this.id = id;
		this.mail = mail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
}
