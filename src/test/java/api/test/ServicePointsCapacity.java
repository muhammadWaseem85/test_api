package api.test;

import api.page_objects.CapacityPage;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
//import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import web.config.ConfigFileReader;

public class ServicePointsCapacity {

    ConfigFileReader configFileReader;
    ConfigFileReader conf = new ConfigFileReader();
    String size = null;
    Integer depth = null;
    Integer width = null;
    Integer height = null;
    Integer compartment = null;
    Integer used = null;
    Integer free = null;
    String body = null;

    // Here, we are checking the capacity of service point 10075
    // In this service point we have
    // 14 compartments in Small Unit
    // 2 compartments in Medium Unit
    // 6 compartments in Large Unit

    @Test
    public void CheckServicePointCapacity() {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .queryParam("sp_id", conf.getSpId())
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/capacity")
                .then()
                        .extract();
        System.out.println("Test Case CheckServicePointCapacity >>>>>");
        Assert.assertEquals(response.statusCode(), 200);
        body = response.getBody().asString();
        System.out.println(body);
        //response.then().body(matchesJsonSchemaInClasspath("schema/servicePoint/ServicePointCapacity.json"));
    }

    @Test (dependsOnMethods={"CheckServicePointCapacity"}, alwaysRun=true)
    public void ServicePointCapacityWithInvalidSP() {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .queryParam("sp_id", "SP100")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .get("/capacity");
        System.out.println("Test Case ServicePointCapacityWithInvalidSP >>>>>");
        String result = response.getBody().asString();
        System.out.println(result);
        System.out.println(response.statusCode());
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,42);
        Assert.assertEquals(msg, "Invalid service point.");

    }

    @Test (dependsOnMethods={"ServicePointCapacityWithInvalidSP"}, alwaysRun=true)
    public void ServicePointCapacityWithInvalidApiKey() {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .queryParam("sp_id", conf.getSpId())
                        .header("Content-Type","application/json")
                        .header("X-Api-Key","645f8c26-64f3-d9bc-3eb2-57396ab80d7e-7777asdf")
                        .get("/capacity");
        System.out.println("Test Case ServicePointCapacityWithInvalidApiKey >>>>>");
        String result = response.getBody().asString();
        System.out.println(result);
        System.out.println(response.statusCode());
        Assert.assertEquals(response.statusCode(), 403);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,16);
        Assert.assertEquals(msg, "This API key does not have access to the requested controller.");
    }

    @Test (dependsOnMethods={"ServicePointCapacityWithInvalidApiKey"}, alwaysRun=true)
    public void VerifyCompartmentDimensionForSmall() {
        JSONArray jsonarray = new JSONArray(body);
        //System.out.println(String.format("Shipments Points => %d", jsonarray.length()));
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject obj1 = jsonarray.getJSONObject(i);
            JSONArray capacity = (JSONArray) obj1.get("capacity");
            JSONObject obj2 = capacity.getJSONObject(0);
            size = obj2.getString("size");
            depth = obj2.getInt("depth");
            height = obj2.getInt("height");
            width = obj2.getInt("width");
        }

        System.out.println("Size: "+ size + " Depth: " + depth + " Height: "+ height + " Width: " + width );
        Assert.assertEquals(size, conf.getSmallSize());
        String lockerDepth = Integer.toString(depth);
        String lockerWidth = Integer.toString(width);
        String lockerHeight = Integer.toString(height);
        Assert.assertEquals(lockerDepth, conf.getLockerDepth());
        Assert.assertEquals(lockerWidth, conf.getLockerWidth());
        Assert.assertEquals(lockerHeight, conf.getHeightOfSmallLocker());
    }

    @Test (dependsOnMethods={"VerifyCompartmentDimensionForSmall"}, alwaysRun=true)
    public void VerifyCompartmentDimensionForMedium() {
        JSONArray jsonarray = new JSONArray(body);
        //System.out.println(String.format("Shipments Points => %d", jsonarray.length()));
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject obj1 = jsonarray.getJSONObject(i);
            JSONArray capacity = (JSONArray) obj1.get("capacity");
            JSONObject obj2 = capacity.getJSONObject(1);
            size = obj2.getString("size");
            depth = obj2.getInt("depth");
            height = obj2.getInt("height");
            width = obj2.getInt("width");
        }

        System.out.println("Size: "+ size + " Depth: " + depth + " Height: "+ height + " Width: " + width );
        Assert.assertEquals(size, conf.getMediumSize());
        String lockerDepth = Integer.toString(depth);
        String lockerWidth = Integer.toString(width);
        String lockerHeight = Integer.toString(height);
        Assert.assertEquals(lockerDepth, conf.getLockerDepth());
        Assert.assertEquals(lockerWidth, conf.getLockerWidth());
        Assert.assertEquals(lockerHeight, conf.getHeightOfMediumLocker());
    }

    @Test (dependsOnMethods={"VerifyCompartmentDimensionForMedium"}, alwaysRun=true)
    public void VerifyCompartmentDimensionForLarge() {
        JSONArray jsonarray = new JSONArray(body);
        //System.out.println(String.format("Shipments Points => %d", jsonarray.length()));
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject obj1 = jsonarray.getJSONObject(i);
            JSONArray capacity = (JSONArray) obj1.get("capacity");
            JSONObject obj2 = capacity.getJSONObject(2);
            size = obj2.getString("size");
            depth = obj2.getInt("depth");
            height = obj2.getInt("height");
            width = obj2.getInt("width");
        }

        System.out.println("Size: "+ size + " Depth: " + depth + " Height: "+ height + " Width: " + width );
        Assert.assertEquals(size, conf.getLargeSize());
        String lockerDepth = Integer.toString(depth);
        String lockerWidth = Integer.toString(width);
        String lockerHeight = Integer.toString(height);
        Assert.assertEquals(lockerDepth, conf.getLockerDepth());
        Assert.assertEquals(lockerWidth, conf.getLockerWidth());
        Assert.assertEquals(lockerHeight, conf.getHeightOfLargeLocker());
    }

    @Test (dependsOnMethods={"VerifyCompartmentDimensionForLarge"}, alwaysRun=true)
    public void VerifyNumberOfCompartmentsInSmall() {
        JSONArray jsonarray = new JSONArray(body);
        //System.out.println(String.format("Shipments Points => %d", jsonarray.length()));
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject obj1 = jsonarray.getJSONObject(i);
            JSONArray capacity = (JSONArray) obj1.get("capacity");
            JSONObject obj2 = capacity.getJSONObject(0);
            compartment = obj2.getInt("compartments");
        }
        System.out.println("Size: "+ compartment );
        String compartments = Integer.toString(compartment);
        Assert.assertEquals(compartments, conf.getCompartmentsInSmall());
    }

    @Test (dependsOnMethods={"VerifyNumberOfCompartmentsInSmall"}, alwaysRun=true)
    public void VerifyNumberOfCompartmentsInMedium() {
        CapacityPage capacity = new CapacityPage();
        capacity.compartments(body, compartment, 1, conf.getCompartmentsInMedium());

    }

    @Test (dependsOnMethods={"VerifyNumberOfCompartmentsInMedium"}, alwaysRun=true)
    public void VerifyNumberOfCompartmentsInLarge() {
        JSONArray jsonarray = new JSONArray(body);
        //System.out.println(String.format("Shipments Points => %d", jsonarray.length()));
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject obj1 = jsonarray.getJSONObject(i);
            JSONArray capacity = (JSONArray) obj1.get("capacity");
            JSONObject obj2 = capacity.getJSONObject(2);
            compartment = obj2.getInt("compartments");
        }
        System.out.println("Size: "+ compartment );
        String compartments = Integer.toString(compartment);
        Assert.assertEquals(compartments, conf.getCompartmentsInLarge());
    }

    @Test (dependsOnMethods={"VerifyNumberOfCompartmentsInLarge"}, alwaysRun=true)
    public void VerifyNumberOfFreeCompartmentsInSmallUnit() {
        // Hard allocation suite is run before it. There , we do some hard reserves in small which are around 3-5
        // So when we run this case, 3-5 compartments should be minus
        JSONArray jsonarray = new JSONArray(body);
        //System.out.println(String.format("Shipments Points => %d", jsonarray.length()));
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject obj1 = jsonarray.getJSONObject(i);
            JSONArray capacity = (JSONArray) obj1.get("capacity");
            JSONObject obj2 = capacity.getJSONObject(0);
            compartment = obj2.getInt("free");
        }
        System.out.println("Free Compartments in Small Unit : "+ compartment );
        String compartments = Integer.toString(compartment);
        //Assert.assertEquals(compartments, conf.getCompartmentsInLarge());
    }

    @Test (dependsOnMethods={"VerifyNumberOfCompartmentsInLarge"}, alwaysRun=true)
    public void VerifyNumberOfFreeCompartmentsInMediumUnit() {
        JSONArray jsonarray = new JSONArray(body);
        //System.out.println(String.format("Shipments Points => %d", jsonarray.length()));
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject obj1 = jsonarray.getJSONObject(i);
            JSONArray capacity = (JSONArray) obj1.get("capacity");
            JSONObject obj2 = capacity.getJSONObject(1);
            compartment = obj2.getInt("free");
        }
        System.out.println("Free Compartments in Medium Unit : "+ compartment );
        String compartments = Integer.toString(compartment);
        //Assert.assertEquals(compartments, conf.getCompartmentsInLarge());
    }

    @Test (dependsOnMethods={"VerifyNumberOfCompartmentsInLarge"}, alwaysRun=true)
    public void VerifyNumberOfFreeCompartmentsInLargeUnit() {
        JSONArray jsonarray = new JSONArray(body);
        //System.out.println(String.format("Shipments Points => %d", jsonarray.length()));
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject obj1 = jsonarray.getJSONObject(i);
            JSONArray capacity = (JSONArray) obj1.get("capacity");
            JSONObject obj2 = capacity.getJSONObject(2);
            compartment = obj2.getInt("free");
        }
        System.out.println("Free Compartments in Large Unit : "+ compartment );
        String compartments = Integer.toString(compartment);
        //Assert.assertEquals(compartments, conf.getCompartmentsInLarge());
    }

    @Test (dependsOnMethods={"VerifyNumberOfCompartmentsInLarge"}, alwaysRun=true)
    public void VerifyNumberOfUsedCompartmentsInSmallUnit() {
        JSONArray jsonarray = new JSONArray(body);
        //System.out.println(String.format("Shipments Points => %d", jsonarray.length()));
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject obj1 = jsonarray.getJSONObject(i);
            JSONArray capacity = (JSONArray) obj1.get("capacity");
            JSONObject obj2 = capacity.getJSONObject(0);
            compartment = obj2.getInt("used");
        }
        System.out.println("Used Compartments in Small Unit : "+ compartment );
        String compartments = Integer.toString(compartment);
        //Assert.assertEquals(compartments, conf.getCompartmentsInLarge());
    }

    @Test (dependsOnMethods={"VerifyNumberOfCompartmentsInLarge"}, alwaysRun=true)
    public void VerifyNumberOfUsedCompartmentsInMediumUnit() {
        JSONArray jsonarray = new JSONArray(body);
        //System.out.println(String.format("Shipments Points => %d", jsonarray.length()));
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject obj1 = jsonarray.getJSONObject(i);
            JSONArray capacity = (JSONArray) obj1.get("capacity");
            JSONObject obj2 = capacity.getJSONObject(1);
            compartment = obj2.getInt("used");
        }
        System.out.println("Used Compartments in Medium Unit : "+ compartment );
        String compartments = Integer.toString(compartment);
        //Assert.assertEquals(compartments, conf.getCompartmentsInLarge());
    }

    @Test (dependsOnMethods={"VerifyNumberOfCompartmentsInLarge"}, alwaysRun=true)
    public void VerifyNumberOfUsedCompartmentsInLargeUnit() {
        JSONArray jsonarray = new JSONArray(body);
        //System.out.println(String.format("Shipments Points => %d", jsonarray.length()));
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject obj1 = jsonarray.getJSONObject(i);
            JSONArray capacity = (JSONArray) obj1.get("capacity");
            JSONObject obj2 = capacity.getJSONObject(2);
            compartment = obj2.getInt("used");
        }
        System.out.println("Used Compartments in Large Unit : "+ compartment );
        String compartments = Integer.toString(compartment);
        //Assert.assertEquals(compartments, conf.getCompartmentsInLarge());
    }

}


