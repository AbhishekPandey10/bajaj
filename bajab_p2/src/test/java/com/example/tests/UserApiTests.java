package com.example.tests;
import io.rest-assured.RestAssured;
import io.rest-assured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import static io.rest-assured.RestAssured.given;

public class UserApiTests {
    private static final String BASE_URL = "https://bfhldevapigw.healthrx.co.in/automation-campus/create/user";
    private static final String ROLL_NUMBER = "PD1093";
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }
    @Test
    public void testCreateUserWithValidData() {
        String requestBody = "{ \"firstName\": \"Abhishek\", \"lastName\": \"Pandey\", \"phoneNumber\": 9875380892, \"emailId\": \"10072001ap@gmail.com\" }";
        Response response = given()
                .header("roll-number", ROLL_NUMBER)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
        Assertions.assertEquals(201, response.getStatusCode());
        Assertions.assertEquals("User created successfully", response.getBody().asString());
    }
    @Test
    public void testCreateUserWithoutRollNumber() {
        String requestBody = "{ \"firstName\": \"Abhishek\", \"lastName\": \"Pandey\", \"phoneNumber\": 9875380892, \"emailId\": \"10072001ap@gmail.com\" }";
        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();

        Assertions.assertEquals(401, response.getStatusCode());
        Assertions.assertTrue(response.getBody().asString().contains("Unauthorized"));
    }
    @Test
    public void testCreateUserWithDuplicatePhoneNumber() {
        String requestBody = "{ \"firstName\": \"Swati\", \"lastName\": \"Pandey\", \"phoneNumber\": 9875380892, \"emailId\": \"swatipandey@gmail.com\" }";
        given()
                .header("roll-number", ROLL_NUMBER)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
        Response response = given()
                .header("roll-number", ROLL_NUMBER)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
        Assertions.assertEquals(400, response.getStatusCode());
        Assertions.assertTrue(response.getBody().asString().contains("Phone number already exists"));
    }
    @Test
    public void testCreateUserWithDuplicateEmailId() {
        String requestBody = "{ \"firstName\": \"Anirudh\", \"lastName\": \"Ready\", \"phoneNumber\": 9876543210, \"emailId\": \"swatipandey@gmail.com\" }";
        given()
                .header("roll-number", ROLL_NUMBER)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
        Response response = given()
                .header("roll-number", ROLL_NUMBER)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
        Assertions.assertEquals(400, response.getStatusCode());
        Assertions.assertTrue(response.getBody().asString().contains("Email ID already exists"));
    }
    @Test
    public void testCreateUserWithInvalidPhoneNumber() {
        String requestBody = "{ \"firstName\": \"Invalid\", \"lastName\": \"User\", \"phoneNumber\": \"invalidPhoneNumber\", \"emailId\": \"invalid.user@gmail.com\" }";

        Response response = given()
                .header("roll-number", ROLL_NUMBER)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
        Assertions.assertEquals(400, response.getStatusCode());
        Assertions.assertTrue(response.getBody().asString().contains("Invalid phone number format"));
    }
    @Test
    public void testCreateUserWithInvalidEmailId() {
        String requestBody = "{ \"firstName\": \"Invalid\", \"lastName\": \"User\", \"phoneNumber\": 9876543210, \"emailId\": \"invalidEmail\" }";
        Response response = given()
                .header("roll-number", ROLL_NUMBER)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();
        Assertions.assertEquals(400, response.getStatusCode());
        Assertions.assertTrue(response.getBody().asString().contains("Invalid email ID format"));
    }
}
