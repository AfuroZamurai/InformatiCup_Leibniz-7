package main.evaluate;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

import main.io.ImageSaver;

/**
 * Implementation of Evaluator interface for the TrasiWebServer of the
 * InformatiCup Competition Sends images to the server and uses the returned
 * confidence values for scoring
 * 
 * @author Jannik
 *
 */
public class TrasiWebEvaluator implements IEvaluator {

	/**
	 * The expected image width in pixels
	 */
	public static final int IMAGE_WIDTH = 64;

	/**
	 * The expected image height in pixels
	 */
	public static final int IMAGE_HEIGHT = 64;

	/**
	 * Variable to hold the api key once loaded the key is loaded at runtime from
	 * file system since uploading keys to public repositories is a bad idea
	 */
	public static String API_KEY;

	public static final String SERVER_URL = "https://phinau.de/trasi";

	/**
	 * Send a post request to the trasi server with the given image
	 * 
	 * @param image
	 *            The image to be sent
	 * @return a String with 2 lines, first line contains status code, second line
	 *         contains response
	 * @throws IOException
	 *             When given file is not valid
	 */
	private String sendRequest(File image) throws IOException {

		// Must be false for compatibility with server
		System.setProperty("jsse.enableSNIExtension", "false");

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(SERVER_URL);

		// add header
		post.setHeader("User-Agent", "Java client");

		// Create payload for request
		FileBody data = new FileBody(image);

		HttpEntity entity = MultipartEntityBuilder.create().addTextBody("key", API_KEY).addPart("image", data).build();

		post.setEntity(entity);

		// Measure time of response
		long millis = System.currentTimeMillis();

		HttpResponse response = client.execute(post);

		long delay = System.currentTimeMillis() - millis;
		System.out.println("Response after: " + delay + "ms");

		// Parse Response
		String responseCode = response.getStatusLine().getStatusCode() + "";

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		return responseCode + "\n" + result.toString();
	}

	/**
	 * Loads a the key from the file system
	 * 
	 * @throws IOException
	 *             When the api_key file is not there
	 */
	private void loadKey() throws IOException {

		File keyFile = new File("data/api_key.txt");

		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(keyFile));
			API_KEY = reader.readLine();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			throw new IOException(
					"API Key File is missing. Add api_key.txt to data folder with a valid key as content.");
		}
		finally {
			reader.close();
		}

		
		
	}

	/**
	 * Implementation for the evaluation method Needs an image of certain dimensions
	 * and will throw exception otherwise. If called the first time will load the
	 * API key from the file system.
	 */
	@Override
	public float evaluate(BufferedImage image) throws Exception {

		EvaluationResult result = evaluateImage(image);

		return result.getMaxValue();
	}

	/**
	 * Sends a given Image to be evaluated by the webserver returning
	 * 
	 * @param image
	 *            The Image that will be send to the server
	 * @return EvaluationResult which contains confidence scores for all classes,
	 *         null for a failed request
	 * @throws Exception
	 *             when image size is wrong or a bad shaped json response
	 */
	@Override
	public EvaluationResult evaluateImage(BufferedImage image) throws Exception {

		// Check if image has neccessary dimensions
		int width = image.getWidth();
		int height = image.getHeight();

		if (width != IMAGE_WIDTH || height != IMAGE_HEIGHT) {
			throw new Exception("Width and height need to be " + IMAGE_WIDTH + "x" + IMAGE_HEIGHT + " but are " + width
					+ "x" + height);
		}

		// If this is first call to programm, load the api key
		if (API_KEY == null) {
			loadKey();
		}

		// Create tmp folder
		new File("data/tmp").mkdirs();

		// Image must be present in file format to upload it TODO: Find workaround
		File imageFile = ImageSaver.saveImage(image, "data/tmp/image");
		
		boolean done = false;
		String response = "";
		
		while(!done) {
			System.out.println("Sending Request...");

			response = sendRequest(imageFile);

			System.out.println("Response: " + response.trim());
			
			if(!response.startsWith("429")) {
				done = true;
				break;
			}
			
			System.out.println("Server is overloaded, waiting 2000ms, then try again");
			Thread.sleep(2000);
		}
		
		if (!response.startsWith("200")) {
			// Invalid response, return null
			return null;
		}

		response = response.replaceAll("200\n", "");

		EvaluationResult result = new EvaluationResult(response);

		return result;
	}

}
