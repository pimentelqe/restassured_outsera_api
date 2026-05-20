package dummyjson;

import base.BaseApiTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import utils.RestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostAuthProductsTest extends BaseApiTest {
    static String accessToken;

    //Valida o login
    @Test
    public void shouldReturn200WhenLoginIsSuccessful(){
        Map<String,String> map = new HashMap<>();
        map.put("username", getApiUsername());
        map.put("password", getApiPassword());

        Response response = RestUtils.post(map, ContentType.JSON,"/auth/login");

        assertEquals(200,response.statusCode());
        accessToken= response.jsonPath().get("accessToken");


    }
    // Valida password inválida
    // deveria retornar 401?
    

    @Test
    public void shouldReturn400_whenLoginWithInvalidPassword(){
        Map<String,String> body = new HashMap<>();
        body.put("username", getApiUsername());
        body.put("password","wrongpass");

        Response response = RestUtils.post(body, ContentType.JSON,"/auth/login");

        assertEquals(400,response.statusCode());
        assertEquals("Invalid credentials", response.jsonPath().get("message"));
    }
    //Valida Usuário inexistente
    // deveria retornar 401?
    @Test
    public void shouldReturn400_whenLoginWithInvalidUsername(){
        Map<String,String> body = new HashMap<>();
        body.put("username","invalidUser");
        body.put("password", getApiPassword());

        Response response = RestUtils.post(body, ContentType.JSON,"/auth/login");

        assertEquals(400,response.statusCode());
        assertEquals("Invalid credentials", response.jsonPath().get("message"));
    }

    @Test
    public void shouldReturn400_whenLoginWithoutUsername(){
        Map<String,String> body = new HashMap<>();
        body.put("password", getApiPassword());

        Response response = RestUtils.post(body, ContentType.JSON,"/auth/login");

        assertEquals(400,response.statusCode());
        assertEquals("Username and password required", response.jsonPath().get("message"));
    }
    // Valida requisição sem password
    @Test
    public void shouldReturn400_whenLoginWithoutPassword(){
        Map<String,String> body = new HashMap<>();
        body.put("username", getApiUsername());

        Response response = RestUtils.post(body, ContentType.JSON,"/auth/login");

        assertEquals(400,response.statusCode());
        assertEquals("Username and password required", response.jsonPath().get("message"));
    }

}
