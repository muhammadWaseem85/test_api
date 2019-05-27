package api.test;

import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import web.config.ConfigFileReader;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ShipmentPatch {

    ConfigFileReader conf = new ConfigFileReader();

    @AfterMethod
    public void afterMethod(ITestResult result) {
        System.out.println("Test Case: " + result.getMethod().getMethodName());
    }


    @Test
    public void CreateShipmentForUpdate () {
        JSONObject json = new JSONObject();
        json.put("piece_id", conf.getFourthPiece());
        json.put("sp_id", conf.getSpId());
        json.put("sms",conf.getPhNumber());
        json.put("alloc_type","1");
        json.put("shipment_type", "1");
        json.put("width", "5");
        json.put("height","10");
        json.put("depth", "5");
        json.put("piece_group_code", conf.getPieceGroupCode());
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");

    }

    @Test (dependsOnMethods={"CreateShipmentForUpdate"}, alwaysRun=true)
    public void UpdateAllPramsMissing () {
        JSONObject json = new JSONObject();
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,70);
        Assert.assertEquals(msg, "Request failed.");
    }

    @Test (dependsOnMethods={"UpdateAllPramsMissing"}, alwaysRun=true)
    public void UpdateAllocTypeMissing () {
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFourthPiece());
        json.put("sp_id", conf.getSpId());
        json.put("sms",conf.getPhNumber());
        json.put("alloc_type","");
        json.put("piece_group_code", conf.getPieceGroupCode());
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,70);
        Assert.assertEquals(msg, "Request failed.");
    }

    @Test (dependsOnMethods={"UpdateAllPramsMissing"}, alwaysRun=true)
    public void UpdateAllocTypeInvalid () {
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFourthPiece());
        json.put("sp_id", conf.getSpId());
        json.put("sms",conf.getPhNumber());
        json.put("alloc_type","klsdfjllls");
        json.put("piece_group_code", conf.getPieceGroupCode());
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,70);
        Assert.assertEquals(msg, "Request failed.");
    }

    @Test (dependsOnMethods={"UpdateAllPramsMissing"}, alwaysRun=true)
    public void UpdatePieceIdMissing () {
        JSONObject json = new JSONObject();
        json.put("piece_id","");
        json.put("sp_id", conf.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("piece_group_code", conf.getPieceGroupCode());
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'piece_id'.");
    }

    @Test (dependsOnMethods={"UpdatePieceIdMissing"}, alwaysRun=true)
    public void UpdatePieceUidMissing () {
        JSONObject json = new JSONObject();
        json.put("piece_uid","");
        json.put("sp_id", conf.getSpId());
        json.put("sms",conf.getPhNumber());
        json.put("alloc_type","1");
        json.put("piece_group_code", conf.getPieceGroupCode());
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'piece_uid'.");
    }

    @Test (dependsOnMethods={"UpdatePieceUidMissing"}, alwaysRun=true)
    public void UpdateInvalidPieceId () {
        JSONObject json = new JSONObject();
        json.put("piece_id","GE-");
        json.put("sp_id", conf.getSpId());
        json.put("sms",conf.getPhNumber());
        json.put("alloc_type","1");
        json.put("piece_group_code", conf.getPieceGroupCode());
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value 'GE-' provided for parameter 'piece_id'.");
    }

    @Test (dependsOnMethods={"UpdateInvalidPieceId"}, alwaysRun=true)
    public void UpdateInvalidPieceUid () {
        JSONObject json = new JSONObject();
        json.put("piece_uid","4bb5f40d-78da-4678-a6aa-80873b673");
        json.put("sp_id", conf.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("piece_group_code", conf.getPieceGroupCode());
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '4bb5f40d-78da-4678-a6aa-80873b673' provided for parameter 'piece_uid'.");
    }

    @Test (dependsOnMethods={"UpdateInvalidPieceId"}, alwaysRun=true)
    public void UpdateSMS ()  {
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFourthPiece());
        json.put("sp_id", conf.getSpId());
        json.put("sms","+923347347775");
        json.put("alloc_type","1");
        json.put("shipment_type","1");
        json.put("piece_group_code", conf.getPieceGroupCode());
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        String b = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(b);
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        Assert.assertEquals( code,93);
        JSONObject obj = new JSONObject(b);
        String msg = obj.getString("message");
        System.out.println(">>>>>>>>" + msg);
        JSONArray arr = obj.getJSONArray("changed_keys");
        for (int i = 0; i < arr.length(); i++) {
            System.out.println(arr);
            //Assert.assertEquals(arr,"[\"sms\"]");
        }

    }

    @Test (dependsOnMethods={"UpdateInvalidPieceId"}, alwaysRun=true)
    public void UpdateWrongSMS ()  {
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFourthPiece());
        json.put("sp_id", conf.getSpId());
        json.put("sms","+923");
        json.put("alloc_type","1");
        json.put("shipment_type","1");
        json.put("piece_group_code", conf.getPieceGroupCode());
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        String b = response.getBody().asString();
        System.out.println(b);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '+923' provided for parameter 'sms'.");
    }

    @Test (dependsOnMethods={"UpdateInvalidPieceId"}, alwaysRun=true)
    public void UpdatePieceIdNotFound ()  {
        JSONObject json = new JSONObject();
        json.put("piece_id","ZHtst131");
        json.put("sp_id", conf.getSpId());
        json.put("sms",conf.getPhNumber());
        json.put("alloc_type","1");
        json.put("shipment_type","1");
        json.put("piece_group_code", conf.getPieceGroupCode());
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        String b = response.getBody().asString();
        System.out.println(b);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,64);
        Assert.assertEquals(msg, "Invalid piece id or piece uid.");

    }

    @Test (dependsOnMethods={"UpdateInvalidPieceId"}, alwaysRun=true)
    public void UpdateFromHardToSoft ()  {
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFourthPiece());
        json.put("sp_id", conf.getSpId());
        json.put("sms",conf.getPhNumber());
        json.put("alloc_type","2");
        json.put("shipment_type","1");
        json.put("piece_group_code", conf.getPieceGroupCode());
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments")
                        .then()
                        .extract();
        String b = response.getBody().asString();
        System.out.println(b);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/shipment/SoftAllocation.json"));
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,92);
        Assert.assertEquals(msg, "Shipment is soft reserved.");

    }

    @Test (dependsOnMethods={"UpdateFromHardToSoft"}, alwaysRun=true)
    public void UpdateFromSoftToHard ()  {
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFourthPiece());
        json.put("sp_id", conf.getSpId());
        json.put("sms",conf.getPhNumber());
        json.put("alloc_type","1");
        json.put("shipment_type","1");
        json.put("piece_group_code", conf.getPieceGroupCode());
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments")
                        .then()
                        .extract();
        String body = response.getBody().asString();
        System.out.println(body);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/shipment/HardAllocation.json"));
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,93);
        Assert.assertEquals(msg, "Shipment is hard reserved.");

    }

    @Test (dependsOnMethods={"UpdateFromSoftToHard"}, alwaysRun=true)
    public void UpdatePieceIdBelongsToSomeOneElse ()  {
        JSONObject json = new JSONObject();
        json.put("piece_id","SBXPP1001");
        json.put("alloc_type","2");
        json.put("shipment_type","1");
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        String b = response.getBody().asString();
        System.out.println(b);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,64);
        Assert.assertEquals(msg, "Invalid piece id or piece uid.");
    }

    @Test (dependsOnMethods={"UpdatePieceIdBelongsToSomeOneElse"}, alwaysRun=true)
    public void UpdateAltServicePoint ()  {
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFourthPiece());
        json.put("alt_sp_id",conf.getAltSpID());
        json.put("alloc_type","1");
        json.put("shipment_type","1");
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        String b = response.getBody().asString();
        System.out.println(b);
        //Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
//        Assert.assertEquals( code,64);
//        Assert.assertEquals(msg, "Invalid piece id or piece uid.");
    }

    @Test (dependsOnMethods={"UpdatePieceIdBelongsToSomeOneElse"}, alwaysRun=true)
    public void UpdatePieceUIDBelongsToSomeOneElse ()  {
        JSONObject json = new JSONObject();
        json.put("piece_uid","4bb5f40d-78da-4678-a6aa-80873b673981");
        json.put("alloc_type","2");
        json.put("shipment_type","1");
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        String b = response.getBody().asString();
        System.out.println(b);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,64);
        Assert.assertEquals(msg, "Invalid piece id or piece uid.");
    }

    @Test (dependsOnMethods={"UpdateFromSoftToHard"}, alwaysRun=true)
    public void UpdateAShipment () {
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFirstPiece());
        json.put("sp_id", conf.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("shipment_type", "1");
        json.put("width", "15");
        json.put("height","15");
        json.put("depth", "15");
        json.put("piece_group_code", conf.getPieceGroupCode());
        baseURI = conf.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments")
                        .then().extract();
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        response.then().body(matchesJsonSchemaInClasspath("schema/shipment/UpdateShipment.json"));
    }

}
