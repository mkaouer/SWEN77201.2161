package org.eclipse.egit.github.core;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.egit.github.core.util.DateUtils;

public class UserStargazer implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	private Date starredAt;

	public UserStargazer setStarredAt(Date createdAt) {
		this.starredAt = DateUtils.clone(createdAt);
		return this;
	}

	public Date getStarredAt() {
		return DateUtils.clone(starredAt);
	}

	public UserStargazer setUser(User user)
	{
		this.user = user;
		return this;
	}

	public User getUser()
	{
		return user;
	}

}
