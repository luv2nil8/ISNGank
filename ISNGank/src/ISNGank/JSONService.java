package ISNGank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;
 
public class JSONService
{
    private final String baseUrl;
    private final String username;
    private final String password;
 
    public JSONService(String baseUrl, String username, String password) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
    }
    
    public JSONObject getUsers()
    {
    	return getData("users");
    }
    
    public JSONObject getClients()
    {
    	return getData("clients");
    }
    
    public JSONObject getAgencies()
    {
    	return getData("agencies");
    }   
    
    public JSONObject getAgents()
    {
    	return getData("agents");
    }
    
    public JSONObject getEscrowOffices()
    {
    	return getData("escrowoffices");
    }
    
    public JSONObject getEscrowOfficers()
    {
    	return getData("escrowofficers");
    }

	
	public JSONObject getContacts(){
		return getData("contacts");
	}
	
		
	public JSONObject getData(String item)
	{
    	JSONObject data = new JSONObject(getDataFromServer(item+"/"));
    	//System.out.println(data);
    	JSONArray dataJSON = data.getJSONArray(item);
    	JSONObject JSON = new JSONObject();
    	for (int i = 0; dataJSON.length()>i; i++)
    	{
    		String id = new JSONObject(dataJSON.get(i).toString()).get("id").toString();
    		System.out.println("Finding "+item.substring(0, 1).toUpperCase()+item.substring(1, item.length()-1) +" "+ id);
        	JSONObject returnedData = new JSONObject(getDataFromServer(item+"/"+id));
    		JSON.append(id.toString(), returnedData);
    	}
    	
    	return JSON;
    }
	

    public String getDataFromServer(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(baseUrl + path);
            URLConnection urlConnection = setUsernamePassword(url);
            BufferedReader reader = new BufferedReader(
            		new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
 
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
 
    private URLConnection setUsernamePassword(URL url) throws IOException {
        URLConnection urlConnection = url.openConnection();
        String authString = username + ":" + password;
        String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());;
        urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
        return urlConnection;
    }
}

