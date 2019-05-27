package api.page_objects;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import web.config.ConfigFileReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class ShipmentPage {

    ConfigFileReader configFileReader;
    ConfigFileReader conf = new  ConfigFileReader();
    static Connection con = null;
    private Statement stmt;
    public  String DB_URL = conf.getDbUrl();
    public  String DB_USER = conf.getDbUser();
    public  String DB_PASSWORD = conf.getDbPassword();

    public String endUserLogin () {
        configFileReader= new ConfigFileReader();
        org.json.simple.JSONObject json = new org.json.simple.JSONObject();
        json.put("piece_id","ZHtst126");
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
        String body = response.getBody().asString();
        System.out.println(body);
        return body;

    }

    public String getToken () {
        JSONObject obj = new JSONObject(endUserLogin());
        String token = (String) obj.get("token");
        return token;

    }

    public String getUID () {
        JSONObject obj = new JSONObject(endUserLogin());
        JSONObject obj2 = obj.getJSONObject("piece_info");
        String uid = obj2.getString("piece_uid");
        return uid ;

    }


    public  void abc () {
        JSONObject obj = new JSONObject(endUserLogin());
        System.out.println(obj);

    }

    @Test
    public void HardAllocationAlreadyExisting1 () {

        System.out.println(getUID());

    }


    @Test
    public void HardAllocationAlreadyExisting2 () {

        System.out.println(getUID());

    }


    @Test
    public void HardAllocationAlreadyExisting3 () {

        System.out.println(getUID());

    }


    public void setUp() {
        try{
            String dbClass = "com.mysql.cj.jdbc.Driver";
            Class.forName(dbClass).newInstance();
            Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            stmt = con.createStatement();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public String getPieceID (String piece) {
        setUp();
        String piece_id = null;
        try {
            String query = "SELECT * FROM  piece WHERE piece_barcode = " + "'" + piece+ "';";
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {

                piece_id = res.getString("piece_id");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return piece_id;
    }


    public String getHardwareUnitId (String piece) {
        setUp();
        String hardware_unit_id = null;
        try {
            String query = "SELECT * FROM  piece WHERE piece_barcode = " + "'" + piece+ "';";
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {

                hardware_unit_id = res.getString("hardware_unit_id");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hardware_unit_id;
    }


    public String getCompartmentId (String piece) {
        setUp();
        String compartment_id = null;
        try {
            String query = "SELECT * FROM  piece WHERE piece_barcode = " + "'" + piece+ "';";
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {

                compartment_id = res.getString("compartment_id");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return compartment_id;
    }


@Test
    public  void test () {
        System.out.println(getHardwareUnitId(conf.getFirstPiece()));
        System.out.println(getCompartmentId(conf.getFirstPiece()));
}





}
