package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import menuing.entity.Token;
import menuing.entity.User;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonBuilderFactory;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.json.JsonObjectBuilder;

@Stateless
public class Tokens {
    @PersistenceContext(name = "menuing")
    EntityManager em;
    
    public List<Token> findAll() {
        return this.em.createNamedQuery(Token.FIND_ALL).getResultList();
    }
    
    public void create(Token token) {
        this.em.persist(token);
    }
    
    public void remove(Token token) {
        this.em.remove(token);
    }
    
    public void callFirebase(String token, String message) throws Exception{
        String authKey = "AAAAi_9cHsE:APA91bEtiBgzX5L-uOYMlhsvnKusByMGVIiWQM0ph_whbe7aodrbwP7JzDCkGuIAjq0gc8IP4OKlK4aEqMiFi-0hCFeoyR3R88bNGvM1UVE-1WPJGlotTo0xkfUlUdR6Ny9u6EwQYOYS";
        String FMCurl = "https://fcm.googleapis.com/fcm/send";

        URL url = new URL(FMCurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + authKey);
        conn.setRequestProperty("Content-Type", "application/json");

        JsonObjectBuilder dataB = Json.createObjectBuilder();
        dataB.add("to", token.trim());

        JsonObjectBuilder infoB = Json.createObjectBuilder();
        
        infoB.add("title", "Menuing"); // Notification title
        infoB.add("body", message); // Notification body
        dataB.add("data", infoB.build());

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(dataB.build().toString());
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        System.out.println("Response : " + response);
        in.close();
        
    }
}
