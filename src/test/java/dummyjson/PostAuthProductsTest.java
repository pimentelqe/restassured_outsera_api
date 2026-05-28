package dummyjson;

import base.BaseApiTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import utils.RestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PostAuthProductsTest extends BaseApiTest {
    static String accessToken;

    @Test
    public void shouldReturn200WhenLoginIsSuccessful() {
        Map<String, String> body = new HashMap<>();
        body.put("username", getApiUsername());
        body.put("password", getApiPassword());

        Response response = RestUtils.post(body, ContentType.JSON, "/auth/login");

        assertEquals(200, response.statusCode(), "Login deve retornar HTTP 200");
        assertNotNull(response.jsonPath().getString("accessToken"), "accessToken deve estar presente no response");
        assertFalse(response.jsonPath().getString("accessToken").isBlank(), "accessToken nao deve estar vazio");
        assertNotNull(response.jsonPath().getString("refreshToken"), "refreshToken deve estar presente no response");
        assertNotNull(response.jsonPath().getString("username"), "username deve estar presente no response");

        accessToken = response.jsonPath().getString("accessToken");
    }

    @Test
    public void shouldReturn400_whenLoginWithInvalidPassword() {
        Map<String, String> body = new HashMap<>();
        body.put("username", getApiUsername());
        body.put("password", "wrongpass");

        Response response = RestUtils.post(body, ContentType.JSON, "/auth/login");

        assertEquals(400, response.statusCode(), "Senha invalida deve retornar HTTP 400");
        assertEquals("Invalid credentials", response.jsonPath().getString("message"),
                "Mensagem de erro deve ser 'Invalid credentials'");
    }

    @Test
    public void shouldReturn400_whenLoginWithInvalidUsername() {
        Map<String, String> body = new HashMap<>();
        body.put("username", "invalidUser");
        body.put("password", getApiPassword());

        Response response = RestUtils.post(body, ContentType.JSON, "/auth/login");

        assertEquals(400, response.statusCode(), "Usuario invalido deve retornar HTTP 400");
        assertEquals("Invalid credentials", response.jsonPath().getString("message"),
                "Mensagem de erro deve ser 'Invalid credentials'");
    }

    @Test
    public void shouldReturn400_whenLoginWithoutUsername() {
        Map<String, String> body = new HashMap<>();
        body.put("password", getApiPassword());

        Response response = RestUtils.post(body, ContentType.JSON, "/auth/login");

        assertEquals(400, response.statusCode(), "Ausencia de username deve retornar HTTP 400");
        assertEquals("Username and password required", response.jsonPath().getString("message"),
                "Mensagem de erro deve ser 'Username and password required'");
    }

    @Test
    public void shouldReturn400_whenLoginWithoutPassword() {
        Map<String, String> body = new HashMap<>();
        body.put("username", getApiUsername());

        Response response = RestUtils.post(body, ContentType.JSON, "/auth/login");

        assertEquals(400, response.statusCode(), "Ausencia de password deve retornar HTTP 400");
        assertEquals("Username and password required", response.jsonPath().getString("message"),
                "Mensagem de erro deve ser 'Username and password required'");
    }
}
