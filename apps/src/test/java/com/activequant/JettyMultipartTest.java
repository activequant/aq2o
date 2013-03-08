package com.activequant;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Unit test for simple App.
 */
public class JettyMultipartTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public JettyMultipartTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(JettyMultipartTest.class);
	}

	public void testApp() throws ClientProtocolException, IOException {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://localhost:44444/csv/");
		MultipartEntity reqEntity = new MultipartEntity();
		StringBody seriesId = new StringBody("S1");
		StringBody field = new StringBody("F1");
		StringBody freq = new StringBody("RAW");
		reqEntity.addPart("SERIESID", seriesId);
		reqEntity.addPart("FIELD", field);
		reqEntity.addPart("FREQ", freq);
		reqEntity.addPart("DATA", new StringBody("1,1\n2,2\n3,3\n"));

		httppost.setEntity(reqEntity);

		System.out.println("executing request " + httppost.getRequestLine());
		HttpResponse response = httpclient.execute(httppost);
		System.out.println("----------------------------------------");
		System.out.println(response.getStatusLine());
		HttpEntity resEntity = response.getEntity();
		if (resEntity != null) {
			System.out.println("Response content length: "
					+ resEntity.getContentLength());
		}
		EntityUtils.consume(resEntity);

		assertTrue(true);
	}

}
