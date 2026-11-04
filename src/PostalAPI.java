


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.net.HttpURLConnection;
import java.net.URL; 
import java.net.URI; 
import java.util.Scanner;



public class PostalAPI {
    public static JSONObject getSaleTaxData(String postalCode){

        String urlAPI = "https://salestaxzip.com/api/v1/rate/" + postalCode;
        try {
            HttpURLConnection connection = fetchAPIResponse(urlAPI);

            if (connection.getResponseCode() != 200) {
                System.out.println("Error: coulc not connect to the API");
                return null;
            } else {
                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                scanner.close();
                connection.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject dataObject = (JSONObject) parser.parse(String.valueOf(informationString));

                JSONObject data = (JSONObject) dataObject.get("data");
                JSONObject rates = (JSONObject) data.get("rates");


                double combine = ((Number) rates.get("combined")).doubleValue();
                double state = ((Number) rates.get("state")).doubleValue();
                double county = ((Number) rates.get("county")).doubleValue();
                double city = ((Number) rates.get("city")).doubleValue();
                double local = ((Number) rates.get("local")).doubleValue();

                JSONObject result = new JSONObject();
                result.put("combine", combine);
                result.put("state", state);
                result.put("county", county);
                result.put("city", city);
                result.put("local", local);


                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static HttpURLConnection fetchAPIResponse(String urlAPI){
        try {
            URL url = new URI(urlAPI).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        } catch (Exception e) {
            // throw new RuntimeException(e);
            e.printStackTrace();
        }

        return null;

    }
}
