/**
 * com.lodbrok.entity package contains the model objects
 */
package com.lodbrok.entity;

import static com.lodbrok.endpoint.tool.LagerthaFactory.LODBROK_DEATH_AGE;

import java.io.Serializable;

/**
 * Score class represents the Score model object
 * 
 * @author Fabio Riberto
 *
 */
public class Score implements Comparable<Score>, Serializable {

	private static final long serialVersionUID = 6227253130988253689L;

	/**
	 * User id that requests to post a score
	 */
	private int userId;

	/**
	 * Posted score
	 */
	private int score;

	/**
	 * New Score class
	 * 
	 * @param userId
	 * @param score
	 */
	public Score(int userId, int score) {
		this.setUserId(userId);
		this.setScore(score);
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
	
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * This method compares 2 Score objects to let the one having the highest
	 * score value at the top of the High Score List (descending order)
	 * 
	 * @param scoreToCompare
	 *            the score to to compare to
	 * @return result
	 */
	@Override
	public int compareTo(Score scoreToCompare) {
		// Switch to these commented lines in order to obtain an ascending High
		// Score List
		// int result = Integer.compare(this.score, scoreToCompare.score);
		// if ((result == 0) && (Integer.compare(this.userId,
		// scoreToCompare.userId) != 0))
		int result = Integer.compare(scoreToCompare.score, this.score);
		if ((result == 0) && (Integer.compare(scoreToCompare.userId, this.userId) != 0))
			result = 1;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		if (getUserId() != ((Score) obj).userId)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = LODBROK_DEATH_AGE; // prime number
		result += getUserId() + getScore() + 7;
		return result;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getUserId());
		sb.append("=");
		sb.append(getScore());
		return sb.toString();
	}

}
