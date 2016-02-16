/**
 * com.lodbrok.control package contains the controller files
 */
package com.lodbrok.control;

import static com.lodbrok.endpoint.tool.LagerthaFactory.SESSION_LIFE;
import static org.mockito.Mockito.when;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.lodbrok.entity.Login;

/**
 * @author Fabio Riberto
 * @RunWith MockitoJUnitRunner
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

	public static final int LEONIDA_FACTOR = 300;
	int timeWarp;
	String sessionKey;

	@InjectMocks
	private final LoginController lcon = new LoginController();

	@Mock
	private LoginController lconMock;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		timeWarp = (int) SESSION_LIFE / LEONIDA_FACTOR; // Wait only 2 seconds
														// instead 10 minutes!
		sessionKey = "";
	}

	@Test
	public void testInactiveSession() throws Exception {
		sessionKey = lcon.formatSessionKey(new SecureRandom());
		boolean isActive = lcon.isSessionActive(sessionKey);
		Assert.assertFalse("The session is active", isActive);
	}

	@Test
	public void testSessionLife() throws InterruptedException {
		int timeWarp = (int) SESSION_LIFE / LEONIDA_FACTOR; // Wait only 2
															// seconds instead
															// 10 minutes!
		when(lconMock.getSessionLife()).thenReturn(timeWarp);
		int userId = 444;
		Login login = lcon.openSession(userId);
		boolean isActive = lcon.isSessionActive(login.getSessionKey());
		Assert.assertTrue("The session is not active", isActive); // the session
																	// key is
																	// active at
																	// login
																	// time
		TimeUnit.MILLISECONDS.sleep(lconMock.getSessionLife());
		isActive = lcon.isSessionActive(login.getSessionKey());
		Assert.assertTrue("The session is active", isActive); // the session key
																// is no longer
																// active after
																// the
																// expiration
																// time
	}
}
