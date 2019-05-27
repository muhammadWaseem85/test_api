package api.page_objects;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import web.config.ConfigFileReader;
import static io.restassured.RestAssured.baseURI;

public class EndUserPage {

    ConfigFileReader configFileReader;

    public String endUserLogin () {

        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("phone","+923347347789");
        json.put("pin", "123456");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .body(json.toString())
                        .post("/login");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        return body;

    }

    public String getToken () {

        JSONObject obj = new JSONObject(endUserLogin());
        String token = (String) obj.get("token");
        return token;

    }


}
