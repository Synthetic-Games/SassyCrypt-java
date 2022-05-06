package com.sassycrypt.sassycrypt.license;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sassycrypt.sassycrypt.util.Helpers;
import java.io.IOException;
import java.util.Date;
import lombok.Getter;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Bryce
 */
public class License {

    @Getter
    private String license_key;
    
    @Getter
    private String machine_code;
    
    @Getter
    private String duration;

    @Getter
    private int timed;
    
    @Getter
    private int max_machines;

    @Getter
    private int expiry;

    @Getter
    private int owner_id;
    
    @Getter
    private String created;


    private static final OkHttpClient client = new OkHttpClient();
    
    private static final Gson gson = new Gson();

    public static License activate(String key) {

        JSONObject obj = new JSONObject();
        obj.put("key", key);
        obj.put("machine_code", Helpers.getHWID());

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), obj.toJSONString());

        Request request = new Request.Builder()
                .url("https://api.sassycrypt.com:8443/api/license/activate")
                .post(body)
                .build();

        try {
            Call call = client.newCall(request);
            Response response = call.execute();
            
            String responseBody = response.body().string();
            
            System.out.println("response: "+responseBody);
            
            JSONParser parser = new JSONParser();
            JSONObject obj2 = (JSONObject)parser.parse(responseBody);
            JSONObject data = (JSONObject)obj2.get("data");
            JSONObject license2 = (JSONObject)data.get("license");
            System.out.println("License data: "+license2.toJSONString());
            License license = gson.fromJson(license2.toJSONString(), License.class);
            
            return license;
            
            
        } catch (JsonSyntaxException | IOException | ParseException ex) {
            System.out.println("Error activating license: " + ex.getMessage());
        }
        
        return new License();

    }
    
    @Override
    public String toString(){
        return "key: "+license_key+" isTimed: "+timed+" hasExpired: "+expiry+" isCorrectMachine: "+machine_code+" owner_id:"+owner_id+" duration:"+duration;
    }

}
