/**
 * com.lodbrok.control package contains the controller files
 * 
 */
package com.lodbrok.control;

import static com.lodbrok.endpoint.tool.LagerthaFactory.SESSION_KEY_BIT_LENGTH;
import static com.lodbrok.endpoint.tool.LagerthaFactory.SESSION_LIFE;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.lodbrok.entity.Login;

/**
 * @author Fabio Riberto
 *
 *         LoginController class is in charge to process the login requests and
 *         validate the session keys
 * 
 */
public class LoginController {

	/**
	 * The aliveLogins attribute is a collection of the current active logins
	 * 
	 */
	private final ConcurrentMap<String, Login> aliveLogins;

	/**
	 * SessionWiper is a component that check the session key life time. It
	 * check when a session key is expired and wipe it out the aliveLogins map
	 * of active logins
	 * 
	 */
	private final SessionWiper sessionWiper;

	/**
	 * LoginController instantiation
	 * 
	 */
	public LoginController() {
		aliveLogins = new ConcurrentHashMap<>();
		sessionWiper = new SessionWiper(this);
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(sessionWiper, getSessionLife(),
				getSessionLife(), TimeUnit.MILLISECONDS);
	}

	/**
	 * This method get the pool of active session keys
	 * 
	 * @return a clone version of aliveLogins
	 * 
	 */
	public ConcurrentMap<String, Login> getAliveLogins() {
		return aliveLogins;
	}

	/**
	 * This method open a new session: insert the new session id into the pool
	 * of alive sessions
	 * 
	 * @param userId
	 * @return Login object
	 * 
	 */
	public synchronized Login openSession(final Integer userId) {
		final String sessionKey = formatSessionKey(new SecureRandom());
		final Login newSession = new Login(System.currentTimeMillis(), userId, sessionKey);
		aliveLogins.put(sessionKey, newSession);
		return newSession;
	}

	/**
	 * This method delete a session while it is expired. This shall be called
	 * from SessionWiper to check if there are sessions that can be removed
	 * because of the expiration time is reached
	 * 
	 */
	public synchronized void wipeExpiredSessions() {
		ArrayList<Login> al = new ArrayList<Login>(aliveLogins.values());
		do {
			Login login = al.iterator().next();
			if (getSessionLife() < (System.currentTimeMillis() - login.getBornTime()))
				aliveLogins.remove(login.getSessionKey());
		} while (al.iterator().hasNext());
	}

	/**
	 * This method check if a session is valid or over
	 *
	 * @param sessionKey
	 * @return true if the session is valid
	 * @return false if the session is over
	 * 
	 */
	public synchronized boolean isSessionActive(final String sessionKey) {
		if (aliveLogins.get(sessionKey) != null) {
			if (getSessionLife() < (System.currentTimeMillis() - aliveLogins.get(sessionKey).getBornTime())) {
				aliveLogins.remove(sessionKey);
				return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * This method generates and formats a new session key. The requirement is
	 * to have an uppercase string representing session i.e.: UICSNDK
	 * 
	 * @return the sessionKey as a String (Upper Case)
	 * 
	 */
	public String formatSessionKey(final SecureRandom random) {
		final String sessionKey = new BigInteger(SESSION_KEY_BIT_LENGTH, random).toString(SESSION_KEY_BIT_LENGTH)
				.toUpperCase();
		return sessionKey;
	}

	/**
	 * This method returns the session max life time in milliseconds
	 * 
	 * @return int the sessionLife
	 * 
	 */
	public int getSessionLife() {
		return SESSION_LIFE;
	}

	/**
	 * This class finds and wipes out the expired sessions based on a fixed
	 * timeout
	 * 
	 */
	class SessionWiper implements Runnable {

		/**
		 * LoginController attribute
		 * 
		 */
		private final LoginController loginCtrl;

		public SessionWiper(LoginController loginCtrl) {
			this.loginCtrl = loginCtrl;
		}

		@Override
		public void run() {
			loginCtrl.wipeExpiredSessions();
		}
	}
}
