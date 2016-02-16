/**
 * com.lodbrok.endpoint package contains the server and its support classes
 */
package com.lodbrok.endpoint;

import static com.lodbrok.endpoint.tool.LagerthaFactory.HIGHSCORELIST_KEY;
import static com.lodbrok.endpoint.tool.LagerthaFactory.KATTEGAT_PORT;
import static com.lodbrok.endpoint.tool.LagerthaFactory.LOGIN_KEY;
import static com.lodbrok.endpoint.tool.LagerthaFactory.SCORE_KEY;
import static com.lodbrok.endpoint.tool.LagerthaFactory.SESSIONKEY_KEY;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Fabio Riberto
 *
 */
public class LodbrokServerTest {

	HttpURLConnection connection;

	@Before
	public void init() throws Exception {
		connection = null;
		LodbrokServer.main(null);
	}

	@Test
	public void testValidLoginURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = KATTEGAT_PORT;
		String userIdValid = "995";
		String lReqValid = LOGIN_KEY;
		connection = loginRequest(protocol, host, port, userIdValid, lReqValid);
		Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
		String sessionkeyValVal = getResponseText(connection);
		Assert.assertNotNull(null, sessionkeyValVal);
		System.out.println(sessionkeyValVal);
	}

	@Test
	public void testNotValidUserId_LoginURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = KATTEGAT_PORT;
		String userIdValid = "995";
		String userIdNotValid = "-2";
		String lReqValid = LOGIN_KEY;
		connection = loginRequest(protocol, host, port, userIdNotValid, lReqValid);
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(userIdNotValid)).concat("\" ").concat("is a wrong user id\n")
						.concat("Valid user id is a 31 bit unsigned integer number (i.e.: ")
						.concat(String.valueOf(userIdValid)).concat(")\n"),
				HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());
	}

	@Test
	public void testNotValidLoginLiteral_LoginURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = KATTEGAT_PORT;
		String userIdValid = "995";
		String lReqNotValid = "margin";
		connection = loginRequest(protocol, host, port, userIdValid, lReqNotValid);
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(lReqNotValid)).concat("\" ").concat("is a wrong login literal\n")
						.concat("Valid login literal is \"").concat(LOGIN_KEY).concat("\"\n"),
				HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());
	}

	@Test
	public void testValidScoreURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = KATTEGAT_PORT;
		String userIdValid = "995";
		String lReqValid = LOGIN_KEY;
		String levelIdValid = "7";
		String sReqValid = SCORE_KEY;
		String skReqValid = SESSIONKEY_KEY;
		connection = loginRequest(protocol, host, port, userIdValid, lReqValid);
		String skValValid = getResponseText(connection);
		String scoreValid = "2550";
		connection = scoreRequest(protocol, host, port, levelIdValid, sReqValid, skReqValid, skValValid, scoreValid);
		Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
	}

	@Test
	public void testNotValidScoreLiteral_ScoreURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = KATTEGAT_PORT;
		String userIdValid = "995";
		String lReqValid = LOGIN_KEY;
		String levelIdValid = "7";
		String sReqValid = SCORE_KEY;
		String sReqNotValid = "spore";
		String skReqValid = SESSIONKEY_KEY;
		connection = loginRequest(protocol, host, port, userIdValid, lReqValid);
		String skValValid = getResponseText(connection);
		String scoreValid = "2550";
		connection = scoreRequest(protocol, host, port, levelIdValid, sReqNotValid, skReqValid, skValValid, scoreValid);
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(sReqNotValid)).concat("\" ").concat("is a wrong score literal\n")
						.concat("Valid score literal is ").concat(String.valueOf(sReqValid)).concat(")\n"),
				HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());

	}

	@Test
	public void testNotValidScore_ScoreURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = KATTEGAT_PORT;
		String userIdValid = "995";
		String lReqValid = LOGIN_KEY;
		String levelIdValid = "7";
		String sReqValid = SCORE_KEY;
		String skReqValid = SESSIONKEY_KEY;
		connection = loginRequest(protocol, host, port, userIdValid, lReqValid);
		String skValValid = getResponseText(connection);
		String scoreValid = "2550";
		String scoreNotValid = "-54";
		connection = scoreRequest(protocol, host, port, levelIdValid, sReqValid, skReqValid, skValValid, scoreNotValid);
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(scoreNotValid)).concat("\" ").concat("is a wrong score\n")
						.concat("Valid score is a 31 bit unsigned integer number (i.e.: ")
						.concat(String.valueOf(scoreValid)).concat(")\n"),
				HttpURLConnection.HTTP_BAD_REQUEST, connection.getResponseCode());
	}

	@Test
	public void testValidHighScoreListURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = KATTEGAT_PORT;
		String levelIdValid = "7";
		String hslReqValid = HIGHSCORELIST_KEY;
		connection = highScoreListRequest(protocol, host, port, levelIdValid, hslReqValid);
		Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
		String returnedHighScoreList = getResponseText(connection);
		Assert.assertNotNull(null, returnedHighScoreList);
		System.out.println(returnedHighScoreList);
	}

	@Test
	public void testNotValidlevelId_HighScoreListURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = KATTEGAT_PORT;
		String levelIdValid = "7";
		String levelNotValid = "-4";
		String hslReqValid = HIGHSCORELIST_KEY;
		connection = highScoreListRequest(protocol, host, port, levelNotValid, hslReqValid);
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(levelNotValid)).concat("\" ").concat("is a wrong level id\n")
						.concat("Valid level id is a 31 bit unsigned integer number (i.e.: ")
						.concat(String.valueOf(levelIdValid)).concat(")\n"),
				HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());
	}

	@Test
	public void testNotValidHighScoreListLiteral_HighScoreListURIFormat() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = KATTEGAT_PORT;
		String levelIdValid = "7";
		String hslReqNotValid = "fightscorefirst";
		connection = highScoreListRequest(protocol, host, port, levelIdValid, hslReqNotValid);
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(hslReqNotValid)).concat("\" ")
						.concat("is a wrong high score list literal\n").concat("Valid high score list literal is \"")
						.concat(HIGHSCORELIST_KEY).concat("\"\n"),
				HttpURLConnection.HTTP_NOT_FOUND, connection.getResponseCode());
	}

	@Test
	public void testCompletePoolOfRequests() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = KATTEGAT_PORT;
		String userIdValid = "995";
		String lReqValid = LOGIN_KEY;
		String levelIdValid = "7";
		String sReqValid = SCORE_KEY;
		String skReqValid = SESSIONKEY_KEY;
		String hslReqValid = HIGHSCORELIST_KEY;
		connection = loginRequest(protocol, host, port, userIdValid, lReqValid);
		Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
		String skValValid = getResponseText(connection);
		String scoreValid = "2550";
		connection = scoreRequest(protocol, host, port, levelIdValid, sReqValid, skReqValid, skValValid, scoreValid);
		Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
		connection = highScoreListRequest(protocol, host, port, levelIdValid, hslReqValid);
		String hslVal = getResponseText(connection);
		Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
		Assert.assertNotNull(null, hslVal);
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(hslVal)).concat("\" ").concat("is a wrong High Score List sequence\n")
						.concat("Valid High Score List sequence is a CSV of <userid>=<score> (i.e.: ")
						.concat(String.valueOf("995=2550 ")).concat("or 995=2550,34=3400").concat(")\n"),
				"995=2550", hslVal);
		levelIdValid = "2";
		connection = highScoreListRequest(protocol, host, port, levelIdValid, hslReqValid);
		hslVal = getResponseText(connection);
		Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
		Assert.assertEquals(
				"\n\"".concat(String.valueOf(hslVal)).concat("\" ").concat("is a wrong High Score List sequence\n")
						.concat("Valid High Score List sequence is a CSV of <userid>=<score> or a empy text (i.e.: ")
						.concat(String.valueOf("<empty_text>")).concat(")\n"),
				"", hslVal);
	}

	private HttpURLConnection loginRequest(final String protocol, final String host, final int port,
			final String userId, final String lReq) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(protocol);
		sb.append("://");
		sb.append(host);
		sb.append(":");
		sb.append(port);
		sb.append("/");
		sb.append(userId);
		sb.append("/");
		sb.append(lReq);
		URL url = new URL(sb.toString());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.disconnect();
		return connection;
	}

	private HttpURLConnection scoreRequest(final String protocol, final String host, final int port,
			final String levelId, final String scoreReq, final String sessionKeyAttr, final String sessionKeyVal,
			final String score) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(protocol);
		sb.append("://");
		sb.append(host);
		sb.append(":");
		sb.append(port);
		sb.append("/");
		sb.append(levelId);
		sb.append("/");
		sb.append(scoreReq);
		sb.append("?");
		sb.append(sessionKeyAttr);
		sb.append("=");
		sb.append(sessionKeyVal);
		URL url = new URL(sb.toString());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
		dos.writeBytes(score);
		dos.flush();
		dos.close();
		connection.disconnect();
		return connection;
	}

	private HttpURLConnection highScoreListRequest(final String protocol, final String host, final int port,
			final String levelId, final String hslReq) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(protocol);
		sb.append("://");
		sb.append(host);
		sb.append(":");
		sb.append(port);
		sb.append("/");
		sb.append(levelId);
		sb.append("/");
		sb.append(hslReq);
		URL url = new URL(sb.toString());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.disconnect();
		return connection;
	}

	private String getResponseText(final HttpURLConnection connection) throws IOException {
		String response;
		BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
		StringBuilder sb = new StringBuilder();
		while ((response = br.readLine()) != null) {
			sb.append(response);
		}
		return sb.toString();
	}
}
