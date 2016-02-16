/**
 * com.lodbrok.entity package contains the model objects
 */
package com.lodbrok.entity;

import static com.lodbrok.endpoint.tool.LagerthaFactory.MAX_SCORES_PER_LEVEL;

import java.io.Serializable;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * HighScoreList class represents the high score list model object
 * 
 * @author Fabio Riberto
 *
 */
public class HighScoreList implements Serializable {

	private static final long serialVersionUID = -5018784421902718177L;

	/**
	 * List of users scores for a particular level. Since a sorted container is
	 * needed, a ConcurrentSkipListSet has been chose.
	 */
	private ConcurrentSkipListSet<Score> highScoreLists;

	/**
	 * New HighScoreList class
	 * 
	 */
	public HighScoreList() {
		this.highScoreLists = new ConcurrentSkipListSet<>();
	}

	/**
	 * This method return a high score list
	 * 
	 * @return ConcurrentSkipListSet<Score>
	 */
	public ConcurrentSkipListSet<Score> getHighScoreList() {
		return highScoreLists;
	}

	/**
	 * This method set a high score list
	 * 
	 * @param highScoreList
	 *            the highScoreList to set
	 */
	public void setHighScoreList(ConcurrentSkipListSet<Score> highScoreList) {
		this.highScoreLists = highScoreList;
	}

	/**
	 * Insert a new posted score for a particular level if there doesn't exist
	 * one higher (for that user)
	 *
	 * @param score
	 */
	public void insert(Score score) { // Here
										// LodbrokFilter.exctractScoreDataMap()
										// has yet verified that the posted
										// score has num_of_chars > 0
		Score lastValidScore = null;
		for (Score scoreIteration : highScoreLists) { // Verify that particular
														// score for a user
														// isn't member of high
														// score list for a
														// requested level
			if (scoreIteration.getUserId() == score.getUserId())
				lastValidScore = scoreIteration;
		}
		if (lastValidScore != null) { // If it is, test if the existing one is
										// bigger or smaller than the newer
			if (lastValidScore.getScore() >= score.getScore()) {
				return;
			} else
				highScoreLists.remove(lastValidScore);
		} else {
			highScoreLists.add(score); // Here lastValidScore is null OR it has
										// been removed! That's why the new
										// score is inserted
			cutHighScoreList(); // Here LodbrokFilter.exctractScoreDataMap() has
								// yet verified that the posted score has
								// num_of_chars > 0
		}
	}

	/**
	 * Cut the smaller value of the High score list if it becomes bigger than
	 * MAX_SCORES_PER_LEVEL
	 *
	 */
	private void cutHighScoreList() {
		if (highScoreLists.size() > MAX_SCORES_PER_LEVEL) { // Check that the
															// max size of the
															// high score list
															// hasn't been
															// reached.
			highScoreLists.pollLast();
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		String parseHighScoreList = getHighScoreList().toString().trim();
		parseHighScoreList = parseHighScoreList.replace("[", "").replace("]", "").replace(", ", ",");
		return sb.append(parseHighScoreList).toString();
	}
}
