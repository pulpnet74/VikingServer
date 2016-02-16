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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Fabio Riberto
 *
 */
public class LotOfRequestsStressTest {

	List<String> userIds = new ArrayList<>();
	List<String> sessionKeys = new ArrayList<>();
	List<String> levels = new ArrayList<>();
	List<String> scores = new ArrayList<>();
	public static final int HUGE_NUM_OF_LOGINS = 201; // Only odd numbers are
														// chosen to be valid
														// user id
														// (HUGE_NUM_OF_LOGINS/2)
	public static final int HUGE_NUM_OF_LEVELS = 201;
	public static final int HUGE_NUM_OF_SCORES = HUGE_NUM_OF_LEVELS * 2;

	HttpURLConnection connection;
	Random rng;

	@Before
	public void init() throws Exception {
		LodbrokServer.main(null);
		for (int i = 1; i < HUGE_NUM_OF_LOGINS; i++) {
			if (i % 2 != 0)
				userIds.add(String.valueOf(i));
		}
		for (int i = 1; i < HUGE_NUM_OF_LEVELS; i++) {
			levels.add(String.valueOf(i));
		}
		for (int i = 1; i < HUGE_NUM_OF_SCORES; i++) {
			scores.add(String.valueOf(i * 25));
		}
		rng = new Random();
	}

	@Test
	public void testEnormousNumberOfRequests() throws IOException {
		String protocol = "http";
		String host = "localhost";
		int port = KATTEGAT_PORT;
		String lReqValid = LOGIN_KEY;
		String sReqValid = SCORE_KEY;
		String hslReqValid = HIGHSCORELIST_KEY;
		String skReqValid = SESSIONKEY_KEY;
		int totOfRequests = 0;
		// Logins
		for (String s : userIds) {
			connection = loginRequest(protocol, host, port, s, lReqValid);
			Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
			sessionKeys.add(getResponseText(connection));
		}
		System.out.println("Number of Login Requests = ".concat(String.valueOf(userIds.size())));
		totOfRequests += userIds.size();
		// Store scores
		for (String s : sessionKeys) {
			Assert.assertNotNull(null, s);
			System.out.println(s);
			int i = 0;
			for (String s2 : levels) {
				s2 = String.valueOf(rng.nextInt(levels.size()));
				String s3 = scores.get(i).concat(String.valueOf(i * rng.nextInt(10)));
				connection = scoreRequest(protocol, host, port, s2, sReqValid, skReqValid, s, s3);
				Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
				i++;
			}
		}
		System.out.println("Number of Score Requests = ".concat(String.valueOf(levels.size() * sessionKeys.size())));
		totOfRequests += levels.size() * sessionKeys.size();
		// Retrieve High Score Lists
		for (String s2 : levels) {
			connection = highScoreListRequest(protocol, host, port, s2, hslReqValid);
			Assert.assertEquals(null, HttpURLConnection.HTTP_OK, connection.getResponseCode());
			System.out.println("LEVEL ".concat(s2).concat(": ").concat(getResponseText(connection)));
		}
		System.out.println("Number of High Score List Requests = ".concat(String.valueOf(levels.size())));
		totOfRequests += levels.size();
		System.out.println("Number of Requests = ".concat(String.valueOf(totOfRequests)));
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
