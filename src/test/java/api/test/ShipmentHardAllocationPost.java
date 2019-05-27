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

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Random;
import java.util.Scanner;


public class ShipmentHardAllocationPost {
    ConfigFileReader configFileReader;
    ConfigFileReader conf = new ConfigFileReader();
    static Connection con = null;
    private static Statement stmt;
    Connection conn = null;
    Session session= null;

    @BeforeTest
    public void setUp() throws FileNotFoundException {

        int lport=5656;
        String rhost = conf.getDbUrl();
        String host = conf.getSshHostName();
        int rport=3306;
        Scanner file = new Scanner(new File("infinity_db_key_pair.pem"));
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



    @Test
    public void HardAllocation () throws SQLException {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("piece_id",configFileReader.getFirstPiece());
        //json.put("piece_id","ZHtst132");
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


