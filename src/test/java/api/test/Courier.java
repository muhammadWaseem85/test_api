package api.test;

import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.*;
import java.util.Map;
import org.json.simple.JSONObject;
import web.config.ConfigFileReader;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class Courier {
    ConfigFileReader conf = new ConfigFileReader();

    @Test
    public void GetShipmentForCourierWithNoData() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "10.000")
                        .and()
                        .queryParam("longitude","10.000")
                        .and()
                        .queryParam("distance", "100000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("order_by", "ASC")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key", conf.getApiKey())
                        .get("/shipments_v2")
                        .then().extract();
        System.out.println("Test Case GetShipmentForCourierWithNoData >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/courier/ShipmentForCourier.json"));
    }

    @Test (dependsOnMethods={"GetShipmentForCourierWithNoData"}, alwaysRun=true)
    public void GetShipmentForCourier() {
        baseURI = conf.getCourierApiUrl();
        Response response =
                (Response) given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "100000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("order_by", "ASC")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then().extract();
        System.out.println("Test Case GetShipmentForCourier >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/courier/ShipmentForCourier.json"));
    }

    @Test (dependsOnMethods={"GetShipmentForCourier"}, alwaysRun=true)
    public void GetShipmentWIthTokenValueZero() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "100000")
                        .and()
                        .queryParam("send_token", "0")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("order_by", "ASC")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then().extract();
        System.out.println("Test Case GetShipmentWIthTokenValueZero >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/courier/ShipmentForCourier.json"));
    }

    @Test (dependsOnMethods={"GetShipmentWIthTokenValueZero"}, alwaysRun=true)
    public void GetShipmentWIthTokenValueOne() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "100000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("order_by", "ASC")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then().extract();
        System.out.println("Test Case GetShipmentWIthTokenValueOne >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/courier/ShipmentForCourier.json"));

    }

    @Test (dependsOnMethods={"GetShipmentWithMissingAPIKey"}, alwaysRun=true)
    public void GetShipmentWIthInvalidTokenValue() {
        baseURI = conf.getCourierApiUrl();
        Response response =
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "100000")
                        .and()
                        .queryParam("send_token", "5")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("order_by", "ASC")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2");
        System.out.println("Test Case GetShipmentWIthInvalidTokenValue >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        JSONArray array  = new JSONArray(body);
        for (int i = 0; i < array.length(); i++) {
            org.json.JSONObject responseBody = array.getJSONObject(i);
            int code = responseBody.getInt("code");
            Assert.assertEquals( code,20);
            String msg = responseBody.getString("message");
            Assert.assertEquals(msg, "Incorrect value '5' provided for parameter 'send_token'.");
        }
    }

    @Test (dependsOnMethods={"GetShipmentWIthTokenValueOne"}, alwaysRun=true)
    public void GetShipmentWithEmptyParam() {
        baseURI = conf.getCourierApiUrl();
        Response response =
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2");
        System.out.println("Test Case GetShipmentWithEmptyParam >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
    }

    @Test (dependsOnMethods={"GetShipmentWithEmptyParam"}, alwaysRun=true)
    public void GetShipmentWithInvalidAPIKey() {
        baseURI = conf.getCourierApiUrl();
        Response response =
                given()
                        .queryParam("latitude", "10.000")
                        .and()
                        .queryParam("longitude","10.000")
                        .and()
                        .queryParam("distance", "100000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("order_by", "ASC")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key","a645f8c26-64f3-d9bc-3eb2-57396ab80d7e-pp747abcd1234")
                        .get("/shipments_v2");
        System.out.println("Test Case GetShipmentWithInvalidAPIKey >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 403);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,11);
        Assert.assertEquals(msg, "This API key does not have access to the requested resource.");

    }

    @Test (dependsOnMethods={"GetShipmentWithInvalidAPIKey"}, alwaysRun=true)
    public void GetShipmentWithMissingAPIKey() {
        baseURI = conf.getCourierApiUrl();
        Response response =
                given()
                        .queryParam("latitude", "10.000")
                        .and()
                        .queryParam("longitude","10.000")
                        .and()
                        .queryParam("distance", "100000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("order_by", "ASC")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .get("/shipments_v2");
        System.out.println("Test Case GetShipmentWithMissingAPIKey >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 403);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,12);
        Assert.assertEquals(msg, "Auth token is unrecognized.");
        System.out.println(response.statusCode());
    }

    @Test (dependsOnMethods={"GetShipmentWIthInvalidTokenValue"}, alwaysRun=true)
    public void GetShipmentWithEmptyDistance() {
        baseURI = conf.getCourierApiUrl();
        Response response =
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("order_by", "ASC")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2");
        System.out.println("Test Case GetShipmentWithEmptyDistance >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        JSONArray array  = new JSONArray(body);
        for (int i = 0; i < array.length(); i++) {
            org.json.JSONObject responseBody = array.getJSONObject(i);
            int code = responseBody.getInt("code");
            Assert.assertEquals( code,20);
            String msg = responseBody.getString("message");
            Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'distance'.");
        }
    }

    @Test (dependsOnMethods={"GetShipmentWithEmptyDistance"}, alwaysRun=true)
    public void GetShipmentWithIncorrectDistance() {
        baseURI = conf.getCourierApiUrl();
        Response response =
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "abc")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("order_by", "ASC")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2");
        System.out.println("Test Case GetShipmentWithIncorrectDistance >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        JSONArray array  = new JSONArray(body);
        for (int i = 0; i < array.length(); i++) {
            org.json.JSONObject responseBody = array.getJSONObject(i);
            int code = responseBody.getInt("code");
            Assert.assertEquals( code,20);
            String msg = responseBody.getString("message");
            Assert.assertEquals(msg, "Incorrect value 'abc' provided for parameter 'distance'.");
        }
    }

    @Test (dependsOnMethods={"GetShipmentWithIncorrectDistance"}, alwaysRun=true)
    public void GetShipmentWithMaxLengthDistance() {
        baseURI = conf.getCourierApiUrl();
        Response response =
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "10000000000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("order_by", "ASC")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2");
        System.out.println("Test Case GetShipmentWithMaxLengthDistance >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        JSONArray array  = new JSONArray(body);
        for (int i = 0; i < array.length(); i++) {
            org.json.JSONObject responseBody = array.getJSONObject(i);
            int code = responseBody.getInt("code");
            Assert.assertEquals( code,20);
            String msg = responseBody.getString("message");
            Assert.assertEquals(msg, "Incorrect value '10000000000' provided for parameter 'distance'.");
        }
    }

    @Test (dependsOnMethods={"GetShipmentWithIncorrectDistance"}, alwaysRun=true)
    public void GetShipmentWithDefaultValueOfParamSortBy() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "10000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then()
                        .extract();
        System.out.println("Test Case GetShipmentWithDefaultValueOfParamSortBy >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/courier/ShipmentForCourier.json"));
    }

    @Test (dependsOnMethods={"GetShipmentWithDefaultValueOfParamSortBy"}, alwaysRun=true)
    public void GetShipmentWitSortByDistance() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "10000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then()
                        .extract();
        System.out.println("Test Case GetShipmentWitSortByDistance >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/courier/ShipmentForCourier.json"));
    }

    @Test (dependsOnMethods={"GetShipmentWitSortByDistance"}, alwaysRun=true)
    public void GetShipmentWitSortBySP_ID() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "10000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "sp_id")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then()
                        .extract();
        System.out.println("Test Case GetShipmentWitSortBySP_ID >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/courier/ShipmentForCourier.json"));
    }

    @Test (dependsOnMethods={"GetShipmentWitSortBySP_ID"}, alwaysRun=true)
    public void GetShipmentWithSortByAlt_Sp_ID() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "10000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "alt_sp_id")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then()
                        .extract();
        System.out.println("Test Case GetShipmentWithSortByAlt_Sp_ID >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/courier/ShipmentForCourier.json"));
    }

    @Test (dependsOnMethods={"GetShipmentWithSortByAlt_Sp_ID"}, alwaysRun=true)
    public void GetShipmentWithSortByZip() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "10000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "zip")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then()
                        .extract();
        System.out.println("Test Case GetShipmentWithSortByZip >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/courier/ShipmentForCourier.json"));
    }

    @Test (dependsOnMethods={"GetShipmentWithSortByZip"}, alwaysRun=true)
    public void GetShipmentWithDefaultValueOFOrderBy() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "10000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then()
                        .extract();
        System.out.println("Test Case GetShipmentWithDefaultValueOFOrderBy >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/courier/ShipmentForCourier.json"));
    }

    @Test (dependsOnMethods={"GetShipmentWithDefaultValueOFOrderBy"}, alwaysRun=true)
    public void GetShipmentWithMissingLimit() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "10000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then()
                        .extract();
        System.out.println("Test Case GetShipmentWithMissingLimit >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/courier/ShipmentForCourier.json"));
    }

    @Test (dependsOnMethods={"GetShipmentWithMissingLimit"}, alwaysRun=true)
    public void GetShipmentWithInvalidLimit() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "10000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("limit" , "jj")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then()
                        .extract();
        System.out.println("Test Case GetShipmentWithInvalidLimit >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        JSONArray array  = new JSONArray(body);
        for (int i = 0; i < array.length(); i++) {
            org.json.JSONObject responseBody = array.getJSONObject(i);
            int code = responseBody.getInt("code");
            Assert.assertEquals( code,20);
            String msg = responseBody.getString("message");
            Assert.assertEquals(msg, "Incorrect value 'jj' provided for parameter 'limit'.");
        }

    }

    @Test (dependsOnMethods={"GetShipmentWithInvalidLimit"}, alwaysRun=true)
    public void GetShipmentWithMinLengthLimit() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "10000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("limit" , "1")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then()
                        .extract();
        System.out.println("Test Case GetShipmentWithMinLengthLimit >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/courier/ShipmentForCourier.json"));
    }

    @Test (dependsOnMethods={"GetShipmentWithMinLengthLimit"}, alwaysRun=true)
    public void GetShipmentWithMaxLengthLimit() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "10000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("limit" , "100000000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then()
                        .extract();
        System.out.println("Test Case GetShipmentWithMaxLengthLimit >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        JSONArray array  = new JSONArray(body);
        for (int i = 0; i < array.length(); i++) {
            org.json.JSONObject responseBody = array.getJSONObject(i);
            int code = responseBody.getInt("code");
            Assert.assertEquals( code,20);
            String msg = responseBody.getString("message");
            Assert.assertEquals(msg, "Incorrect value '100000000' provided for parameter 'limit'.");
        }
    }

    @Test (dependsOnMethods={"GetShipmentWithMaxLengthLimit"}, alwaysRun=true)
    public void GetShipmentWithInvalidOffSet() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "10000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","74c")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then()
                        .extract();
        System.out.println("Test Case GetShipmentWithInvalidOffSet >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        JSONArray array  = new JSONArray(body);
        for (int i = 0; i < array.length(); i++) {
            org.json.JSONObject responseBody = array.getJSONObject(i);
            int code = responseBody.getInt("code");
            Assert.assertEquals( code,20);
            String msg = responseBody.getString("message");
            Assert.assertEquals(msg, "Incorrect value '74c' provided for parameter 'offset'.");
        }
    }

    @Test (dependsOnMethods={"GetShipmentWithInvalidOffSet"}, alwaysRun=true)
    public void GetShipmentWithMaxLengthOffSet() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "10000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","10000000000")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then()
                        .extract();
        System.out.println("Test Case GetShipmentWithMaxLengthOffSet >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        JSONArray array  = new JSONArray(body);
        for (int i = 0; i < array.length(); i++) {
            org.json.JSONObject responseBody = array.getJSONObject(i);
            int code = responseBody.getInt("code");
            Assert.assertEquals( code,20);
            String msg = responseBody.getString("message");
            Assert.assertEquals(msg, "Incorrect value '10000000000' provided for parameter 'offset'.");
        }
    }

    @Test (dependsOnMethods={"GetShipmentWithIncorrectDistance"}, alwaysRun=true)
    public void GetShipmentWithDefaultOffSet() {
        baseURI = conf.getCourierApiUrl();
        Response response = (Response)
                given()
                        .queryParam("latitude", "33.687956")
                        .and()
                        .queryParam("longitude","73.0003609")
                        .and()
                        .queryParam("distance", "10000")
                        .and()
                        .queryParam("send_token", "1")
                        .and()
                        .queryParam("sort_by", "distance")
                        .and()
                        .queryParam("limit" , "1000")
                        .and()
                        .queryParam("offset","0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/shipments_v2")
                        .then()
                        .extract();
        System.out.println("Test Case GetShipmentWithDefaultOffSet >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/courier/ShipmentForCourier.json"));
    }

    @Test (dependsOnMethods={"GetShipmentWithDefaultOffSet"}, alwaysRun=true)
    public void CourierShipmentUpdateRequestTypeError () {
        Map<String, Object> map = new HashMap<>();
        map.put("request_type","error");
        map.put("data", Arrays.asList(new HashMap<String, Object>() {{
                                          put("uid", "032ded01-e712-490c-b453-06cfdbf97f1f");
                                          put("piece_id","ZHtst103");
                                          put("event_type","103");
                                          //put("timestamp","1554712450");
                                      }}
                                  ));
        String jsonText = JSONValue.toJSONString(map);
        System.out.println(jsonText);
        baseURI = "https://infinity.swipbox.com/prepilotapi/courier-api/v1";
        Response response =
                (Response) given()
                        .header("request_type" , "error")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body("["+jsonText+"]")
                        .patch("/shipments");
        System.out.println("Test Case CreateANewShipment >>>>>");
        System.out.println("["+jsonText+"]");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
    }

    @Test (dependsOnMethods={"GetShipmentForCourier"}, alwaysRun=true)
    public void CourierMultipleTokens () throws IOException, ParseException {
        JSONObject object = new JSONObject();
        object.put("request_type","tokens");
        JSONArray array = new JSONArray();
        JSONObject arrayElementOne = new JSONObject();
        arrayElementOne.put("uid", "00000000-4109-4E45-0034-40140000003F");
        Map<String, Object> map = new HashMap<String, Object>();
        List<Object> list = new ArrayList<>();
        list.add("MCw2NCwwLDQ4LDAsNjQsMTAyLDAsNTYsMTYzLDE1MSwxMTAsMjAyLDE0MCwyMTksMjI5LDE5NiwxOTYsMTA0LDIsNjEsMjQyLDMsMjQ1LDIxLDMsOTUsMTE0LDI3LDEwLDE2OSwxMzQsMjI5LDEwMiwyMDcsMTY1LDE4MiwxNSwyMDgsMjIsMjQ5LDE0OSwyMjUsMjAwLDI0MCwyMDIsNzgsMjUzLDE0NCwyMDQsMjM0LDIyMiwxOTcsNjEsOTgsNjIsOTEsMjI1LDIwMyw3OCw1MiwzOSw1NSwyMzcsNzIsMTk3LDM4LDE0MCwyOCwxNSwyMjcsMTQsNiwyNTMsMjQ3LDM0LDE4MCwxMjIsMjMzLDMsODQsMTI4LDI0NywxNDQsMTkxLDE1NSwxMDEsMTUxLDc5LDIwMCw1NCwzLDIyOCwyMzQsMjI3LDI0MiwyMTAsMTcsMjEwLDE4NiwzMiwxNDIsNDcsMTcxLDE2NCwyMzEsNDEsMjI0LDY0LDIzMSw4Niw2OCwxMDEsMTAyLDU4LDQ1LDEzMywyMDMsMTExLDEzNSwxMzYsMjEzLDEwMCwyNDAsNDUsMTg1LDE3NCwxMDYsMTcwLDEyMSw4LDI4LDUxLDIxOSw0NSwyMSwxOCw5NSw0OSwxMDYsMTI2LDE0MywxMDksOTksMTc1LDMyLDIzNCw3MSwxOTcsMjI3LDE2MSwyMzUsODUsMTc1LDExOCwxOTgsMjM2LDE5OCw4Niw0OSwxOTAsMjQsNzQsMzQsOTEsMiwyMDgsMTkwLDc2LDIwMywxMTgsMTk5LDE0Miw2LDIwMCw4OCwzLDE4MiwxNzcsNDUsODIsNzY=");
        map.put("tokens", list);
        arrayElementOne.putAll(map);
        Map<String, Object> map2 = new HashMap<String, Object>();
        List<Object> list2 = new ArrayList<>();
        list2.add("ABD0000004");
        map2.put("pieceIds", list2);
        arrayElementOne.putAll(map2);
        JSONArray arrayElementOneArray = new JSONArray();
        JSONObject arrayElementOneArrayElementOne = new JSONObject();
        arrayElementOneArray.put(arrayElementOneArrayElementOne);
        array.put(arrayElementOne);
        object.put("data", array);
        //System.out.println(object);
        baseURI = "https://infinity.swipbox.com/prepilotapi/courier-api/v1";
        Response response =
                (Response) given()
                        .header("request_type" , "error")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body("["+object+"]")
                        .patch("/shipments");
        System.out.println("Test Case CreateANewShipment >>>>>");
        //System.out.println("["+object+"]");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        String bodyminus = (body.substring(1, body.length()-1));
        //System.out.println(bodyminus);
        System.out.println(body);
//        String token = response.path("tokens");
//        System.out.println(token);
    }



}
