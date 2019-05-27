package api.test;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import org.testng.Assert;
import web.config.ConfigFileReader;
import java.sql.*;
import java.util.Random;


public class ShipmentQueueAndNearestPoint {

    ConfigFileReader configFileReader;
    ConfigFileReader conf = new ConfigFileReader();
    static Connection con = null;
    private static Statement stmt;
    Connection conn = null;
    Session session= null;

    //@BeforeTest
    public void setUp() {

        int lport=5656;
        String rhost = conf.getDbUrl();
        String host = conf.getSshHostName();
        int rport=3306;
        String user=conf.getSshUser();
        String password="";
        String dbuserName = conf.getDbUser();
        String dbpassword = conf.getDbPassword();
        String url = "jdbc:mysql://localhost:"+lport+"/" + conf.getDbName();
        String driverName="com.mysql.cj.jdbc.Driver";
        String SshKeyFilepath = "D:\\infinity_db_key_pair.pem";
//        Connection conn = null;
//        Session session= null;
        try{
            //Set StrictHostKeyChecking property to no to avoid UnknownHostKey issue
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            session=jsch.getSession(user, host, 22);
            jsch.addIdentity(SshKeyFilepath);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            System.out.println(session);
            System.out.println("Connected");
            int assinged_port=session.setPortForwardingL(lport, rhost, rport);
            System.out.println("localhost:"+assinged_port+" -> "+rhost+":"+rport);
            System.out.println("Port Forwarded");

            //mysql database connectivity
            Class.forName(driverName).newInstance();
            conn = DriverManager.getConnection (url, dbuserName, dbpassword);
            stmt = conn.createStatement();
            System.out.println();
            System.out.println ("Database connection established");
            System.out.println("DONE");

        }catch(Exception e){
            e.printStackTrace();
        }

    }




// -----------------------------------------------------------------------------------
// ------------------ HARD ALLOCATION - REQUESTED SP IS AVAILABLE --------------------
// -----------------------------------------------------------------------------------

    @Test
    public void ConvertFromHardToHardWithDifferentSpId ()  {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFourthPiece());
        json.put("sp_id", configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("shipment_type","1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments")
                        .then().extract();
        System.out.println("Test Case convertFromHardToHardWithDifferentSpId >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/shipment/Shipment.json"));
        int code = response.path("code");
        Assert.assertEquals( code,94);
        String msg = response.path("message");
        Assert.assertTrue(msg.contains("Shipment is"));
    }

    @Test (dependsOnMethods={"ConvertFromHardToHardWithDifferentSpId"}, alwaysRun=true)
    public void UpdateSoftToHard ()  {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.get1stPiece());
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("shipment_type","1");
        json.put("width", "10");
        json.put("height","10");
        json.put("depth", "10");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments")
                        .then().extract();
        System.out.println("Test Case UpdateSoftToHard >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/shipment/Shipment.json"));
        int code = response.path("code");
        Assert.assertEquals( code,94);
        String msg = response.path("message");
        Assert.assertTrue(msg.contains("Shipment is"));
    }


// -----------------------------------------------------------------------------------
// ------------------ HARD ALLOCATION - NEAREST SP IS AVAILABLE --------------------
// -----------------------------------------------------------------------------------

    @Test(dependsOnMethods={"UpdateSoftToHard"}, alwaysRun=true)
    public void HardAllocationToSameSpId ()  {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.getFifthPiece());
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("shipment_type", "1");
        json.put("width", "1");
        json.put("height","1");
        json.put("depth", "1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case HardAllocationToSameSpId >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
    }

    @Test (dependsOnMethods={"HardAllocationToSameSpId"}, alwaysRun=true)
    public void HardAllocationFindNearestSP ()  {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.getSixthPiece());
        json.put("sp_id", conf.getAnotherSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("shipment_type", "1");
        json.put("width", "1");
        json.put("height","1");
        json.put("depth", "1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case HardAllocationFindNearestSP >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
    }


// -----------------------------------------------------------------------------------
// ----------------------------------- Reject ----------------------------------------
// -----------------------------------------------------------------------------------

    @Test (dependsOnMethods={"HardAllocationFindNearestSP"}, alwaysRun=true)
    public void BehaviourRejectTheShipmentCreation () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getEightPiece());
        json.put("sp_id",conf.getAnotherSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("shipment_type", "1");
        json.put("width", "1");
        json.put("height","1");
        json.put("depth", "1");
        //json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKeyForReject())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case HardAllocationAlreadyExisting >>>>>");
        System.out.println(response.statusCode());
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,88);
        Assert.assertEquals(msg, "Shipment rejeceted, no compartment available.");
    }


// -----------------------------------------------------------------------------------
// ----------------------------------- Queue / Events --------------------------------
// -----------------------------------------------------------------------------------




    @Test (dependsOnMethods={"BehaviourRejectTheShipmentCreation"}, alwaysRun=true)
    public void VerifyEventDetailForShipment () {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", configFileReader.getSecondPiece())
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .get("/events");
        System.out.println("Test Case VerifyEventDetailForShipment >>>>>");
        Assert.assertEquals(response.statusCode(), 200);
        String body = response.getBody().asString();
        System.out.println(body);
    }

    @Test (dependsOnMethods={"VerifyEventDetailForShipment"}, alwaysRun=true)
    public void ValidateEventDetailForShipmentSchema () {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        given()
                .queryParam("piece_id", configFileReader.getSecondPiece())
                .header("Content-Type","application/json")
                .header("X-Api-Key",configFileReader.getApiKey())
                .get("/events")
                .then().body(matchesJsonSchemaInClasspath("schema/shipment/EventDetail.json"));
        System.out.println("Test Case ValidateEventDetailForShipmentSchema >>>>>");
    }

    @Test (dependsOnMethods={"ValidateEventDetailForShipmentSchema"}, alwaysRun=true)
    public void CreateShipmentsInBulkToCheckParcelInQueue () {
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            configFileReader = new ConfigFileReader();
            int r = rand.nextInt(9999);
            JSONObject json = new JSONObject();
            json.put("piece_id", configFileReader.getFirstPiece()+r);
            json.put("sp_id", configFileReader.getAnotherSpID());
            json.put("sms", "+923347347789");
            json.put("alloc_type", "1");
            json.put("shipment_type", "1");
            json.put("width", "1");
            json.put("height", "1");
            json.put("depth", "1");
            json.put("piece_group_code", configFileReader.getPieceGroupCode());
            baseURI = configFileReader.getShipmentApiUrl();
            Response response = (Response)
                    given()
                            .header("Content-Type", "application/json")
                            .header("X-Api-Key", configFileReader.getApiKeyForQue())
                            .and()
                            .body(json.toString())
                            .post("/shipments");
            System.out.println("Test Case CreateShipmentsInBulkToCheckParcelInQueue >>>>>");
            System.out.println(response.statusCode());
            String body = response.getBody().asString();
            System.out.println(body);
        }
    }

    @Test (dependsOnMethods={"CreateShipmentsInBulkToCheckParcelInQueue"}, alwaysRun=true)
    public void BehaviourQueueTheParcel () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getNinthPiece());
        json.put("sp_id",conf.getAnotherSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("shipment_type", "1");
        json.put("width", "1");
        json.put("height","1");
        json.put("depth", "1");
        //json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKeyForQue())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case BehaviourQueueTheParcel >>>>>");
        System.out.println(response.statusCode());
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,94);
        Assert.assertEquals(msg, "Shipment is added to queue for later hard reservation.");
    }

    @Test (dependsOnMethods={"BehaviourQueueTheParcel"}, alwaysRun=true)
    public void QueuedShipmentForServicePoint () {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .queryParam("sp_id", configFileReader.getAnotherSpID())
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKeyForQue())
                        .get("/queued")
                        .then().extract();
        System.out.println("Test Case QueuedShipmentForServicePoint >>>>>");
        Assert.assertEquals(response.statusCode(), 200);
        String body = response.getBody().asString();
        System.out.println(body);
        response.then().body(matchesJsonSchemaInClasspath("schema/shipment/QueuedShipments.json"));
    }

    @Test (dependsOnMethods={"QueuedShipmentForServicePoint"}, alwaysRun=true)
    public void QueuedShipmentIfNoShipmentIsQueued () {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .queryParam("sp_id", configFileReader.getSpId())
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .get("/queued")
                        .then().extract();
        System.out.println("Test Case QueuedShipmentForServicePoint >>>>>");
        Assert.assertEquals(response.statusCode(), 200);
        String body = response.getBody().asString();
        System.out.println(body);
        //response.then().body(matchesJsonSchemaInClasspath("schema/shipment/QueuedShipments.json"));
    }



    //@AfterTest
    public void tearDown() throws Exception {
        if(conn != null && !conn.isClosed()){
            System.out.println("Closing Database Connection");
            conn.close();
        }
        if(session !=null && session.isConnected()){
            System.out.println("Closing SSH Connection");
            session.disconnect();
        }
    }

}


