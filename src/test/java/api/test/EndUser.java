package api.test;

import api.page_objects.EndUserPage;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import web.config.ConfigFileReader;


public class EndUser {
    ConfigFileReader configFileReader;
    ConfigFileReader conf = new ConfigFileReader();
    String username = null;
    String userId = null;
    String token = null;
    String refreshToken = null;
    Integer expiresIn = null;


// -----------------------------------------------------------------------------------
// ------------------------------- Sign up section -----------------------------------
// -----------------------------------------------------------------------------------

    @Test
    public void EndUserSignUpWithoutPhoneNumber () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        //json.put("phone","+923347347789");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .post("/signup");

        System.out.println("Test Case EndUserSignUpWithoutPhoneNumber >>>>>");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 500);
        String type = response.path("type");
        String msg = response.path("message");
        Assert.assertEquals( type,"Error");
        Assert.assertEquals(msg, "The string supplied did not seem to be a phone number");
    }

    @Test (dependsOnMethods={"EndUserSignUpWithoutPhoneNumber"}, alwaysRun=true)
    public void EndUserSignUpWithInvalidPhoneNumber () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("phone","47789");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .post("/signup");

        System.out.println("Test Case EndUserSignUpWithInvalidPhoneNumber >>>>>");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        String msg = response.path("error");
        Assert.assertEquals(msg, "Invalid Phone Number");
    }

    @Test (dependsOnMethods={"EndUserSignUpWithInvalidPhoneNumber"}, alwaysRun=true)
    public void EndUserSignUpWithMexLengthPhoneNumber () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("phone","+923347347789939393947789");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .post("/signup");

        System.out.println("Test Case EndUserSignUpWithMexLengthPhoneNumber >>>>>");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 500);
        String type = response.path("type");
        String msg = response.path("message");
        Assert.assertEquals(type, "Error");
        Assert.assertEquals(msg, "The string supplied is too long to be a phone number");

    }

    @Test (dependsOnMethods={"EndUserSignUpWithMexLengthPhoneNumber"}, alwaysRun=true)
    public void SignUpEndUserAlreadyExists () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("phone","+923347347789");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .post("/signup");

        System.out.println("Test Case SignUpEndUserAlreadyExists >>>>>");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        String msg = response.path("error");
        Assert.assertEquals(msg, "End User Already Registered");
    }

    // THIS CASE WILL BE Discuss
   // @Test
    public void ResetEndUser () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("phone","+923347347789");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .body(json.toString())
                        .post("/signup");

        System.out.println("Test Case SignUpEndUserAlreadyExists >>>>>");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        String msg = response.path("error");
        Assert.assertEquals(msg, "End User Already Registered");

    }

    // THIS CASE WILL BE Discuss
    // @Test
    public void SignUpEndUser () {
        configFileReader= new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("phone","+923347347789");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .body(json.toString())
                        .post("/signup");

        System.out.println("Test Case SignUpEndUserAlreadyExists >>>>>");
        String body = response.getBody().asString();
        System.out.println(response.statusCode());
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        String msg = response.path("error");
        Assert.assertEquals(msg, "End User Already Registered");

    }


// -----------------------------------------------------------------------------------
// ------------------------------- Login Section -----------------------------------
// -----------------------------------------------------------------------------------

    @Test (dependsOnMethods={"SignUpEndUserAlreadyExists"}, alwaysRun=true)
    public void LoginWithoutCredential () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .post("/login");
        System.out.println("Test Case LoginWithoutCredential >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        Assert.assertEquals(code , 1000);
        String msg = response.path("error");
        Assert.assertEquals(msg, "Login Failed");

    }

    @Test (dependsOnMethods={"LoginWithoutCredential"}, alwaysRun=true)
    public void LoginWithoutPhoneNumber () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("pin", "123456");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .post("/login");
        System.out.println("Test Case LoginWithoutPhoneNumber >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        Assert.assertEquals(code , 1000);
        String msg = response.path("error");
        Assert.assertEquals(msg, "Login Failed");
    }

    @Test (dependsOnMethods={"LoginWithoutPhoneNumber"}, alwaysRun=true)
    public void LoginWithoutPIN () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("phone",conf.getPhNumber());
        //json.put("pin", "123456");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .post("/login");
        System.out.println("Test Case LoginWithoutPIN >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        String msg = response.path("error");
        Assert.assertEquals(msg, "Missing required parameter PASSWORD");
    }

    @Test (dependsOnMethods={"LoginWithoutPIN"}, alwaysRun=true)
    public void LoginWithIncorrectPIN () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("phone",conf.getPhNumber());
        json.put("pin", "12345689");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .post("/login");
        System.out.println("Test Case LoginWithIncorrectPIN >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        String msg = response.path("error");
        Assert.assertEquals(msg, "Incorrect username or password.");
    }

    @Test (dependsOnMethods={"LoginWithIncorrectPIN"}, alwaysRun=true)
    public void LoginWithIncorrectPhoneNumber () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("phone","+923347340000");
        json.put("pin", "123456");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .post("/login");
        System.out.println("Test Case LoginWithIncorrectPhoneNumber >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        Assert.assertEquals(code , 1000);
        String msg = response.path("error");
        Assert.assertEquals(msg, "Login Failed");

    }

    @Test (dependsOnMethods={"LoginWithIncorrectPIN"}, alwaysRun=true)
    public void LoginWithCorrectCredentials () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("phone", conf.getPhNumber());
        json.put("pin", conf.getPIN());
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =   (Response)
               given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .post("/login")
                .then()
                .extract();
        System.out.println("Test Case LoginWithCorrectCredentials >>>>>");
        response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/enduser/EndUserLogin.json"));
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 201);
        JSONObject obj = new JSONObject(body);
        username = (String) obj.get("username");
        userId = (String) obj.get("user_id");
        token = (String) obj.get("token");
        refreshToken = (String) obj.get("refresh_token");
        expiresIn = obj.getInt("expires_in");
        // Need to apply assertion for expire_in
        //Assert.assertEquals(expiresIn, 3600);

    }



// ------------------------------------------------------------------------------------------
// ------------------------------- Activation Key Section -----------------------------------
// ------------------------------------------------------------------------------------------
/*
    @Test //(dependsOnMethods={"LoginWithIncorrectPIN"}, alwaysRun=true)
    public void ActivationKeyWithoutQueryParameters () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =   (Response)
            given()
                    .header("Content-Type","application/json")
                    .and()
                    .body(json.toString())
                    .post("/phone")
                    .then()
                    .extract();
        System.out.println("Test Case ActivationKeyWithoutQueryParameters  >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        String err = response.path("error");
        Assert.assertEquals(err, "Unable to verify User");
    }

    @Test //(dependsOnMethods={"ActivationKeyWithoutQueryParameters"}, alwaysRun=true)
    public void ActivationKeyWithMissingCode () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
//    json.put("activation_code", "8585");
    json.put("phone", conf.getPhNumber());
    json.put("pin", conf.getPIN());
//    json.put("device_id", "idkddkdkdk8585");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =   (Response)
                given()
                        .header("Content-Type","application/json")
                        .and()
                        .body(json.toString())
                        .post("/phone")
                        .then()
                        .extract();
        System.out.println("Test Case ActivationKeyWithoutQueryParameters  >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        String err = response.path("error");
        Assert.assertEquals(err, "Unable to verify User");
    }


    @Test //(dependsOnMethods={"ActivationKeyWithoutQueryParameters"}, alwaysRun=true)
    public void ActivationKeyWithInvalidCode () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("activation_code", "8585");
        json.put("phone", conf.getPhNumber());
        json.put("pin", conf.getPIN());
        json.put("device_id", "idkddkdkdk8585");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =   (Response)
                given()
                        .header("Content-Type","application/json")
                        .and()
                        .body(json.toString())
                        .post("/phone")
                        .then()
                        .extract();
        System.out.println("Test Case ActivationKeyWithoutQueryParameters  >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        String err = response.path("error");
        Assert.assertEquals(err, "Unable to verify User");
    }


    */


// ------------------------------------------------------------------------------------------
// ------------------------------- Forgot Pin Section ---------------------------------------
// ------------------------------------------------------------------------------------------

    @Test (dependsOnMethods={"LoginWithCorrectCredentials"}, alwaysRun=true)
    public void ForgotPinCodeWithMissingPhoneNumber () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .post("/pin");
        System.out.println("Test Case ForgotPinCodeWithMissingPhoneNumber >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        String msg = response.path("error");
        Assert.assertEquals(msg, "unknown Phone number");

    }

    @Test (dependsOnMethods={"ForgotPinCodeWithMissingPhoneNumber"}, alwaysRun=true)
    public void ForgotPinCodeWithInvalidPhoneNumber () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("phone","+923347340000");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .post("/pin");
        System.out.println("Test Case ForgotPinCodeWithInvalidPhoneNumber >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        String msg = response.path("error");
        Assert.assertEquals(msg, "unknown Phone number");

    }

    @Test (dependsOnMethods={"ForgotPinCodeWithInvalidPhoneNumber"}, alwaysRun=true)
    public void ForgotPinCode () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("phone",conf.getPhNumber());
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .post("/pin");
        System.out.println("Test Case ForgotPinCode>>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 201);
        String msg = response.path("success");
        Assert.assertEquals(msg, "Activation code has been sent to enduser in SMS");

    }


// ------------------------------------------------------------------------------------------
// ------------------------------- Change Pin Code Section --------------------------
// ------------------------------------------------------------------------------------------

    //When end user receive verification code on phone , that verification code will be sent
    //alongside phone to change the pin code

    @Test (dependsOnMethods={"ForgotPinCode"}, alwaysRun=true)
    public void ChangePinCodeWithMissingPhone () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        //json.put("phone","+923347340000");
        json.put("confirmation_code","626250");
        json.put("pin","123456");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .patch("/pin");
        System.out.println("Test Case ChangePinCodeWithMissingPhone >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        String msg = response.path("error");
        Assert.assertEquals(msg, "Unknown Phone Number");

    }

    @Test (dependsOnMethods={"ChangePinCodeWithMissingPhone"}, alwaysRun=true)
    public void ChangePinCodeWithInvalidPhone () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("phone","+923347340000");
        json.put("confirmation_code","626250");
        json.put("pin","123456");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .patch("/pin");
        System.out.println("Test Case ChangePinCodeWithInvalidPhone >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        String msg = response.path("error");
        Assert.assertEquals(msg, "Unknown Phone Number");
    }

    @Test (dependsOnMethods={"ChangePinCodeWithInvalidPhone"}, alwaysRun=true)
    public void ChangePinCodeWithInvalidCode () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("phone",conf.getPhNumber());
        json.put("confirmation_code","62625");
        json.put("pin","123456");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .patch("/pin");
        System.out.println("Test Case ChangePinCodeWithInvalidCode >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
        int code = response.path("code");
        Assert.assertEquals(code, 1010);
        String msg = response.path("error");
        Assert.assertEquals(msg, "Invalid verification code provided");

    }

    //@Test (dependsOnMethods={"ChangePinCodeWithInvalidCode"}, alwaysRun=true)
    public void ChangePinCode () {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("phone",conf.getPhNumber());
        json.put("confirmation_code","525094");
        json.put("pin","123456");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .and()
                        .body(json.toString())
                        .patch("/pin");
        System.out.println("Test Case ChangePinCode >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 200);
        String msg = response.path("success");
        Assert.assertEquals(msg, "Invalid verification code provided");

    }



// ------------------------------------------------------------------------------------------
// ------------------------------- Change End User Phone Number Section ---------------------------------
// ------------------------------------------------------------------------------------------

    //When an enduser app wants to change the receiving phone number this method is called.
    //It will return an activation code on SMS on the new number and a new pin will be able to
    //be generated.
/*
    @Test //(dependsOnMethods={"ChangePinCodeWithInvalidCode"}, alwaysRun=true)
    public void ChangePhoneNumberWithMissingOldNumber() {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        //json.put("old_phone",conf.getPhNumber());
        json.put("new_phone","+923316068554");
        json.put("pin","123456");
        baseURI = configFileReader.getEndUserApiUrl2();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .header("x-auth-token", token)
                        .and()
                        .body(json.toString())
                        .patch("/phone");
        System.out.println("Test Case ChangePhoneNumberWithMissingOldNumber >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
//        String type = response.path("type");
//        String msg = response.path("message");
//        Assert.assertEquals(type, "Error");
//        Assert.assertEquals(msg, "Invalid New Phone Number");

    }

    @Test //(dependsOnMethods={"ChangePhoneNumberWithMissingOldNumber"}, alwaysRun=true)
    public void ChangePhoneNumberWithMissingNewNumber() {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("old_phone",conf.getPhNumber());
        //json.put("new_phone","+3316068554");
        json.put("pin","123456");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .body(json.toString())
                        .patch("/phone");
        System.out.println("Test Case ChangePhoneNumberWithMissingNewNumber >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 500);
        String type = response.path("type");
        String msg = response.path("message");
        Assert.assertEquals(type, "Error");
        Assert.assertEquals(msg, "The string supplied did not seem to be a phone number");

    }

    @Test //(dependsOnMethods={"ChangePhoneNumberWithMissingNewNumber"}, alwaysRun=true)
    public void ChangePhoneNumberWithMissingOldAndNewNumber() {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        //json.put("old_phone",conf.getPhNumber());
        //json.put("new_phone","+3316068554");
        json.put("pin","123456");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .body(json.toString())
                        .patch("/phone");
        System.out.println("Test Case ChangePhoneNumberWithMissingOldAndNewNumber >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 500);
        String type = response.path("type");
        String msg = response.path("message");
        Assert.assertEquals(type, "Error");
        Assert.assertEquals(msg, "The string supplied did not seem to be a phone number");

    }

    @Test //(dependsOnMethods={"ChangePhoneNumberWithMissingOldAndNewNumber"}, alwaysRun=true)
    public void ChangePhoneNumberWithInvalidPIN() {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("old_phone","+923316068554");
        json.put("new_phone",conf.getPhNumber());
        json.put("pin","1234");
        baseURI = "https://infinity.swipbox.com/prepilotapi/enduser-api/v1.2";
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .body(json.toString())
                        .patch("/phone");
        System.out.println("Test Case ChangePhoneNumberWithInvalidPIN >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
//        int code = response.path("code");
//        Assert.assertEquals(code, 1010);
//        String msg = response.path("error");
//        Assert.assertEquals(msg, "Unknown Phone Number");

    }

    @Test //(dependsOnMethods={"ChangePhoneNumberWithInvalidPIN"}, alwaysRun=true)
    public void ChangePhoneNumber() {
        configFileReader = new ConfigFileReader();
        JSONObject json = new JSONObject();
        json.put("old_phone",conf.getPhNumber());
        json.put("new_phone","+923316068554");
        json.put("pin","123456");
        baseURI = configFileReader.getEndUserApiUrl();
        Response response =
                (Response) RestAssured.given()
                        .header("Content-Type","application/json")
                        .and()
                        .body(json.toString())
                        .patch("/phone");
        System.out.println("Test Case ChangePhoneNumber >>>>>");
        System.out.println(response.statusCode());
        String body = response.getBody().asString();
        System.out.println(body);
        Assert.assertEquals(response.statusCode(), 400);
//        int code = response.path("code");
//        Assert.assertEquals(code, 1010);
//        String msg = response.path("error");
//        Assert.assertEquals(msg, "Unknown Phone Number");

    }

*/
    //-------------------

    @Test (dependsOnMethods={"LoginWithCorrectCredentials"}, alwaysRun=true)
    public void GetParcelForEndUser () {
        configFileReader= new ConfigFileReader();
        EndUserPage endUser = new EndUserPage();
        baseURI = configFileReader.getEndUserApiUrl();
        Response getParcel =
                RestAssured.given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .header("x-auth-token", token)
                        .get("/shipments");
        System.out.println("Test Case GetParcelForEndUser  >>>>>");
        System.out.println(getParcel.statusCode());
        String responseBody = getParcel.getBody().asString();
        System.out.println(responseBody);
    }

    @Test (dependsOnMethods={"GetParcelForEndUser"}, alwaysRun=true)
    public void ValidateParcelForEndUserSchema () {
        configFileReader= new ConfigFileReader();
        EndUserPage endUser = new EndUserPage();
        baseURI = configFileReader.getEndUserApiUrl();
                RestAssured.given()
                        .header("Content-Type","application/json")
                        .header("X-Api-Key",conf.getApiKeyForEndUser())
                        .header("x-auth-token", token)
                        .get("/shipments")
                        .then().body(matchesJsonSchemaInClasspath("schema/enduser/ParcelForEndUser.json"));
        System.out.println("Test Case ValidateParcelForEndUserSchema >>>>>");
    }



}
