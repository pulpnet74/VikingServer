/**
 * com.lodbrok.endpoint.tool package contains the HTTP server support classes
 */
package com.lodbrok.endpoint.tool;

import static com.lodbrok.endpoint.tool.LagerthaFactory.CONTENT_TEXT;
import static com.lodbrok.endpoint.tool.LagerthaFactory.CONTENT_TYPE;
import static com.lodbrok.endpoint.tool.LagerthaFactory.HIGHSCORELIST_KEY;
import static com.lodbrok.endpoint.tool.LagerthaFactory.LEVELID_KEY;
import static com.lodbrok.endpoint.tool.LagerthaFactory.LOGIN_KEY;
import static com.lodbrok.endpoint.tool.LagerthaFactory.PARAM_ATTR_KEY;
import static com.lodbrok.endpoint.tool.LagerthaFactory.REQ_PARAM;
import static com.lodbrok.endpoint.tool.LagerthaFactory.SCORE_KEY;
import static com.lodbrok.endpoint.tool.LagerthaFactory.SESSIONKEY_KEY;
import static com.lodbrok.endpoint.tool.LagerthaFactory.USERID_KEY;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

import com.lodbrok.control.LodbrokController;
import com.lodbrok.exception.InvalidRequestException;
import com.lodbrok.exception.InvalidSessionException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * This class implement the server handler
 * 
 * @author Fabio Riberto
 * @extends HttpHandler
 */
public class LodbrokHandler implements HttpHandler {
	/**
	 * Controller for routing the requests and store the data
	 */
	private final LodbrokController controller;

	/**
	 * LodbrokHandler new instance
	 *
	 * @param controller
	 */
	public LodbrokHandler(LodbrokController controller) {
		this.controller = controller;
	}

	/**
	 * This method handle the external requests. It communicates to the
	 * controller which service shall process the request
	 * 
	 * @param exchange
	 * @throws IOException
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		@SuppressWarnings("unchecked")
		Map<String, String> params = (Map<String, String>) exchange.getAttribute(PARAM_ATTR_KEY); // Filter
																									// the
																									// "parameters"
																									// attribute
		String reqAction = params.get(REQ_PARAM); // Select the HTTP
													// encapsulated request
		String msg = "";
		int connCode = HttpURLConnection.HTTP_OK;
		try {
			if (reqAction.equals(SCORE_KEY)) { // Take actions to store a score
												// for a particular level
				handleScore(params); // It isn't requested any Response body
			} else if (reqAction.equals(HIGHSCORELIST_KEY)) { // Take actions to
																// retrieve the
																// highscorelist
																// for a
																// particular
																// level
				msg = handleHighScoreList(params); // High Score List in CVS
													// format to "msg" String
			} else if (reqAction.equals(LOGIN_KEY)) { // Take actions to login a
														// user
				msg = handleLogin(params); // Session key to "msg" String
			} else {
				throw new InvalidRequestException("Invalid request: the URI doesn't contain a valid request.");
			}
		} catch (InvalidRequestException | InvalidSessionException e) {
			msg = e.getMessage();
			connCode = HttpURLConnection.HTTP_NOT_FOUND;
		} finally {
			exchange.getResponseHeaders().add(CONTENT_TYPE, CONTENT_TEXT);
			exchange.sendResponseHeaders(connCode, msg.length());
			OutputStream os = exchange.getResponseBody();
			os.write(msg.getBytes()); // Consume resources
			os.close(); // Release resources
		}
	}

	/**
	 * This method format the score request to be interpreted by the controller
	 * 
	 * @param dataMap
	 * @throws InvalidSessionException
	 * @throws NumberFormatException
	 * 
	 */
	private void handleScore(final Map<String, String> dataMap) throws NumberFormatException, InvalidSessionException {
		controller.scoreService(Integer.parseInt(dataMap.get(LEVELID_KEY)), Integer.parseInt(dataMap.get(SCORE_KEY)),
				dataMap.get(SESSIONKEY_KEY));
	}

	/**
	 * This method format the high score list request to be interpreted by the
	 * controller
	 * 
	 * @param dataMap
	 * @return String the high score list for a requested level as a string
	 * 
	 */
	private String handleHighScoreList(final Map<String, String> dataMap) {
		return controller.highScoreListService(Integer.parseInt(dataMap.get(LEVELID_KEY)));
	}

	/**
	 * This method format the login request to be interpreted by the controller
	 * 
	 * @param dataMap
	 * @return String the generated session key as a string
	 * 
	 */
	private String handleLogin(final Map<String, String> dataMap) {
		return controller.loginService(Integer.parseInt(dataMap.get(USERID_KEY)));
	}
}
