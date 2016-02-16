/**
 * com.lodbrok.control package contains the controller files
 */
package com.lodbrok.control;

import com.lodbrok.entity.Score;
import com.lodbrok.exception.InvalidSessionException;

/**
 * @author Fabio Riberto
 *
 *         LodbrokController class is in charge to route the requests to the
 *         proper service controller.
 */
public class LodbrokController {

	/**
	 * LoginController: a component that manage the login activities
	 */
	private final LoginController loginCtrl;

	/**
	 * ScoreController: a component that manage the score storaging and score
	 * high list retrieving activities
	 */
	private final ScoreController scoreCtrl;

	/**
	 * LodbrokController constructor
	 */
	public LodbrokController() {
		scoreCtrl = new ScoreController();
		loginCtrl = new LoginController();
	}

	/**
	 * LodbrokController unique instance: this class delegates the operations to
	 * proper specialized controller
	 */
	public volatile static LodbrokController controlInstance;

	/**
	 * This method return the LodbrokController unique instance
	 * 
	 * @return LodbrokController unique instance: this class delegates the
	 *         operations to proper specialized controller
	 */
	public static LodbrokController getInstance() {
		// Out-of-order writes + Double-checked locking
		if (controlInstance == null)
			synchronized (LodbrokController.class) {
				LodbrokController inst = controlInstance;
				if (inst == null) {
					synchronized (LodbrokController.class) {
						inst = new LodbrokController();
						controlInstance = new LodbrokController();
					}
					controlInstance = inst;
				}
			}
		return controlInstance;
		// Singleton classic style
		// if (controlInstance == null) {
		// synchronized (LodbrokController.class) {
		// if (controlInstance == null) {
		// controlInstance = new LodbrokController();
		// }
		// }
		// }
		// return controlInstance;
	}

	/**
	 * Score service: Check that the session is valid. Case session is valid
	 * then call ScoreController.storeAScore() method to try to insert new score
	 * for a particular level
	 *
	 * @param levelId
	 * @param score
	 * @param sessionKey
	 * @throws InvalidSessionException
	 */
	public void scoreService(int levelId, int score, String sessionKey) throws InvalidSessionException {
		if (loginCtrl.isSessionActive(sessionKey)) {
			if (loginCtrl.getAliveLogins().get(sessionKey) != null)
				scoreCtrl.storeAScore(levelId,
						new Score(loginCtrl.getAliveLogins().get(sessionKey).getUserId(), score));
		} else
			throw new InvalidSessionException("The session key is not valid.");
	}

	/**
	 * This method returns the list of the scores for the requested level
	 * 
	 * @param levelId
	 *            level to retrieve the proper High Score List
	 * @return String that represent the list of the scores for the requested
	 *         level
	 */
	public String highScoreListService(int levelId) {
		return scoreCtrl.extractHighScoreList(levelId).toString();
	}

	/**
	 * This method returns the session key when a user request to be logged in
	 * 
	 * @param userId
	 *            user that request to open a new session
	 * @return String that represent the session key
	 */
	public String loginService(int userId) {
		return loginCtrl.openSession(userId).getSessionKey();
	}

	/**
	 * Ensure that this instance shall not be cloned overriding the clone()
	 * Object inherited method While clone() method is called, a
	 * CloneNotSupportedException is thrown to indicate that this particular
	 * class is not clonable
	 *
	 * @throws CloneNotSupportedException
	 * @return Object
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}