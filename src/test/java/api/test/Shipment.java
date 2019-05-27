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


public class Shipment {
    ConfigFileReader configFileReader;
    ConfigFileReader conf = new ConfigFileReader();
    static Connection con = null;
    private static Statement stmt;

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
        String SshKeyFilepath = "D:\\infinity_db_key_pair.pem";
        Connection conn = null;
        Session session= null;
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


    @Test
    public void ResetShipmentForServicePoint10082 () {
        baseURI = "https://infinity.swipbox.com/prepilotapi/factory_app_api/v1";
        Response response =
                given()
                        .queryParam("sp_id", "10082")
                        .get("/clear_pieces");
        System.out.println("Test Case ResetShipmentForServicePoint10082 >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
    }

    @Test (dependsOnMethods={"ResetShipmentForServicePoint10082"}, alwaysRun=true)
    public void ResetShipmentForServicePoint10084 () {
        baseURI = "https://infinity.swipbox.com/prepilotapi/factory_app_api/v1";
        Response response =
                given()
                        .queryParam("sp_id", "10084")
                        .get("/clear_pieces");
        System.out.println("Test Case ResetShipmentForServicePoint10084 >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
    }

    @Test (dependsOnMethods={"ResetShipmentForServicePoint10084"}, alwaysRun=true)
    public void ResetShipmentForServicePoint10076 () {
        baseURI = "https://infinity.swipbox.com/prepilotapi/factory_app_api/v1";
        Response response =
                given()
                        .queryParam("sp_id", "10076")
                        .get("/clear_pieces");
        System.out.println("Test Case ResetShipmentForServicePoint10076 >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
    }

    @Test (dependsOnMethods={"ResetShipmentForServicePoint10076"}, alwaysRun=true)
    public void ResetShipmentForServicePoint10075 () {
        baseURI = "https://infinity.swipbox.com/prepilotapi/factory_app_api/v1";
        Response response =
                given()
                        .queryParam("sp_id", "10075")
                        .get("/clear_pieces");
        System.out.println("Test Case ResetShipmentForServicePoint10075 >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
    }


// ------------------------------------------------------------------------------------------------
// --------------------------------------   POST  ------------------------------------------------
// ------------------------------------------------------------------------------------------------


// ------------------------------------------------------------------------------------------------
// -----------------------------    SOFT ALLOCATION SECTION   ------------------------------------
// ------------------------------------------------------------------------------------------------


    @Test (dependsOnMethods={"ResetShipmentForServicePoint10075"}, alwaysRun=true)
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

    @Test (dependsOnMethods={"SoftAllocationWithSMS"}, alwaysRun=true)
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
        System.out.println("Test Case MissingShipmentAndPieceID >>>>>");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,23);
        Assert.assertEquals(msg, "Parameter 'piece_id' missing.");
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
                Assert.assertEquals(barcode, conf.get2ndPiece());
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

    @Test (dependsOnMethods={"SoftAllocationWithInvalidPieceId"}, alwaysRun=true)
    public void SoftAllocationWithSpIdAndAltSpId () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get5thPiece());
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
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,92);
        Assert.assertEquals(msg, "Shipment is soft reserved.");
        System.out.println(response.statusCode());
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
        json.put("piece_id",configFileReader.get6thPiece());
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
        json.put("piece_id",configFileReader.get6thPiece());
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
        json.put("piece_id",configFileReader.get6thPiece());
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
        json.put("piece_id",configFileReader.get6thPiece());
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
        json.put("piece_id",configFileReader.get6thPiece());
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
        json.put("piece_id",configFileReader.get6thPiece());
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



// --------------------------------------------------------------------------------------------------------
// -----------------------------    HARD ALLOCATION SECTION   ---------------------------------------------
// --------------------------------------------------------------------------------------------------------

    //@Test (dependsOnMethods={"ResetShipmentForServicePoint10075"}, alwaysRun=true)
    @Test (dependsOnMethods={"SoftAllocationWithMaxDepth"}, alwaysRun=true)
    public void HardAllocation () throws SQLException {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        //json.put("piece_id",configFileReader.getFirstPiece());
        json.put("piece_id","ZHtst132");
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms",conf.getPhNumber());
        json.put("alloc_type","1");
        json.put("shipment_type", "1");
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
                        .post("/shipments");
        System.out.println("Test Case HardAllocation >>>>>");
        Assert.assertEquals(response.statusCode(), 200);
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);

        String query = "SELECT * FROM  piece WHERE piece_barcode = " + "'" + conf.getFirstPiece() + "';";
        ResultSet res = stmt.executeQuery(query);
        String depth = null;
        String barcode = null;
        String alloc_type = null;
        String sp_id = null;
        String compartment_size = null;
        String width = null;
        String height = null;
        String piece_id = null;
        String hardware_unit_id = null;
        String compartment_id = null;
        while (res.next())
        {
            piece_id = res.getString("piece_id");
            barcode = res.getString("piece_barcode");
            alloc_type = res.getString("alloc_type");
            String piece_type = res.getString("piece_type");
            sp_id = res.getString("service_point_id");
            compartment_size = res.getString("compartment_size");
            width = res.getString("width");
            height = res.getString("height");
            depth = res.getString("depth");
            hardware_unit_id = res.getString("hardware_unit_id");
            compartment_id = res.getString("compartment_id");
            Assert.assertEquals(piece_type, "1");
        }
        System.out.println(piece_id);
        Assert.assertEquals(barcode, conf.getFirstPiece());
        Assert.assertEquals(alloc_type, "1");
        Assert.assertEquals(sp_id, conf.getSpId());
        Assert.assertEquals(compartment_size, "1");
        Assert.assertEquals(width, "10");
        Assert.assertEquals(height, "10");
        Assert.assertEquals(depth, "10");
        //String query2 = "SELECT cs.*  FROM compartment_status cs INNER JOIN service_point_hardware_unit u ON cs.hardware_id = u.hardware_id WHERE u.service_point_hardware_unit_id =" + "'" + shipment.getHardwareUnitId(conf.getFirstPiece()) + "'" + " AND cs.compartment_id =" + "'" + shipment.getCompartmentId(conf.getFirstPiece()) + "'";
        String query2 = "SELECT cs.*  FROM compartment_status cs INNER JOIN service_point_hardware_unit u ON cs.hardware_id = u.hardware_id WHERE u.service_point_hardware_unit_id =" + hardware_unit_id + " AND cs.compartment_id =" + compartment_id;

        ResultSet res2 = stmt.executeQuery(query2);
        while (res2.next())
        {
            System.out.print("\t" + res2.getString(1));
            System.out.print("\t" + res2.getString(2));
            System.out.print("\t" + res2.getString(3));
            System.out.print("\t" + res2.getString(4));
        }

        //String query3 = "SELECT * FROM hardware_command WHERE piece_id =" + "'" + shipment.getPieceID(conf.getFirstPiece()) + "' AND command_status = 1 LIMIT 1";
        String query3 = "SELECT * FROM hardware_command WHERE piece_id ="  + piece_id + " AND command_status = 1 LIMIT 1";
        ResultSet res3 = stmt.executeQuery(query3);
        while (res3.next()) {
            String id = res3.getString("hardware_command_id");
            System.out.println(id);
        }

    }

    @Test (dependsOnMethods={"HardAllocation"}, alwaysRun=true)
    public void ValidateHardAllocationSchema () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.getSecondPiece());
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("shipment_type", "1");
        json.put("width", "1");
        json.put("height","1");
        json.put("depth", "1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        given()
                .header("Content-Type","application/json")
                .header("X-Api-Key",configFileReader.getApiKey())
                .and()
                .body(json.toString())
                .post("/shipments")
                .then().body(matchesJsonSchemaInClasspath("schema/shipment/CreateShipment.json"));
        System.out.println("Test Case ValidateHardAllocationSchema >>>>>");
    }

    @Test (dependsOnMethods={"ValidateHardAllocationSchema"}, alwaysRun=true)
    public void HardAllocationAlreadyExisting () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.getFirstPiece());
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
        System.out.println("Test Case HardAllocationAlreadyExisting >>>>>");
        System.out.println(response.statusCode());
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,39);
        Assert.assertEquals(msg, "Piece ID already exists.");
    }

    @Test (dependsOnMethods={"HardAllocationAlreadyExisting"}, alwaysRun=true)
    public void HardAllocationWithAltSpId () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getThirdPiece());
        json.put("sp_id", configFileReader.getAltSpID());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("shipment_type", "1");
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
                        .post("/shipments");
        System.out.println("Test Case HardAllocationWithAltSpId >>>>>");
        System.out.println(response.statusCode());
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals(response.statusCode(), 200);
        //Assert.assertEquals(msg, "Piece ID already exists.");
    }

    @Test (dependsOnMethods={"HardAllocationAlreadyExisting"}, alwaysRun=true)
    public void HardAllocationWithIncorrectSpId () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.getThirdPiece());
        json.put("sp_id","775G");
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
        System.out.println("Test Case HardAllocationWithIncorrectSpId >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '775G' provided for parameter 'sp_id'.");

    }

    @Test (dependsOnMethods={"HardAllocationWithIncorrectSpId"}, alwaysRun=true)
    public void HardAllocationWithMissingSpId () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id","ZHtst119");
        json.put("sp_id","");
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
        System.out.println("Test Case HardAllocationWithMissingSpId >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,42);
        Assert.assertEquals(msg, "Invalid service point.");

    }

    @Test (dependsOnMethods={"HardAllocationWithMissingSpId"}, alwaysRun=true)
    public void HardAllocationWithInvalidSpId () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id","ZHtst119");
        json.put("sp_id","01");
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
        System.out.println("Test Case HardAllocationWithInvalidSpId >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,42);
        Assert.assertEquals(msg, "Invalid service point.");

    }

    @Test (dependsOnMethods={"HardAllocationWithInvalidSpId"}, alwaysRun=true)
    public void HardAllocationWithWrongAltSpId () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id","ZHtst121");
        json.put("alt_sp_id","01");
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
        System.out.println("Test Case HardAllocationWithWrongAltSpId >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,42);
        Assert.assertEquals(msg, "Invalid service point.");

    }

    @Test (dependsOnMethods={"HardAllocationWithWrongAltSpId"}, alwaysRun=true)
    public void ShipmentNotCreateIfApiKeyIsIncorrect () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.getThirdPiece());
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
                        .header("X-Api-Key","645f8c26")
                        .and()
                        .body(json.toString())
                        .post("/shipments");
        System.out.println("Test Case ShipmentNotCreateIfApiKeyIsIncorrect >>>>>");
        Assert.assertEquals(response.statusCode(), 403);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,16);
        Assert.assertEquals(msg, "This API key does not have access to the requested controller.");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
    }


// -----------------------------------------------------------------------------------
// ----------------------------------- GET -------------------------------------------
// -----------------------------------------------------------------------------------


    @Test (dependsOnMethods={"HardAllocationWithInvalidSpId"}, alwaysRun=true)
    public void HardAllocationViewShipmentDetails () {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", configFileReader.getFirstPiece())
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case HardAllocationViewShipmentDetails >>>>>");
        Assert.assertEquals(response.statusCode(), 200);
        String body = response.getBody().asString();
        System.out.println(body);
    }

    @Test (dependsOnMethods={"HardAllocationViewShipmentDetails"}, alwaysRun=true)
    public void ValidateViewShipmentDetailSchema () {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        given()
                .queryParam("piece_id", configFileReader.getFirstPiece())
                .header("Content-Type","application/json")
                .header("X-Api-Key",configFileReader.getApiKey())
                .get("/shipments").then().body(matchesJsonSchemaInClasspath("schema/shipment/ViewShipment.json"));
        System.out.println("Test Case ValidateViewShipmentDetailSchema >>>>>");
    }

    @Test (dependsOnMethods={"ValidateViewShipmentDetailSchema"}, alwaysRun=true)
    public void GetHardAllocationParamMissing () {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        //.queryParam("piece_id", configFileReader.getFirstPiece())
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationParamMissing >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,31);
        Assert.assertEquals(msg, "shipment_id / piece_id / piece_uid is missing.");
    }

    @Test (dependsOnMethods={"GetHardAllocationParamMissing"}, alwaysRun=true)
    public void GetHardAllocationByPieceIdAlphaNumeric () {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", "AYAH1234")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationByPieceIdAlphaNumeric >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,23);
        Assert.assertEquals(msg, "No record found.");
    }

    @Test (dependsOnMethods={"GetHardAllocationByPieceIdAlphaNumeric"}, alwaysRun=true)
    public void GetHardAllocationByPieceIdNumeric () {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", "1234567")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationByPieceIdNumeric >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,23);
        Assert.assertEquals(msg, "No record found.");
    }

    @Test (dependsOnMethods={"GetHardAllocationByPieceIdNumeric"}, alwaysRun=true)
    public void GetHardAllocationByPieceIdAlpha () {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", "ABCDEF")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationByPieceIdAlpha >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,23);
        Assert.assertEquals(msg, "No record found.");
    }

    @Test (dependsOnMethods={"GetHardAllocationByPieceIdAlpha"}, alwaysRun=true)
    public void GetHardAllocationStartingFromZero () {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", "0CDEF1234")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationStartingFromZero >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,23);
        Assert.assertEquals(msg, "No record found.");
    }

    @Test (dependsOnMethods={"GetHardAllocationStartingFromZero"}, alwaysRun=true)
    public void GetHardAllocationByPieceIdMinLength () {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", "0")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationByPieceIdMinLength >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,49);
        Assert.assertEquals(msg, "Invalid piece or shipment id.");
    }

    @Test (dependsOnMethods={"GetHardAllocationByPieceIdMinLength"}, alwaysRun=true)
    public void GetHardAllocationByPieceIdMaxLength () {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", "AHHAHAHHAHHAHA0000000000000000000000000000234782934792347293487293423874293489324")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationByPieceIdMaxLength >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,49);
        Assert.assertEquals(msg, "Invalid piece or shipment id.");
    }

    @Test (dependsOnMethods={"GetHardAllocationByPieceIdMaxLength"}, alwaysRun=true)
    public void GetHardAllocationByPieceIdNoneAlphaNumeric () {
        configFileReader= new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", "*&*&@*#&@*#&@*")
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationByPieceIdNoneAlphaNumeric >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,49);
        Assert.assertEquals(msg, "Invalid piece or shipment id.");
    }



// -----------------------------------------------------------------------------------
// ----------------------------------- PATCH  ----------------------------------------
// -----------------------------------------------------------------------------------


    //@Test (dependsOnMethods={"ResetShipmentForServicePoint10075"}, alwaysRun=true)
    @Test (dependsOnMethods={"GetHardAllocationByPieceIdNoneAlphaNumeric"}, alwaysRun=true)
    public void CreateShipmentForUpdate () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id", conf.getFourthPiece());
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("shipment_type", "1");
        json.put("width", "5");
        json.put("height","10");
        json.put("depth", "5");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .post("/shipments");

    }

    @Test (dependsOnMethods={"CreateShipmentForUpdate"}, alwaysRun=true)
    public void UpdateAllPramsMissing () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        System.out.println("Test Case UpdateAllPramsMissing >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,70);
        Assert.assertEquals(msg, "Request failed.");
    }

    @Test (dependsOnMethods={"UpdateAllPramsMissing"}, alwaysRun=true)
    public void UpdateAllocTypeMissing () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFourthPiece());
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        System.out.println("Test Case UpdateAllocTypeMissing >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,70);
        Assert.assertEquals(msg, "Request failed.");
    }

    @Test (dependsOnMethods={"UpdateAllPramsMissing"}, alwaysRun=true)
    public void UpdateAllocTypeInvalid () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFourthPiece());
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","klsdfjllls");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        System.out.println("Test Case UpdateAllocTypeInvalid >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,70);
        Assert.assertEquals(msg, "Request failed.");
    }

    @Test (dependsOnMethods={"UpdateAllPramsMissing"}, alwaysRun=true)
    public void UpdatePieceIdMissing () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id","");
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        System.out.println("Test Case UpdateAllocTypeInvalid >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'piece_id'.");
    }

    @Test (dependsOnMethods={"UpdatePieceIdMissing"}, alwaysRun=true)
    public void UpdatePieceUidMissing () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_uid","");
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        System.out.println("Test Case UpdateAllocTypeInvalid >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '' provided for parameter 'piece_uid'.");
    }

    @Test (dependsOnMethods={"UpdatePieceUidMissing"}, alwaysRun=true)
    public void UpdateInvalidPieceId () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id","GE-");
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        System.out.println("Test Case UpdateAllocTypeInvalid >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value 'GE-' provided for parameter 'piece_id'.");
    }

    @Test (dependsOnMethods={"UpdateInvalidPieceId"}, alwaysRun=true)
    public void UpdateInvalidPieceUid () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_uid","4bb5f40d-78da-4678-a6aa-80873b673");
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        System.out.println("Test Case UpdateAllocTypeInvalid >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals( code,22);
        Assert.assertEquals(msg, "Incorrect value '4bb5f40d-78da-4678-a6aa-80873b673' provided for parameter 'piece_uid'.");
    }

    @Test (dependsOnMethods={"UpdateInvalidPieceId"}, alwaysRun=true)
    public void UpdateSMS ()  {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFourthPiece());
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms","+923347347775");
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
                        .patch("/shipments");
        System.out.println("Test Case UpdateAllocTypeInvalid >>>>>");
        String b = response.getBody().asString();
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
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFourthPiece());
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms","+923");
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
                        .patch("/shipments");
        System.out.println("Test Case UpdateAllocTypeInvalid >>>>>");
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
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id","ZHtst131");
        json.put("sp_id", configFileReader.getSpId());
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
                        .patch("/shipments");
        System.out.println("Test Case UpdateAllocTypeInvalid >>>>>");
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
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFourthPiece());
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","2");
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
                        .then()
                        .extract();
        System.out.println("Test Case UpdateAllocTypeInvalid >>>>>");
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
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFourthPiece());
        json.put("sp_id", configFileReader.getSpId());
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
                        .then()
                        .extract();
        System.out.println("Test Case UpdateAllocTypeInvalid >>>>>");
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
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id","SBXPP1001");
        json.put("alloc_type","2");
        json.put("shipment_type","1");
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        System.out.println("Test Case UpdateAllocTypeInvalid >>>>>");
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
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",conf.getFourthPiece());
        json.put("alt_sp_id","10082");
        json.put("alloc_type","1");
        json.put("shipment_type","1");
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        System.out.println("Test Case UpdateAllocTypeInvalid >>>>>");
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
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_uid","4bb5f40d-78da-4678-a6aa-80873b673981");
        json.put("alloc_type","2");
        json.put("shipment_type","1");
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        System.out.println("Test Case UpdateAllocTypeInvalid >>>>>");
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
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.getFirstPiece());
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("shipment_type", "1");
        json.put("width", "15");
        json.put("height","15");
        json.put("depth", "15");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response = (Response)
                given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments");
        System.out.println("Test Case Update a shipment >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
    }

    @Test (dependsOnMethods={"UpdateAShipment"}, alwaysRun=true)
    public void ValidateUpdateShipmentSchema () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.getFirstPiece());
        json.put("sp_id", configFileReader.getSpId());
        json.put("sms","+923347347789");
        json.put("alloc_type","1");
        json.put("shipment_type", "1");
        json.put("width", "10");
        json.put("height","10");
        json.put("depth", "10");
        json.put("piece_group_code", configFileReader.getPieceGroupCode());
        baseURI = configFileReader.getShipmentApiUrl();
                         given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .patch("/shipments")
                        .then()
                        .body(matchesJsonSchemaInClasspath("schema/shipment/UpdateShipment.json"));
         System.out.println("Test Case ValidateUpdateShipmentSchema >>>>>");
    }

// -----------------------------------------------------------------------------------
// ------------------ HARD ALLOCATION - REQUESTED SP IS AVAILABLE --------------------
// -----------------------------------------------------------------------------------

    @Test(dependsOnMethods={"ValidateUpdateShipmentSchema"}, alwaysRun=true)
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

    @Test (dependsOnMethods={"ConvertFromHardToHardWithDifferentSpId"}, alwaysRun=true)
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




// -----------------------------------------------------------------------------------
// ----------------------------------- Delete ----------------------------------------
// -----------------------------------------------------------------------------------

    @Test (dependsOnMethods={"QueuedShipmentIfNoShipmentIsQueued"}, alwaysRun=true)
    public void DeleteAShipmentByPieceID () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get1stPiece());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .delete("/shipments");
        System.out.println("Test Case DeleteAShipmentByPieceID >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        System.out.println(response.statusCode());
    }

    @Test (dependsOnMethods={"DeleteAShipmentByPieceID"}, alwaysRun=true)
    public void DeleteAShipmentByShipmentID () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("shipment_id","190010041");
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .delete("/shipments");
        System.out.println("Test Case DeleteAShipmentByShipmentID >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        System.out.println(response.statusCode());
    }

    //@Test (dependsOnMethods={"DeleteAShipmentByPieceID"}, alwaysRun=true)
    public void DeleteAShipmentByPieceUID () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        //json.put("shipment_id","190010041");
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .delete("/shipments");
        System.out.println("Test Case DeleteAShipmentByShipmentID >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        System.out.println(response.statusCode());
    }

    @Test (dependsOnMethods={"DeleteAShipmentByShipmentID"}, alwaysRun=true)
    public void AlreadyDeleteAShipmentByPieceID () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.get1stPiece());
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .delete("/shipments");
        System.out.println("Test Case DeleteAShipmentByPieceID >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        System.out.println(response.statusCode());
    }

    @Test (dependsOnMethods={"AlreadyDeleteAShipmentByPieceID"}, alwaysRun=true)
    public void PieceIdNotExist () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id","JSK363832");
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .delete("/shipments");
        System.out.println("Test Case DeleteAShipmentByPieceID >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        System.out.println(response.statusCode());
    }

    //@Test (dependsOnMethods={"AlreadyDeleteAShipmentByPieceID"}, alwaysRun=true)
    public void PieceUIDNotExist () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        //json.put("piece_id","JSK363832");
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .delete("/shipments");
        System.out.println("Test Case DeleteAShipmentByPieceID >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        System.out.println(response.statusCode());

    }

    @Test (dependsOnMethods={"AlreadyDeleteAShipmentByPieceID"}, alwaysRun=true)
    public void ShipmentIdNotExist () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        //json.put("piece_id","JSK363832");
        json.put("shipment_id","190010041");
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",configFileReader.getApiKey())
                        .and()
                        .body(json.toString())
                        .delete("/shipments");
        System.out.println("Test Case DeleteAShipmentByPieceID >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        System.out.println(response.statusCode());
    }




    @AfterTest
    public void tearDown() throws Exception {
        // Close DB connection
        if (con != null) {
            con.close();
        }
    }

}


