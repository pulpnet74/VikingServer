/**
 * com.lodbrok.endpoint.tool package contains the HTTP server support classes
 */
package com.lodbrok.endpoint.tool;

import static com.lodbrok.endpoint.tool.LagerthaFactory.GET_METHOD;
import static com.lodbrok.endpoint.tool.LagerthaFactory.HIGHSCORELIST_KEY;
import static com.lodbrok.endpoint.tool.LagerthaFactory.HIGHSCORELIST_URI_FILTER;
import static com.lodbrok.endpoint.tool.LagerthaFactory.LEVELID_KEY;
import static com.lodbrok.endpoint.tool.LagerthaFactory.LOGIN_KEY;
import static com.lodbrok.endpoint.tool.LagerthaFactory.LOGIN_URI_FILTER;
import static com.lodbrok.endpoint.tool.LagerthaFactory.MAIN_CONTEXT;
import static com.lodbrok.endpoint.tool.LagerthaFactory.PARAM_ATTR_KEY;
import static com.lodbrok.endpoint.tool.LagerthaFactory.POST_METHOD;
import static com.lodbrok.endpoint.tool.LagerthaFactory.REQ_PARAM;
import static com.lodbrok.endpoint.tool.LagerthaFactory.SCORE_KEY;
import static com.lodbrok.endpoint.tool.LagerthaFactory.SCORE_URI_FILTER;
import static com.lodbrok.endpoint.tool.LagerthaFactory.SESSIONKEY_KEY;
import static com.lodbrok.endpoint.tool.LagerthaFactory.USERID_KEY;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lodbrok.exception.InvalidBodyException;
import com.lodbrok.exception.InvalidRequestException;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

/**
 * This class implement the server filter
 * 
 * @author Fabio Riberto
 * @extends Filter
 *
 */
public class LodbrokFilter extends Filter {

	@Override
	public String description() {
		String desc = "This filter is responsible to recognize and validate proper Score, Highscorelist and Login"
				+ "URIs in order extract the data and deliver to the HttpExchange to routed to the controller."
				+ "It shall be able to recognize bad and invalid requests and inject the proper error status"
				+ "code for the response";
		return desc;
	}

	@Override
	public void doFilter(HttpExchange exchange, Chain chainOfFilters) throws IOException {
		try {
			URI uri = exchange.getRequestURI();
			String uriStr = uri.toString();
			List<String> patterns = new ArrayList<String>();
			patterns.add(SCORE_URI_FILTER);
			patterns.add(HIGHSCORELIST_URI_FILTER);
			patterns.add(LOGIN_URI_FILTER);
			Map<String, String> dataMap = null;
			for (String pattern : patterns) {
				if (uriStr.matches(pattern)) {
					switch (pattern) {
					case SCORE_URI_FILTER:
						// System.out.println(SCORE_URI_FILTER + " pattern
						// matched!");
						dataMap = exctractScoreDataMap(exchange);
						break;
					case HIGHSCORELIST_URI_FILTER:
						// System.out.println(HIGHSCORELIST_URI_FILTER + "
						// pattern matched!");
						dataMap = exctractHighScoreListDataMap(exchange);
						break;
					case LOGIN_URI_FILTER:
						// System.out.println(LOGIN_URI_FILTER + " pattern
						// matched!");
						dataMap = exctractLoginDataMap(exchange);
						break;
					default:
						break;
					}
				} else if (patterns.iterator().hasNext())
					continue;
				else
					throw new InvalidRequestException("Invalid request: the URI doesn't match any valid pattern\n"
							.concat("Check the URI format"));
			}
			if (!(dataMap == null))
				exchange.setAttribute(PARAM_ATTR_KEY, dataMap);
			else
				throw new InvalidRequestException(
						"Invalid request: the URI doesn't contain a valid request\n".concat("Check the URI format"));
			chainOfFilters.doFilter(exchange);
		} catch (InvalidRequestException e) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, e.getMessage().length());
			OutputStream os = exchange.getResponseBody();
			os.write(e.getMessage().getBytes());
			os.close();
		} catch (Exception e) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, e.getMessage().length());
			OutputStream os = exchange.getResponseBody();
			os.write(e.getMessage().getBytes());
			os.close();
		} finally {

		}
	}

	/**
	 * This method exctractScoreDataMap extracts the data from the score request
	 * URI
	 *
	 * @param exchange
	 * @return dataMap
	 * @throws InvalidRequestException
	 * @throws InvalidBodyException
	 * @throws IOException
	 */
	private static Map<String, String> exctractScoreDataMap(HttpExchange exchange)
			throws InvalidRequestException, InvalidBodyException, IOException {
		String reqMethod = exchange.getRequestMethod();
		Map<String, String> dataMap = new HashMap<>();
		String score, levId, sessionKey;
		try { // Open resources
			InputStreamReader isr = new InputStreamReader(
					exchange.getRequestBody()/* , "utf-8" */);
			BufferedReader br = new BufferedReader(isr);
			try { // Consume resources
				score = br.readLine();
			} catch (IOException e) {
				throw new InvalidBodyException(
						"Invalid body: The body is empty\n" + "A minimum of one number expected");
			} finally { // Close resources
				br.close();
				isr.close();
			}
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
		if (reqMethod.equalsIgnoreCase(POST_METHOD)) {
			if (score.isEmpty())
				throw new InvalidBodyException(
						"Invalid body: The body of the POST message is blank\n" + "Minimum of one character expected");
			if (!isAlphaNumericEncoded(score))
				throw new InvalidBodyException("Invalid body: the POST body=" + score
						+ " contains one or more special characters\n" + "Alphanumeric body expected");
			levId = exchange.getRequestURI().toString().split(SCORE_KEY)[0].substring(1,
					exchange.getRequestURI().toString().split(SCORE_KEY)[0].length() - 1);
			sessionKey = exchange.getRequestURI().toString().split(SESSIONKEY_KEY + "=")[1];
		} else
			throw new InvalidRequestException("The " + reqMethod + " is invalid\n" + POST_METHOD + " method expected");
		dataMap.put(REQ_PARAM, SCORE_KEY);
		dataMap.put(SCORE_KEY, score);
		dataMap.put(LEVELID_KEY, levId);
		dataMap.put(SESSIONKEY_KEY, sessionKey);
		return dataMap;
	}

	/**
	 * This method exctractScoreDataMap extracts the data from the high score
	 * limit request URI
	 *
	 * @param exchange
	 * @return dataMap
	 * @throws InvalidRequestException
	 * @throws InvalidBodyException
	 * @throws IOException
	 */
	private static Map<String, String> exctractHighScoreListDataMap(HttpExchange exchange)
			throws InvalidRequestException, InvalidBodyException, IOException {
		String reqMethod = exchange.getRequestMethod();
		Map<String, String> dataMap = new HashMap<>();
		String levId;
		if (reqMethod.equalsIgnoreCase(GET_METHOD)) {
			levId = exchange.getRequestURI().toString().split(MAIN_CONTEXT)[1];
		} else
			throw new InvalidRequestException("The " + reqMethod + " is invalid\n" + GET_METHOD + " mehod expected");
		dataMap.put(REQ_PARAM, HIGHSCORELIST_KEY);
		dataMap.put(LEVELID_KEY, levId);
		return dataMap;
	}

	/**
	 * This method exctractScoreDataMap extracts the data from the login request
	 * URI
	 *
	 * @param exchange
	 * @return dataMap
	 * @throws InvalidRequestException
	 * @throws InvalidBodyException
	 * @throws IOException
	 */
	private static Map<String, String> exctractLoginDataMap(HttpExchange exchange)
			throws InvalidRequestException, InvalidBodyException, IOException {
		String reqMethod = exchange.getRequestMethod();
		Map<String, String> dataMap = new HashMap<>();
		String userId;
		if (reqMethod.equalsIgnoreCase(GET_METHOD)) {
			userId = exchange.getRequestURI().toString().split(MAIN_CONTEXT)[1];
		} else
			throw new InvalidRequestException("The " + reqMethod + " is invalid\n" + GET_METHOD + " mehod expected");
		dataMap.put(REQ_PARAM, LOGIN_KEY);
		dataMap.put(USERID_KEY, userId);
		return dataMap;
	}

	/**
	 * This method check that the body content contains only [0...9] and/or
	 * [a...z]/[A...Z] characters
	 *
	 * @param body
	 *            content
	 * @return true if the body content contains only [0...9] and/or
	 *         [a...z]/[A...Z] characters
	 * @return false if the body content contains one or more special characters
	 */
	private static boolean isAlphaNumericEncoded(String body) {
		for (char character : body.toCharArray()) {
			if (!Character.isLetterOrDigit(character)) {
				return false;
			}
		}
		return true;
	}
}
