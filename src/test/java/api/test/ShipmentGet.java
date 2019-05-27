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


public class ShipmentGet {
    ConfigFileReader configFileReader;
    ConfigFileReader conf = new ConfigFileReader();
    static Connection con = null;
    private static Statement stmt;

    //@BeforeTest
    public void setUp() {

        int lport = 5656;
        String rhost = conf.getDbUrl();
        String host = conf.getSshHostName();
        int rport = 3306;
        String user = conf.getSshUser();
        String password = "";
        String dbuserName = conf.getDbUser();
        String dbpassword = conf.getDbPassword();
        String url = "jdbc:mysql://localhost:" + lport + "/" + conf.getDbName();
        String driverName = "com.mysql.cj.jdbc.Driver";
        String SshKeyFilepath = "D:\\infinity_db_key_pair.pem";
        Connection conn = null;
        Session session = null;
        try {
            //Set StrictHostKeyChecking property to no to avoid UnknownHostKey issue
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, 22);
            jsch.addIdentity(SshKeyFilepath);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            System.out.println(session);
            System.out.println("Connected");
            int assinged_port = session.setPortForwardingL(lport, rhost, rport);
            System.out.println("localhost:" + assinged_port + " -> " + rhost + ":" + rport);
            System.out.println("Port Forwarded");

            //mysql database connectivity
            Class.forName(driverName).newInstance();
            conn = DriverManager.getConnection(url, dbuserName, dbpassword);
            stmt = conn.createStatement();
            System.out.println();
            System.out.println("Database connection established");
            System.out.println("DONE");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void HardAllocationViewShipmentDetails() {
        configFileReader = new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", configFileReader.getFirstPiece())
                        .header("Content-Type", "application/json")
                        .header("X-Api-Key", configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case HardAllocationViewShipmentDetails >>>>>");
        Assert.assertEquals(response.statusCode(), 200);
        String body = response.getBody().asString();
        System.out.println(body);
    }

    //@Test(dependsOnMethods = {"HardAllocationViewShipmentDetails"}, alwaysRun = true)
    public void ValidateViewShipmentDetailSchema() {
        configFileReader = new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        given()
                .queryParam("piece_id", configFileReader.getFirstPiece())
                .header("Content-Type", "application/json")
                .header("X-Api-Key", configFileReader.getApiKey())
                .get("/shipments").then().body(matchesJsonSchemaInClasspath("schema/shipment/ViewShipment.json"));
        System.out.println("Test Case ValidateViewShipmentDetailSchema >>>>>");
    }

    @Test(dependsOnMethods = {"HardAllocationViewShipmentDetails"}, alwaysRun = true)
    public void GetHardAllocationParamMissing() {
        configFileReader = new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        //.queryParam("piece_id", configFileReader.getFirstPiece())
                        .header("Content-Type", "application/json")
                        .header("X-Api-Key", configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationParamMissing >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals(code, 31);
        Assert.assertEquals(msg, "shipment_id / piece_id / piece_uid is missing.");
    }

    @Test(dependsOnMethods = {"GetHardAllocationParamMissing"}, alwaysRun = true)
    public void GetHardAllocationByPieceIdAlphaNumeric() {
        configFileReader = new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", "AYAH1234")
                        .header("Content-Type", "application/json")
                        .header("X-Api-Key", configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationByPieceIdAlphaNumeric >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals(code, 23);
        Assert.assertEquals(msg, "No record found.");
    }

    @Test(dependsOnMethods = {"GetHardAllocationByPieceIdAlphaNumeric"}, alwaysRun = true)
    public void GetHardAllocationByPieceIdNumeric() {
        configFileReader = new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", "1234567")
                        .header("Content-Type", "application/json")
                        .header("X-Api-Key", configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationByPieceIdNumeric >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals(code, 23);
        Assert.assertEquals(msg, "No record found.");
    }

    @Test(dependsOnMethods = {"GetHardAllocationByPieceIdNumeric"}, alwaysRun = true)
    public void GetHardAllocationByPieceIdAlpha() {
        configFileReader = new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", "ABCDEF")
                        .header("Content-Type", "application/json")
                        .header("X-Api-Key", configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationByPieceIdAlpha >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals(code, 23);
        Assert.assertEquals(msg, "No record found.");
    }

    @Test(dependsOnMethods = {"GetHardAllocationByPieceIdAlpha"}, alwaysRun = true)
    public void GetHardAllocationStartingFromZero() {
        configFileReader = new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", "0CDEF1234")
                        .header("Content-Type", "application/json")
                        .header("X-Api-Key", configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationStartingFromZero >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals(code, 23);
        Assert.assertEquals(msg, "No record found.");
    }

    @Test(dependsOnMethods = {"GetHardAllocationStartingFromZero"}, alwaysRun = true)
    public void GetHardAllocationByPieceIdMinLength() {
        configFileReader = new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", "0")
                        .header("Content-Type", "application/json")
                        .header("X-Api-Key", configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationByPieceIdMinLength >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals(code, 49);
        Assert.assertEquals(msg, "Invalid piece or shipment id.");
    }

    @Test(dependsOnMethods = {"GetHardAllocationByPieceIdMinLength"}, alwaysRun = true)
    public void GetHardAllocationByPieceIdMaxLength() {
        configFileReader = new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", "AHHAHAHHAHHAHA0000000000000000000000000000234782934792347293487293423874293489324")
                        .header("Content-Type", "application/json")
                        .header("X-Api-Key", configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationByPieceIdMaxLength >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals(code, 49);
        Assert.assertEquals(msg, "Invalid piece or shipment id.");
    }

    @Test(dependsOnMethods = {"GetHardAllocationByPieceIdMaxLength"}, alwaysRun = true)
    public void GetHardAllocationByPieceIdNoneAlphaNumeric() {
        configFileReader = new ConfigFileReader();
        baseURI = configFileReader.getShipmentApiUrl();
        Response response =
                given()
                        .queryParam("piece_id", "*&*&@*#&@*#&@*")
                        .header("Content-Type", "application/json")
                        .header("X-Api-Key", configFileReader.getApiKey())
                        .get("/shipments");
        System.out.println("Test Case GetHardAllocationByPieceIdNoneAlphaNumeric >>>>>");
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        String msg = response.path("message");
        Assert.assertEquals(code, 49);
        Assert.assertEquals(msg, "Invalid piece or shipment id.");
    }


}