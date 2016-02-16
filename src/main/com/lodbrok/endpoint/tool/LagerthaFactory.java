/**
 * com.lodbrok.endpoint.tool package contains the HTTP server support classes
 */
package com.lodbrok.endpoint.tool;

/**
 * This class collects all the constants that shall be used in the source code
 * These constants are initialized, static, final and public to be reached from
 * everywhere but immutable
 * 
 * @author Fabio Riberto
 *
 */
public class LagerthaFactory {

	/**
	 * The port to access the Endpoint
	 *
	 */
	public static final int KATTEGAT_PORT = 8081;

	/**
	 * Default value for the socket backlog tail
	 *
	 */
	public static final int DEFAULT_BACKLOG = 0;

	/**
	 * Life time of a session in milliseconds
	 *
	 */
	public static final int SESSION_LIFE = 10 * 60 * 1000; // 10min*60sec*1000msec
															// = 600000
															// msec/10min

	/**
	 * Length of a session key in bits
	 *
	 */
	public static final int SESSION_KEY_BIT_LENGTH = 32; // length of a session
															// key in bits

	/**
	 * King Lodbrok death age
	 *
	 */
	public static final int LODBROK_DEATH_AGE = 109; // Prime number

	/**
	 * Max elements of a High score list per level
	 *
	 */
	public static final int MAX_SCORES_PER_LEVEL = 15; // For memory reason,
														// every existing High
														// score list shall not
														// be greater than 15
														// elements

	/**
	 * Default number of thread for JMX
	 *
	 */
	public static final int DEFAULT_NUMBER_OF_THREADS = 10;

	/**
	 * Default Database schema for JMX
	 *
	 */
	public static final String DEFAULT_DATABASE_SCHEMA = "default";

	/**
	 * GET method literal
	 *
	 */
	public static final String GET_METHOD = "GET";

	/**
	 * POST method literal
	 *
	 */
	public static final String POST_METHOD = "POST";

	/**
	 * Key to identify the type of content attribute for the message header
	 * purposes
	 *
	 */
	public static final String CONTENT_TYPE = "Content-Type";

	/**
	 * Key to identify the type of content for the message header purposes
	 *
	 */
	public static final String CONTENT_TEXT = "text/plain";

	/**
	 * Entry for the Endpoint main context
	 *
	 */
	public static final String MAIN_CONTEXT = "/";

	/**
	 * Key to identify the "parameters" attribute
	 *
	 */
	public static final String PARAM_ATTR_KEY = "parameters";

	/**
	 * Key to identify the "request" parameter
	 *
	 */
	public static final String REQ_PARAM = "request";

	/**
	 * Key to identify the "response" parameter
	 *
	 */
	public static final String RES_PARAM = "response";

	// Keys used in the actions
	/**
	 * Key to refer to "score" literal in the action
	 *
	 */
	public static final String SCORE_KEY = "score";

	/**
	 * Key to refer to "highscorelist" literal in the action
	 *
	 */
	public static final String HIGHSCORELIST_KEY = "highscorelist";

	/**
	 * Key to refer to "login" literal in the action
	 *
	 */
	public static final String LOGIN_KEY = "login";
	/**
	 * Key to refer to "levelid" literal in the action
	 *
	 */
	public static final String LEVELID_KEY = "levelid";

	/**
	 * Key to refer to "sessionkey" literal in the action
	 *
	 */
	public static final String SESSIONKEY_KEY = "sessionkey";

	/**
	 * Key to refer to "userid" literal in the action
	 *
	 */
	public static final String USERID_KEY = "userid";

	/**
	 * Decimal URI filter
	 *
	 */
	public static final String DECIMAL_URI_FILTER = "/(\\d+)";

	/**
	 * Session key parameter pattern
	 *
	 */
	public static final String SESSIONKEY_PARAM_PATTERN = "\\?sessionkey=(.+)";

	/**
	 * Score URI filter for URI validation
	 *
	 */
	public static final String SCORE_URI_FILTER = "/(\\d+)" + "/score" + SESSIONKEY_PARAM_PATTERN;

	/**
	 * High score list URI filter for URI validation
	 *
	 */
	public static final String HIGHSCORELIST_URI_FILTER = "/(\\d+)" + "/highscorelist";

	/**
	 * Login URI filter for URI validation
	 *
	 */
	public static final String LOGIN_URI_FILTER = "/(\\d+)" + "/login";
}
