/**
 * com.lodbrok.entity package contains the model objects
 */
package com.lodbrok.entity;

import static com.lodbrok.endpoint.tool.LagerthaFactory.LODBROK_DEATH_AGE;

import java.io.Serializable;

/**
 * Login class represents the Login model object
 * 
 * @author Fabio Riberto
 *
 */
public class Login implements Serializable {

	private static final long serialVersionUID = 4058678764699294450L;

	/**
	 * Actual time when the session start to be active
	 */
	private long bornTime;

	/**
	 * User id that requests to open a session
	 */
	private Integer userId;

	/**
	 * Session unique key
	 */
	private String sessionKey;

	/**
	 * New Login class
	 * 
	 * @param bornTime
	 * @param userId
	 * @param sessionKey
	 */
	public Login(long bornTime, Integer userId, String sessionKey) {
		this.setBornTime(bornTime);
		this.setUserId(userId);
		this.setSessionKey(sessionKey);
	}

	/**
	 * @return the bornTime
	 */
	public long getBornTime() {
		return bornTime;
	}

	/**
	 * @param bornTime
	 *            the bornTime to set
	 */
	public void setBornTime(long bornTime) {
		this.bornTime = bornTime;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @return the sessionKey
	 */
	public String getSessionKey() {
		return sessionKey;
	}

	/**
	 * @param sessionKey
	 *            the sessionKey to set
	 */
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		else if (obj == null || getClass() != obj.getClass())
			return false;
		else {
			Login loginObj = (Login) obj;
			if (getBornTime() != loginObj.bornTime)
				return false;
			else if (!getUserId().equals(loginObj.userId))
				return false;
			else if (getSessionKey() != null)
				if (!getSessionKey().equals(loginObj.sessionKey))
					return false;
				else if (loginObj.sessionKey != null)
					return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = LODBROK_DEATH_AGE; // prime number
		int bornTimeIntNum = (int) (bornTime ^ (bornTime >>> 32));
		int userIdNum = (userId != null ? userId.hashCode() : 0);
		int sessionKeyNum = (sessionKey != null ? sessionKey.hashCode() : 0);
		result += bornTimeIntNum + userIdNum + sessionKeyNum + 7;
		return result;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Login = [bornTime=");
		sb.append(getBornTime());
		sb.append(" userId=");
		sb.append(getUserId());
		sb.append(" sessionKey=");
		sb.append(getSessionKey());
		sb.append("]");
		return sb.toString();
	}
}
