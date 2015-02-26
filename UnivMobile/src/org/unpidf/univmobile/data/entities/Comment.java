package org.unpidf.univmobile.data.entities;

/**
 * Created by rviewniverse on 2015-02-11.
 */
public class Comment {
	private int id;
	private String message;
	private boolean active;
	private String author;
	private String postedOn;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPostedOn() {
		return postedOn;
	}

	public void setPostedOn(String postedOn) {
		this.postedOn = postedOn;
	}
}
