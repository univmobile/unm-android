package org.unpidf.univmobile.data.entities;

/**
 * Created by rviewniverse on 2015-01-27.
 */
public class NotificationEntity {
	private int id;
	private String notificationTime;
	private String content;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNotificationTime() {
		return notificationTime;
	}

	public void setNotificationTime(String notificationTime) {
		this.notificationTime = notificationTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
