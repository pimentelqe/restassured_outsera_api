package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

public class RestUtils {
    private static Response response;

    public static void setBaseURI(String uri){
        RestAssured.baseURI = uri;
    }

    public  static  String getBaseURI(){
        return RestAssured.baseURI;
    }

    public static Response getResponse() {
        return response;
    }

    public static Response post(Object json, ContentType contentType, String endpoint){
        response = RestAssured.given()
                .relaxedHTTPSValidation()
                .contentType(contentType)
                .body(json)
                .log().all()
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        return response;
    }

    public static Response get(Map<String, String> header, String endpoint) {
        return RestAssured.given()
                .relaxedHTTPSValidation()
                .headers(header)
                .when()
                .get(endpoint)
                .thenReturn();
    }

    public static Response get( String endpoint) {
        return RestAssured.given()
                .relaxedHTTPSValidation()
                .when()
                .get(endpoint)
                .thenReturn();
    }

}
