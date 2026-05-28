package dummyjson;

import base.BaseApiTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import maps.LoginMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.RestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GetAuthProductsTest extends BaseApiTest {

    @BeforeAll
    public static void loginAndStoreToken() {
        LoginMap.initLogin();
        Response response = RestUtils.post(LoginMap.getLogin(), ContentType.JSON, "/auth/login");

        assertEquals(200, response.statusCode(), "Login no @BeforeAll deve retornar 200");
        String token = response.jsonPath().getString("accessToken");
        assertNotNull(token, "Token de autenticacao nao deve ser nulo");
        assertFalse(token.isBlank(), "Token de autenticacao nao deve estar vazio");
        LoginMap.token = token;
    }

    private Map<String, String> authHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + LoginMap.token);
        return header;
    }

    @Test
    public void shouldReturn200AndProductsList_whenRequestAuthenticatedProducts() {
        Response response = RestUtils.get(authHeader(), "/auth/products");

        assertEquals(200, response.statusCode(), "GET /auth/products deve retornar HTTP 200");
        List<?> products = response.jsonPath().getList("products");
        assertNotNull(products, "Campo 'products' deve estar presente no response");
        assertFalse(products.isEmpty(), "Lista de products nao deve estar vazia");
    }

    @Test
    public void shouldContainRequiredProductFields_whenFetchingAuthenticatedProducts() {
        Response response = RestUtils.get(authHeader(), "/auth/products");

        assertNotNull(response.jsonPath().get("products[0].id"), "Campo 'id' nao deve ser nulo");
        assertNotNull(response.jsonPath().get("products[0].title"), "Campo 'title' nao deve ser nulo");
        assertNotNull(response.jsonPath().get("products[0].price"), "Campo 'price' nao deve ser nulo");
        assertNotNull(response.jsonPath().get("products[0].category"), "Campo 'category' nao deve ser nulo");
        assertNotNull(response.jsonPath().get("products[0].thumbnail"), "Campo 'thumbnail' nao deve ser nulo");
    }

    @Test
    public void shouldValidateProductFieldTypesCorrectly_whenFetchingProducts() {
        Response response = RestUtils.get(authHeader(), "/auth/products");

        assertTrue(response.jsonPath().get("products[0].id") instanceof Integer,
                "Campo 'id' deve ser do tipo Integer");
        assertTrue(response.jsonPath().get("products[0].price") instanceof Float,
                "Campo 'price' deve ser do tipo Float");
        assertTrue(response.jsonPath().get("products[0].stock") instanceof Integer,
                "Campo 'stock' deve ser do tipo Integer");
    }

    @Test
    public void shouldReturnDatesInIsoFormat_whenFetchingProductMetadata() {
        Response response = RestUtils.get(authHeader(), "/auth/products");
        List<String> dates = response.jsonPath().getList("products.meta.createdAt");

        assertFalse(dates.isEmpty(), "Lista de datas 'createdAt' nao deve ser vazia");
        for (String date : dates) {
            assertTrue(date.matches("^\\d{4}-\\d{2}-\\d{2}T.*Z$"),
                    "Data deve estar no formato ISO 8601: " + date);
        }
    }

    @Test
    public void shouldReturnNonEmptyImagesList_whenFetchingProducts() {
        Response response = RestUtils.get(authHeader(), "/auth/products");
        List<List<String>> images = response.jsonPath().getList("products.images");

        assertFalse(images.isEmpty(), "Lista de images nao deve ser vazia");
        for (List<String> imgList : images) {
            assertFalse(imgList.isEmpty(), "Cada produto deve ter ao menos uma imagem");
        }
    }

    @Test
    public void shouldNotReturnNullForRequiredFields_whenFetchingProducts() {
        Response response = RestUtils.get(authHeader(), "/auth/products");
        List<Map<String, Object>> products = response.jsonPath().getList("products");

        assertFalse(products.isEmpty(), "Lista de produtos nao deve estar vazia");
        for (Map<String, Object> product : products) {
            assertNotNull(product.get("id"), "Campo 'id' nao deve ser nulo em nenhum produto");
            assertNotNull(product.get("title"), "Campo 'title' nao deve ser nulo em nenhum produto");
            assertNotNull(product.get("price"), "Campo 'price' nao deve ser nulo em nenhum produto");
        }
    }

    @Test
    public void shouldReturnValidProductDimensions_whenFetchingProducts() {
        Response response = RestUtils.get(authHeader(), "/auth/products");
        List<Map<String, Object>> dimensions = response.jsonPath().getList("products.dimensions");

        for (Map<String, Object> dim : dimensions) {
            assertTrue((Float) dim.get("width") > 0, "Dimensao 'width' deve ser positiva");
            assertTrue((Float) dim.get("height") > 0, "Dimensao 'height' deve ser positiva");
            assertTrue((Float) dim.get("depth") > 0, "Dimensao 'depth' deve ser positiva");
        }
    }

    @Test
    public void shouldReturn401_whenTokenIsInvalid() {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer token_invalido");

        Response response = RestUtils.get(header, "/auth/products");

        assertEquals(401, response.statusCode(), "Token invalido deve retornar HTTP 401");
        assertNotNull(response.jsonPath().getString("message"), "Response deve conter campo 'message'");
    }

    @Test
    public void shouldReturn401_whenTokenIsNotProvided() {
        Response response = RestUtils.get(new HashMap<>(), "/auth/products");

        assertEquals(401, response.statusCode(), "Ausencia de token deve retornar HTTP 401");
        assertNotNull(response.jsonPath().getString("message"), "Response deve conter campo 'message'");
    }
}
