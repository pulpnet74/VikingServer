/**
 * com.lodbrok.control package contains the controller files
 */
package com.lodbrok.control;

import static com.lodbrok.endpoint.tool.LagerthaFactory.MAX_SCORES_PER_LEVEL;
import static com.lodbrok.endpoint.tool.LagerthaFactory.SESSION_LIFE;

import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.lodbrok.entity.HighScoreList;
import com.lodbrok.entity.Score;

/**
 * @author Fabio Riberto
 * @RunWith MockitoJUnitRunner
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ScoreControllerTest {

	public static final int LEONIDA_FACTOR = 300;
	int timeWarp;
	String sessionKey;
	ScoreController scon;
	int userId[] = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73 }; // 21
																										// user
																										// ids
	int scoreValue[] = { 250, 500, 750, 1000, 1250, 1500, 1750, 2000, 2250, 2500, 2750, 3000, 3250, 3500, 3750, 4000,
			4250, 4500, 4750, 5000, 5250 }; // 21 scores
	int levelId[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 }; // 21
																									// levels
	ConcurrentSkipListSet<Score> scoreArrayList;

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
		scon = new ScoreController();
		scoreArrayList = new ConcurrentSkipListSet<Score>();
	}

	@Test
	public void testStoreOneScore() throws Exception {
		HighScoreList highScoreList = scon.extractHighScoreList(levelId[0]);
		Assert.assertEquals("Valid filled HighScoreList", new ConcurrentSkipListSet<>(),
				highScoreList.getHighScoreList());
		Score scoreObj = new Score(userId[0], scoreValue[0]);
		scon.storeAScore(levelId[0], scoreObj);
		highScoreList = scon.extractHighScoreList(levelId[0]);
		Assert.assertEquals("HighScore Invalid",
				String.valueOf(userId[0]).concat("=").concat(String.valueOf(scoreValue[0])), highScoreList.toString());
	}

	@Test
	public void testStoreTwoScoresSameUser() throws Exception {
		HighScoreList highScoreList = scon.extractHighScoreList(levelId[1]);
		Assert.assertEquals("Valid filled HighScoreList", new ConcurrentSkipListSet<>(),
				highScoreList.getHighScoreList());
		Score scoreObjLower = new Score(userId[0], scoreValue[0]);
		Score scoreObjHigher = new Score(userId[0], scoreValue[1]);
		scon.storeAScore(levelId[1], scoreObjHigher);
		scon.storeAScore(levelId[1], scoreObjLower);
		highScoreList = scon.extractHighScoreList(levelId[1]);
		Assert.assertEquals("HighScore Invalid",
				String.valueOf(userId[0]).concat("=").concat(String.valueOf(scoreObjHigher.getScore())),
				highScoreList.toString());
	}

	@Test
	public void testStoreTwoScoresDifferentUsers() throws Exception {
		HighScoreList highScoreList = scon.extractHighScoreList(levelId[2]);
		Assert.assertEquals("Valid filled HighScoreList", new ConcurrentSkipListSet<>(),
				highScoreList.getHighScoreList());
		Score scoreObjHigher = new Score(userId[1], scoreValue[2]);
		Score scoreObjLower = new Score(userId[2], scoreValue[1]);
		scon.storeAScore(levelId[2], scoreObjLower); // Insert the lower before
														// to test the
														// comparator
		scon.storeAScore(levelId[2], scoreObjHigher);
		highScoreList = scon.extractHighScoreList(levelId[2]);
		Assert.assertEquals("HighScore Invalid",
				String.valueOf(userId[1]).concat("=").concat(String.valueOf(scoreObjHigher.getScore())).concat(",")
						.concat(String.valueOf(userId[2])).concat("=").concat(String.valueOf(scoreObjLower.getScore())),
				highScoreList.toString());
	}

	@Test
	public void testStoreOneScoreDifferentUsers() throws Exception {
		HighScoreList highScoreList = scon.extractHighScoreList(levelId[3]);
		Assert.assertEquals("Valid filled HighScoreList", new ConcurrentSkipListSet<>(),
				highScoreList.getHighScoreList());
		Score scoreObjMario = new Score(userId[3], scoreValue[3]); // Mario id =
																	// 7
		Score scoreObjLuigi = new Score(userId[4], scoreValue[3]); // Luigi id =
																	// 11
		scon.storeAScore(levelId[3], scoreObjMario);
		scon.storeAScore(levelId[3], scoreObjLuigi);
		highScoreList = scon.extractHighScoreList(levelId[3]);
		Assert.assertEquals("HighScore Invalid",
				String.valueOf(userId[3]).concat("=").concat(String.valueOf(scoreObjMario.getScore())).concat(",")
						.concat(String.valueOf(userId[4])).concat("=").concat(String.valueOf(scoreObjLuigi.getScore())),
				highScoreList.toString());
		highScoreList = scon.extractHighScoreList(levelId[4]);
		Assert.assertEquals("Valid filled HighScoreList", new ConcurrentSkipListSet<>(),
				highScoreList.getHighScoreList());
		scon.storeAScore(levelId[4], scoreObjLuigi);
		scon.storeAScore(levelId[4], scoreObjMario);
		highScoreList = scon.extractHighScoreList(levelId[4]);
		Assert.assertEquals("HighScore Invalid",
				String.valueOf(userId[4]).concat("=").concat(String.valueOf(scoreObjLuigi.getScore())).concat(",")
						.concat(String.valueOf(userId[3])).concat("=").concat(String.valueOf(scoreObjMario.getScore())),
				highScoreList.toString());
	}

	@Test
	public void testMaxScoresPerLevel() throws Exception {
		HighScoreList highScoreList = scon.extractHighScoreList(levelId[5]);
		Assert.assertEquals("Valid filled HighScoreList", new ConcurrentSkipListSet<>(),
				highScoreList.getHighScoreList());
		for (int i = 0; i < MAX_SCORES_PER_LEVEL; i++) {
			scoreArrayList.add(new Score(userId[i], scoreValue[i]));
		}
		for (Score scoreIteration : scoreArrayList) { // Verify that particular
														// score for a user
														// isn't member of high
														// score list for a
														// requested level
			scon.storeAScore(levelId[5], scoreIteration);
		}
		highScoreList = scon.extractHighScoreList(levelId[5]);
		StringBuffer sb = new StringBuffer();
		for (int i = MAX_SCORES_PER_LEVEL; i > 1; i--) {
			sb.append(String.valueOf(userId[i - 1]));
			sb.append("=");
			sb.append(String.valueOf(scoreValue[i - 1]));
			sb.append(",");
		}
		sb.append(String.valueOf(userId[0]));
		sb.append("=");
		sb.append(String.valueOf(scoreValue[0]));
		Assert.assertEquals("HighScore Invalid", sb.toString(), highScoreList.toString());
		scoreArrayList.add(new Score(userId[MAX_SCORES_PER_LEVEL], scoreValue[MAX_SCORES_PER_LEVEL]));
		scon.storeAScore(levelId[5], new Score(userId[MAX_SCORES_PER_LEVEL], scoreValue[MAX_SCORES_PER_LEVEL]));
		highScoreList = scon.extractHighScoreList(levelId[5]);
		StringBuffer sb2 = new StringBuffer();
		for (int i = MAX_SCORES_PER_LEVEL; i > 1; i--) {
			sb2.append(String.valueOf(userId[i]));
			sb2.append("=");
			sb2.append(String.valueOf(scoreValue[i]));
			sb2.append(",");
		}
		sb2.append(String.valueOf(userId[1]));
		sb2.append("=");
		sb2.append(String.valueOf(scoreValue[1]));
		Assert.assertEquals("HighScore Invalid", sb2.toString(), highScoreList.toString());
	}

}
