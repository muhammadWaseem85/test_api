package api.test;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class ResetServicePoints {

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

}
