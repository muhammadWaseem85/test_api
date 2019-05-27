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


public class ShipmentDelete {
    ConfigFileReader configFileReader;
    ConfigFileReader conf = new ConfigFileReader();
    static Connection con = null;
    private static Statement stmt;

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

// -----------------------------------------------------------------------------------
// ----------------------------------- Delete ----------------------------------------
// -----------------------------------------------------------------------------------

    @Test
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




    //@AfterTest
    public void tearDown() throws Exception {
        // Close DB connection
        if (con != null) {
            con.close();
        }
    }

}


