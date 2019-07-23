package group10.utility;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class Responsible for Uploading Files to imgur.
 */

public class ImgurUploader {
    public static final String API_URL = "https://api.imgur.com/3/image";
    private final static String CLIENT_ID = "c3796a84e7bd8af";

    /**
     * Takes a String and uploads it to Imgur.
     * Does not check to see if the file is an image, this should be done
     * before the file is passed to this method.
     *
     * @param file The image to be uploaded to Imgur.
     * @return The JSON response from Imgur.
     */
    public static String upload(File file) throws IOException {
        if (file == null) {
            throw new IOException("File passed in is empty");
        }

        HttpURLConnection connection = getHttpConnection(API_URL);
        writeToConnection(connection, "image=" + toBase64(file));
        String jsonResponse = getJsonResponse(connection);
        String result = getUrlFromJson(jsonResponse);
        // delete file after result is processed.
        file.delete();
        return getUrlFromJson(jsonResponse);
    }

    public static File getFileFromMulti(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private static String getUrlFromJson(String jsonResponse) {
        JSONObject object = new JSONObject(jsonResponse);
        return object.getJSONObject("data").getString("link");
    }


    /**
     * Converts a file to a Base64 String.
     *
     * @param file The file to be converted.
     * @return The file as a Base64 String.
     */
    private static String toBase64(File file) {
        try {
            byte[] b = new byte[(int) file.length()];
            FileInputStream fs = new FileInputStream(file);
            fs.read(b);
            fs.close();
            return URLEncoder.encode(DatatypeConverter.printBase64Binary(b), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates and sets up an HttpURLConnection for use with the Imgur API.
     *
     * @param url The URL to connect to.
     * @return The newly created HttpURLConnection.
     */
    private static HttpURLConnection getHttpConnection(String url) {
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
            conn.setReadTimeout(100000);
            conn.connect();
            return conn;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sends the provided content to the connection as uploaded data.
     *
     * @param connection The connection to send the data to.
     * @param content    The data to upload.
     */
    private static void writeToConnection(HttpURLConnection connection, String content) {
        OutputStreamWriter writer;
        try {
            writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the response from the connection, Usually in the format of a JSON string.
     *
     * @param conn The connection to listen to.
     * @return The response, usually as a JSON string.
     */
    private static String getJsonResponse(HttpURLConnection conn) {
        StringBuilder str = new StringBuilder();
        BufferedReader reader;
        try {
            if (conn.getResponseCode() != 200) {
                System.out.println("No good response");
            }
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (str.toString().equals("")) {
            System.out.println("Empty response");
            return null;
        }
        return str.toString();
    }


    public static File toFile(BufferedImage img) throws IOException {
        File out = new File("image.jpg");
        ImageIO.write(img, "jpg", out);
        return out;
    }

    public static BufferedImage getImageFromString(String s) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Files.newInputStream(Paths.get(s)));
        } catch (IOException e) {
            System.out.println("File NOt Found from String");
            e.printStackTrace();
        } finally {
            return image;
        }

    }

}




