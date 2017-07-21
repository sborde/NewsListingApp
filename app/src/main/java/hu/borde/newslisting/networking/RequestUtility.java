package hu.borde.newslisting.networking;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.borde.newslisting.model.News;

/**
 * Created by borde on 2017.06.18..
 */

public class RequestUtility {

    private RequestUtility(){}

    private final static String LOG_TAG = "RequestUtils";
    private static final int READ_TIMEOUT = 1000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int OK_RESPONSE = 200;
    private static final int ERROR_RESPONSE = 400;

    public static List<News> fetchNews(String url) throws IOException {

        if ("".equals(url)) {
            return null;
        }

        URL queryUrl = createUrl(url);
        String resultJson = makeHttpRequest(queryUrl);

        return extractNews(resultJson);

    }

    private static List<News> extractNews(String resultJson) {

        if (resultJson == null || resultJson.isEmpty()) {
            return null;
        }

        List<News> newsList = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(resultJson);
            root = root.getJSONObject("response");
            JSONArray items = root.getJSONArray("results");
            int numberOfItems = items.length();

            for (int i = 0 ; i < numberOfItems ; i++) {
                JSONObject thisNewsInfo = (JSONObject)items.get(i);

                String title = thisNewsInfo.getString("webTitle");

                String topic = thisNewsInfo.getString("sectionName");

                long publishedDate = -1;
                if (thisNewsInfo.has("webPublicationDate")) {
                    publishedDate = getPublishingDate(thisNewsInfo);
                }

                String author = null;
                if (thisNewsInfo.has("author")) {
                    author = thisNewsInfo.getString("author");
                }

                String url = thisNewsInfo.getString("webUrl");


                News thisBook = new News(title, author, url, topic, publishedDate);
                newsList.add(thisBook);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }

        // Return the list of books
        return newsList;

    }

    private static long getPublishingDate(JSONObject bookInfo) {

        long returnValue = -1;

        String dateString = "";

        try {
            dateString = bookInfo.getString("webPublicationDate");
        } catch (JSONException e) {
            Log.e("GetPublishingDate", "Date field not found." + e);
            return returnValue;
        }

        Date publishedDate = null;
        try {
            SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Pattern datePattern = Pattern.compile("([0-9]{4}-[0-9]{2}-[0-9]{2}).*");
            Matcher foundPattern = datePattern.matcher(dateString);


            if (foundPattern.find()) {
                dateString = foundPattern.group(1);

                publishedDate = fullDateFormat.parse(dateString);
            }
        } catch (ParseException e) {
            Log.e("GetPublishingDate", "Not full date format." + e);
        }

        if (publishedDate != null) {
            return publishedDate.getTime();
        }

        if (publishedDate == null) {
            return -1;
        } else {
            return publishedDate.getTime();
        }
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(READ_TIMEOUT /* milliseconds */);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT /* milliseconds */);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == OK_RESPONSE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else if (urlConnection.getResponseCode() == ERROR_RESPONSE) {
                Log.e(LOG_TAG, "Error code: " + String.valueOf(urlConnection.getResponseCode()));
                jsonResponse = "";
            }


        } catch (IOException e) {
            Log.e(LOG_TAG, "Exception caught: " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
