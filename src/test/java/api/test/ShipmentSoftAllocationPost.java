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


public class ShipmentSoftAllocationPost {
    ConfigFileReader configFileReader;
    ConfigFileReader conf = new ConfigFileReader();
    private static Statement stmt;
    Connection conn = null;
    Session session= null;

    @BeforeTest
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
        String SshKeyFilepath = "src/test/resources/infinity_db_key_pair.pem";
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




// ------------------------------------------------------------------------------------------------
// --------------------------------------   POST  ------------------------------------------------
// ------------------------------------------------------------------------------------------------


// ------------------------------------------------------------------------------------------------
// -----------------------------    SOFT ALLOCATION SECTION   ------------------------------------
// ------------------------------------------------------------------------------------------------


    @Test //(dependsOnMethods={"ResetShipmentForServicePoint10075"}, alwaysRun=true)
    public void SoftAllocationWithSMS () {
        ConfigFileReader conf = new ConfigFileReader();
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.get1stPiece());
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms",conf.getPhNumber());
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .log()
                        .all()
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case MissingShipmentAndPieceID >>>>>");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,92);
        Assert.assertEquals(msg, "Shipment is soft reserved.");
        System.out.println(response.statusCode());

        try{
            String query = "SELECT * FROM piece where piece_barcode = " +"'"+conf.get1stPiece() + "';" ;
            ResultSet res = stmt.executeQuery(query);
            while (res.next())
            {
                System.out.print("\t" + res.getString(1));
                String barcode = res.getString("piece_barcode");
                String alloc_type = res.getString("alloc_type");
                String piece_type = res.getString("piece_type");
                String sp_id = res.getString("service_point_id");
                String compartment_size = res.getString("compartment_size");
                String width = res.getString("width");
                String height = res.getString("height");
                String depth = res.getString("depth");
                Assert.assertEquals(conf.get1stPiece(), barcode);
                Assert.assertEquals(alloc_type, "2");
                Assert.assertEquals(piece_type, "1");
                Assert.assertEquals(sp_id, conf.getSpId());
                Assert.assertEquals(compartment_size, null);
                Assert.assertEquals(width, null);
                Assert.assertEquals(height, null);
                Assert.assertEquals(depth, null);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }



    }

    @Test //(dependsOnMethods={"SoftAllocationWithSMS"}, alwaysRun=true)
    public void SoftAllocationWithoutSMS () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.get2ndPiece());
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id",configFileReader.getAltSpID());
        //json.put("sms",conf.getPhNumber());
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithoutSMS >>>>>");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,23);
        Assert.assertEquals(msg, "Parameter 'sms' missing.");
        System.out.println(response.statusCode());

    }

    @Test (dependsOnMethods={"SoftAllocationWithoutSMS"}, alwaysRun=true)
    public void SoftAllocationWithAltSpId () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.get2ndPiece());
        json.put("alt_sp_id", configFileReader.getAltSpID());
        json.put("sms",conf.getPhNumber());
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithAltSpId >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,92);
        Assert.assertEquals(msg, "Shipment is soft reserved.");
        System.out.println(response.statusCode());
//        String a = response.then().log().all().toString();
//        System.out.println(a);

    }

    @Test (dependsOnMethods={"SoftAllocationWithAltSpId"}, alwaysRun=true)
    public void SoftAllocationWithoutSizes () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get3rdPiece());
        json.put("shipment_id" , "90101000");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id",configFileReader.getAltSpID());
        json.put("sms",conf.getPhNumber());
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case MissingShipmentAndPieceID >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,92);
        Assert.assertEquals(msg, "Shipment is soft reserved.");
        System.out.println(response.statusCode());

    }

    @Test (dependsOnMethods={"SoftAllocationWithoutSizes"}, alwaysRun=true)
    public void SoftAllocationWithSizes () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get4thPiece());
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id", conf.getAltSpID());
        json.put("sms",conf.getPhNumber());
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("width", "30");
        json.put("height","20");
        json.put("depth", "10");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case MissingShipmentAndPieceID >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,92);
        Assert.assertEquals(msg, "Shipment is soft reserved.");
        System.out.println(response.statusCode());
        try{
            String query = "SELECT * FROM  piece WHERE piece_barcode = " +"'"+conf.get4thPiece() + "';" ;
            ResultSet res = stmt.executeQuery(query);
            while (res.next())
            {
                //System.out.print("\t" + res.getString(2));
                String barcode = res.getString("piece_barcode");
                String alloc_type = res.getString("alloc_type");
                String piece_type = res.getString("piece_type");
                String sp_id = res.getString("service_point_id");
                String compartment_size = res.getString("compartment_size");
                String width = res.getString("width");
                String height = res.getString("height");
                String depth = res.getString("depth");
                Assert.assertEquals(barcode, conf.get4thPiece());
                Assert.assertEquals(alloc_type, "2");
                //Assert.assertEquals(piece_type, "1");
                Assert.assertEquals(sp_id, conf.getSpId());
                //Assert.assertEquals(compartment_size, "1");
                Assert.assertEquals(width, null);
                Assert.assertEquals(height, null);
                Assert.assertEquals(depth, null);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    @Test (dependsOnMethods={"SoftAllocationWithSizes"}, alwaysRun=true)
    public void SoftAllocationWithMissingAllParameters () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case MissingAllParameters >>>>>");
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,23);
        Assert.assertEquals(msg, "Parameter 'piece_id' missing.");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);

    }

    @Test (dependsOnMethods={"SoftAllocationWithMissingAllParameters"}, alwaysRun=true)
    public void SoftAllocationWithAllParametersEmpty () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id","");
        json.put("shipment_id" , "");
        json.put("sp_id", "");
        json.put("alt_sp_id", "");
        json.put("sms","");
        json.put("alloc_type","");
        json.put("shipment_type", "");
        json.put("width", "");
        json.put("height","");
        json.put("depth", "");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithAllParametersEmpty >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'piece_id'.");
    }

    @Test (dependsOnMethods={"SoftAllocationWithAllParametersEmpty"}, alwaysRun=true)
    public void SoftAllocationWithMissingShipmentAndPieceID () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id", configFileReader.getAltSpID());
        json.put("sms",conf.getPhNumber());
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMissingShipmentAndPieceID >>>>>");
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,23);
        Assert.assertEquals(msg, "Parameter 'piece_id' missing.");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);

    }

    @Test (dependsOnMethods={"SoftAllocationWithMissingShipmentAndPieceID"}, alwaysRun=true)
    public void SoftAllocationWithEmptyShipmentAndPieceID () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id","");
        json.put("shipment_id","");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id", configFileReader.getAltSpID());
        json.put("sms",conf.getPhNumber());
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithEmptyShipmentAndPieceID >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'piece_id'.");


    }

    @Test (dependsOnMethods={"SoftAllocationWithEmptyShipmentAndPieceID"}, alwaysRun=true)
    public void SoftAllocationWithMissingPieceID () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id","");
        json.put("shipment_id","19001004");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id", configFileReader.getAltSpID());
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("sms","+923347347789");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMissingPieceID >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'piece_id'.");
    }

    @Test (dependsOnMethods={"SoftAllocationWithMissingPieceID"}, alwaysRun=true)
    public void SoftAllocationWithEmptyShipmentID () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get5thPiece());
        json.put("shipment_id","");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id", configFileReader.getAltSpID());
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("sms","+923347347789");
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMissingShipmentID >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,92);
        Assert.assertEquals(msg, "Shipment is soft reserved.");
        System.out.println(response.statusCode());

    }

    @Test (dependsOnMethods={"SoftAllocationWithEmptyShipmentID"}, alwaysRun=true)
    public void SoftAllocationWithMinLengthShipmentID () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get4thPiece());
        json.put("shipment_id","1");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id", configFileReader.getAltSpID());
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("sms","+923347347789");
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMinLengthShipmentID >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '1' provided for parameter 'shipment_id'.");
        System.out.println(response.statusCode());
    }

    @Test (dependsOnMethods={"SoftAllocationWithMinLengthShipmentID"}, alwaysRun=true)
    public void SoftAllocationWithMaxLengthShipmentID () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get4thPiece());
        json.put("shipment_id","948503458034958305830459830453480593840423424234234234");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id", configFileReader.getAltSpID());
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("sms","+923347347789");
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMaxLengthShipmentID >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '948503458034958305830459830453480593840423424234234234' provided for parameter 'shipment_id'.");
        System.out.println(response.statusCode());

    }

    @Test (dependsOnMethods={"SoftAllocationWithMaxLengthShipmentID"}, alwaysRun=true)
    public void SoftAllocationWithMissingServicePoint () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id","ZHtst107");
        json.put("sp_id", "");
        json.put("shipment_id", "190010041");
        //json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMissingServicePoint >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,42);
        Assert.assertEquals(msg, "Invalid service point.");
        System.out.println(response.statusCode());
    }

    @Test (dependsOnMethods={"SoftAllocationWithMissingServicePoint"}, alwaysRun=true)
    public void SoftAllocationWithInvalidSpId () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get5thPiece());
        json.put("sp_id", "10bc0");
        json.put("shipment_id", "190010041");
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithInvalidSpId >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        //Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'piece_id'.");
        System.out.println(response.statusCode());
    }

    @Test (dependsOnMethods={"SoftAllocationWithMissingServicePoint"}, alwaysRun=true)
    public void SoftAllocationWithMinLengthPieceId () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id","1");
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
//        json.put("width", "1");
//        json.put("height","1");
//        json.put("depth", "1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMinLengthPieceId >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        //Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'piece_id'.");
        System.out.println(response.statusCode());
    }

    @Test (dependsOnMethods={"SoftAllocationWithMinLengthPieceId"}, alwaysRun=true)
    public void SoftAllocationWithMaxLengthPieceId () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id","ZHtst636737373700000000000000000000000000000000000000000000000000000000000");
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMaxLengthPieceId >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value 'ZHtst636737373700000000000000000000000000000000000000000000000000000000000' provided for parameter 'piece_id'.");
        System.out.println(response.statusCode());
    }

    @Test (dependsOnMethods={"SoftAllocationWithMaxLengthPieceId"}, alwaysRun=true)
    public void SoftAllocationWithInvalidPieceId () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id","ZHtst");
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
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
        System.out.println("Test Case SoftAllocationWithInvalidPieceId >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value 'ZHtst' provided for parameter 'piece_id'.");
        System.out.println(response.statusCode());
    }

    @Test //(dependsOnMethods={"SoftAllocationWithInvalidPieceId"}, alwaysRun=true)
    public void SoftAllocationWithSpIdAndAltSpId () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get6thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithSpIdAndAltSpId >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
//        Assert.assertEquals(response.statusCode(), 200);
//        int code = response.path("code");
//        String msg = response.path("message");
//        Assert.assertEquals( code,92);
//        Assert.assertEquals(msg, "Shipment is soft reserved.");
//        System.out.println(response.statusCode());
    }

    @Test (dependsOnMethods={"SoftAllocationWithSpIdAndAltSpId"}, alwaysRun=true)
    public void SoftAllocationWithMissingSMS () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get6thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
//        json.put("width", "30");
//        json.put("height","20");
//        json.put("depth", "10");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMissingSMS >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'sms'.");
        System.out.println(response.statusCode());


    }

    @Test (dependsOnMethods={"SoftAllocationWithMaxLengthPieceId"}, alwaysRun=true)
    public void SoftAllocationWithInvalidSMS () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get6thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","ksks");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithInvalidSMS >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value 'ksks' provided for parameter 'sms'.");
        System.out.println(response.statusCode());

    }

    @Test (dependsOnMethods={"SoftAllocationWithInvalidSMS"}, alwaysRun=true)
    public void SoftAllocationWithMinLengthSMS () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get6thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+92");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMinLengthSMS >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '+92' provided for parameter 'sms'.");
        System.out.println(response.statusCode());

    }

    @Test (dependsOnMethods={"SoftAllocationWithMinLengthSMS"}, alwaysRun=true)
    public void SoftAllocationWithAllocTypeMissing () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get6thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","");
        json.put("shipment_type", "1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithAllocTypeMissing >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'alloc_type'.");
        System.out.println(response.statusCode());

    }

    @Test (dependsOnMethods={"SoftAllocationWithAllocTypeMissing"}, alwaysRun=true)
    public void SoftAllocationWithWidthMissing () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get6thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("width", "");
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
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithWidthMissing >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'width'.");
    }

    @Test (dependsOnMethods={"SoftAllocationWithAllocTypeMissing"}, alwaysRun=true)
    public void SoftAllocationWithInvalidWidth () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get6thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("width", "10cm");
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
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithInvalidWidth >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '10cm' provided for parameter 'width'.");
    }

    @Test (dependsOnMethods={"SoftAllocationWithAllocTypeMissing"}, alwaysRun=true)
    public void SoftAllocationWithMinWidth () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get7thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("width", "0");
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
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMinWidth >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'width'.");
    }

    @Test (dependsOnMethods={"SoftAllocationWithAllocTypeMissing"}, alwaysRun=true)
    public void SoftAllocationWithMaxWidth () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get7thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("width", "1000");
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
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMinWidth >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,43);
        Assert.assertEquals(msg, "Invalid piece size value.");
    }

    @Test (dependsOnMethods={"SoftAllocationWithAllocTypeMissing"}, alwaysRun=true)
    public void SoftAllocationWithHeightMissing () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get7thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("width", "10");
        json.put("height","");
        json.put("depth", "10");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithHeightMissing >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'height'.");
    }

    @Test (dependsOnMethods={"SoftAllocationWithAllocTypeMissing"}, alwaysRun=true)
    public void SoftAllocationWithInvalidHeight () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get7thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("width", "10");
        json.put("height","10cm");
        json.put("depth", "10");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithInvalidHeight >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '10cm' provided for parameter 'height'.");
    }

    @Test (dependsOnMethods={"SoftAllocationWithAllocTypeMissing"}, alwaysRun=true)
    public void SoftAllocationWithMinHeight () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get6thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("width", "10");
        json.put("height","0");
        json.put("depth", "10");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMinHeight >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'height'.");
    }

    @Test (dependsOnMethods={"SoftAllocationWithAllocTypeMissing"}, alwaysRun=true)
    public void SoftAllocationWithMaxHeight () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get6thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("width", "10");
        json.put("height","10000");
        json.put("depth", "10");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMaxHeight >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '10000' provided for parameter 'height'.");
    }

    @Test (dependsOnMethods={"SoftAllocationWithAllocTypeMissing"}, alwaysRun=true)
    public void SoftAllocationWithDepthMissing () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get6thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("width", "10");
        json.put("height","10");
        json.put("depth", "");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithDepthMissing >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        //Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'depth'.");
    }

    @Test (dependsOnMethods={"SoftAllocationWithAllocTypeMissing"}, alwaysRun=true)
    public void SoftAllocationWithInvalidDepth () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get6thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("width", "10");
        json.put("height","10");
        json.put("depth", "10cm");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithInvalidDepth >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '10cm' provided for parameter 'depth'.");
    }

    @Test (dependsOnMethods={"SoftAllocationWithAllocTypeMissing"}, alwaysRun=true)
    public void SoftAllocationWithMinDepth () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get7thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("width", "10");
        json.put("height","10");
        json.put("depth", "0");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMinDepth >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'depth'.");
    }

    @Test (dependsOnMethods={"SoftAllocationWithAllocTypeMissing"}, alwaysRun=true)
    public void SoftAllocationWithMaxDepth () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get7thPiece());
        json.put("shipment_id", "190010041");
        json.put("sp_id", configFileReader.getSpId());
        json.put("alt_sp_id" , configFileReader.getAltSpID());;
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
        json.put("shipment_type", "1");
        json.put("width", "10");
        json.put("height","10");
        json.put("depth", "1000");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case SoftAllocationWithMaxDepth >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,43);
        Assert.assertEquals(msg, "Invalid piece size value.");
    }

    @AfterTest
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




