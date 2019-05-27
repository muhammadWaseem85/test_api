package api.test;

import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import web.config.ConfigFileReader;

public class ServicePointLatLong {

    ConfigFileReader configFileReader;
    ConfigFileReader conf = new ConfigFileReader();
    Integer sp_id = null;
    Integer alt_sp_id = null;
    Integer depth = null;
    Integer width = null;
    Integer height = null;
    Integer compartment = null;
    String body = null;

    @Test
    public void FindServicePointWithLatAndLng() {
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "100000")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/service_points")
                        .then()
                        .extract();
        System.out.println("Test Case FindServicePointWithLatAndLng >>>>>");
        System.out.println(response.statusCode());
        Assert.assertEquals(response.statusCode(), 200);
        body = response.getBody().asString();
        System.out.println(body);
        response.then().body(matchesJsonSchemaInClasspath("schema/servicePoint/FindServicePoint.json"));
    }

    @Test (dependsOnMethods={"FindServicePointWithLatAndLng"}, alwaysRun=true)
    public void VerifyServicePoint() {
        JSONArray jsonarray = new JSONArray(body);
        //System.out.println(String.format("Shipments Points => %d", jsonarray.length()));
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject obj1 = jsonarray.getJSONObject(0);
            sp_id = obj1.getInt("sp_id");
        }
        System.out.println("Test Case VerifyServicePoint >>>>>");
        System.out.println("Service Point: "+ sp_id );
        String servicePoint = Integer.toString(sp_id);
        Assert.assertEquals(servicePoint, conf.getSpId());
    }

    @Test (dependsOnMethods={"VerifyServicePoint"}, alwaysRun=true)
    public void VerifyAlternateServicePoint() {
        JSONArray jsonarray = new JSONArray(body);
        //System.out.println(String.format("Shipments Points => %d", jsonarray.length()));
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject obj1 = jsonarray.getJSONObject(0);
            alt_sp_id = obj1.getInt("alt_sp_id");
        }
        System.out.println("Test Case VerifyAlternateServicePoint >>>>>");
        System.out.println("Alternate Service Point: "+ alt_sp_id );
        String altServicePoint = Integer.toString(alt_sp_id);
        Assert.assertEquals(altServicePoint, conf.getAltSpID());
    }

    @Test (dependsOnMethods={"VerifyAlternateServicePoint"}, alwaysRun=true)
    public void FindServicePointWithInvalidApiKey() {
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "100000")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key","645f8c26-64f3-d9bc-3eb2-57396ab80d7e-abcd1234")
                        .get("/service_points")
                        .then()
                        .extract();
        System.out.println("Test Case FindServicePointWithInvalidApiKey >>>>>");
        System.out.println(response.statusCode());
        Assert.assertEquals(response.statusCode(), 403);
        String result = response.getBody().asString();
        System.out.println(result);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,16);
        Assert.assertEquals(msg, "This API key does not have access to the requested controller.");

    }

    @Test (dependsOnMethods={"FindServicePointWithInvalidApiKey"}, alwaysRun=true)
    public void FindServicePointWithMissingQueryParameters() {
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/service_points")
                        .then()
                        .extract();
        System.out.println("Test Case FindServicePointWithMissingQueryParameters >>>>>");
        System.out.println(response.statusCode());
        Assert.assertEquals(response.statusCode(), 200);
        String result = response.getBody().asString();
        System.out.println(result);

    }

    @Test (dependsOnMethods={"FindServicePointWithMissingQueryParameters"}, alwaysRun=true)
    public void FindServicePointWithMissingLatitude() {
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "100000")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/service_points")
                        .then()
                        .extract();
        System.out.println("Test Case FindServicePointWithMissingLatitude >>>>>");
        System.out.println(response.statusCode());
        Assert.assertEquals(response.statusCode(), 400);
        String result = response.getBody().asString();
        System.out.println(result);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,82);
        Assert.assertEquals(msg, "Latitude, longitude and distance are mandatory parameters.");

    }

    @Test (dependsOnMethods={"FindServicePointWithMissingLatitude"}, alwaysRun=true)
    public void FindServicePointWithInvalidLatitude() {
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("latitude", "a")
                        .and()
                        .queryParam("distance", "100000")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key", conf.getApiKey())
                        .get("/service_points")
                .then()
                        .extract();
        System.out.println("Test Case FindServicePointWithInvalidLatitude >>>>>");
        System.out.println(response.statusCode());
        Assert.assertEquals(response.statusCode(), 400);
        String result = response.getBody().asString();
        System.out.println(result);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,20);
        Assert.assertEquals(msg, "Incorrect parameter.");
    }

    @Test (dependsOnMethods={"FindServicePointWithInvalidLatitude"}, alwaysRun=true)
    public void FindServicePointWithMissingLongitude() {
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("distance", "100000")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/service_points")
                        .then()
                        .extract();
        System.out.println("Test Case FindServicePointWithMissingLongitude >>>>>");
        System.out.println(response.statusCode());
        Assert.assertEquals(response.statusCode(), 400);
        String result = response.getBody().asString();
        System.out.println(result);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,82);
        Assert.assertEquals(msg, "Latitude, longitude and distance are mandatory parameters.");

    }

    @Test (dependsOnMethods={"FindServicePointWithMissingLatitude"}, alwaysRun=true)
    public void FindServicePointWithInvalidLongitude() {
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .queryParam("longitude","a")
                        .and()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("distance", "100000")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key", conf.getApiKey())
                        .get("/service_points")
                        .then()
                        .extract();
        System.out.println("Test Case FindServicePointWithInvalidLongitude >>>>>");
        System.out.println(response.statusCode());
        Assert.assertEquals(response.statusCode(), 400);
        String result = response.getBody().asString();
        System.out.println(result);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,20);
        Assert.assertEquals(msg, "Incorrect parameter.");
    }

    @Test (dependsOnMethods={"FindServicePointWithMissingLongitude"}, alwaysRun=true)
    public void FindServicePointWithMissingDistance() {
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key","645f8c26-64f3-d9bc-3eb2-57396ab80d7e")
                        .get("/service_points")
                        .then()
                        .extract();
        System.out.println("Test Case FindServicePointWithMissingDistance >>>>>");
        System.out.println(response.statusCode());
        Assert.assertEquals(response.statusCode(), 400);
        String result = response.getBody().asString();
        System.out.println(result);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,82);
        Assert.assertEquals(msg, "Latitude, longitude and distance are mandatory parameters.");

    }

    @Test (dependsOnMethods={"FindServicePointWithMissingLatitude"}, alwaysRun=true)
    public void FindServicePointWithInvalidDistance() {
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .queryParam("longitude","73.687956")
                        .and()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("distance", "a")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key", conf.getApiKey())
                        .get("/service_points")
                        .then()
                        .extract();
        System.out.println("Test Case FindServicePointWithInvalidDistance >>>>>");
        System.out.println(response.statusCode());
        Assert.assertEquals(response.statusCode(), 400);
        String result = response.getBody().asString();
        System.out.println(result);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,20);
        Assert.assertEquals(msg, "Incorrect parameter.");
    }

    @Test (dependsOnMethods={"FindServicePointWithInvalidDistance"}, alwaysRun=true)
    public void FindServicePointWithMinLengthDistance() {
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .queryParam("longitude","73.687956")
                        .and()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("distance", "1")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key", conf.getApiKey())
                        .get("/service_points")
                        .then()
                        .extract();
        System.out.println("Test Case FindServicePointWithMinLengthDistance >>>>>");
        System.out.println(response.statusCode());
        Assert.assertEquals(response.statusCode(), 200);
        String result = response.getBody().asString();
        System.out.println(result);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,23);
        Assert.assertEquals(msg, "No record found.");
    }

    @Test (dependsOnMethods={"FindServicePointWithInvalidDistance"}, alwaysRun=true)
    public void FindServicePointWithMaxLengthDistance() {
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .queryParam("longitude","73.687956")
                        .and()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("distance", "1000000000000000000000000000")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key", conf.getApiKey())
                        .get("/service_points")
                        .then()
                        .extract();
        System.out.println("Test Case FindServicePointWithMaxLengthDistance >>>>>");
        System.out.println(response.statusCode());
        Assert.assertEquals(response.statusCode(), 400);
        String result = response.getBody().asString();
        System.out.println(result);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,20);
        Assert.assertEquals(msg, "Incorrect parameter.");
    }

}


