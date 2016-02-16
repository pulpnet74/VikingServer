/**
 * com.lodbrok.control package contains the controller files
 * 
 */
package com.lodbrok.control;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.lodbrok.entity.HighScoreList;
import com.lodbrok.entity.Score;

/**
 * @author Fabio Riberto
 *
 *         ScoreController class is in charge to process the score and the high
 *         score list requests
 * 
 */
public class ScoreController {

	/**
	 * Thread-safe java.util.Map to store all the High Score Lists for the
	 * existing levels where "Integer" is the level and HighScoreList is the
	 * list for this particular level
	 * 
	 */
	private ConcurrentMap<Integer, HighScoreList> allLevelsHighScoreListMap;

	/**
	 * ScoreController instantiation
	 * 
	 */
	public ScoreController() {
		allLevelsHighScoreListMap = new ConcurrentHashMap<>();
	}

	/**
	 * This method stores a score: if the level is not present, add a new level
	 * and enter the posted store to the new list
	 * 
	 */
	public synchronized void storeAScore(final Integer level, final Score score) {
		HighScoreList hsl = allLevelsHighScoreListMap.get(level);
		if (hsl != null)
			hsl.insert(score);
		else { // put a new element if the level doesn't exist
			hsl = new HighScoreList();
			allLevelsHighScoreListMap.putIfAbsent(level, hsl);
			hsl.insert(score);
		}
	}

	/**
	 * This method extracts a List for a particular level id. It shall return
	 * empty list (as required) if the level id is not present in the map of
	 * lists
	 * 
	 */
	public HighScoreList extractHighScoreList(final int level) {
		if (allLevelsHighScoreListMap.containsKey(level))
			return allLevelsHighScoreListMap.get(level);
		else
			return new HighScoreList();
	}
}
