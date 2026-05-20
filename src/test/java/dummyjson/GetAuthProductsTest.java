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
    static String accessToken;

    @BeforeAll
    public static void loginValidateMap(){
        LoginMap.initLogin();
        Response response = RestUtils.post(LoginMap.getLogin(),ContentType.JSON,"/auth/login");

        assertEquals(200,response.statusCode());
        LoginMap.token = response.jsonPath().get("accessToken");

    }

    //Valida retorno de produtos
    @Test
    public void shouldReturn200AndProductsList_whenRequestAuthenticatedProducts(){
        Map<String,String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+LoginMap.token);

        Response response = RestUtils.get(header,"/auth/products");

        assertEquals(200, response.statusCode());
        assertNotNull(response.jsonPath().getList("products"));
    }
    //Valida Estrutura obrigatória do produto
    @Test
    public void shouldContainRequiredProductFields_whenFetchingAuthenticatedProducts(){
        Map<String,String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+LoginMap.token);

        Response response = RestUtils.get(header,"/auth/products");

        assertNotNull(response.jsonPath().get("products[0].id"));
        assertNotNull(response.jsonPath().get("products[0].title"));
        assertNotNull(response.jsonPath().get("products[0].price"));
    }
    //Valida Tipos de dados
    @Test
    public void shouldValidateProductFieldTypesCorrectly_whenFetchingProducts(){
        Map<String,String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+LoginMap.token);

        Response response = RestUtils.get(header,"/auth/products");

        assertTrue(response.jsonPath().get("products[0].id") instanceof Integer);
        assertTrue(response.jsonPath().get("products[0].price") instanceof Float);
    }
    //Vaida datas no formato ISO
    @Test
    public void shouldReturnDatesInIsoFormat_whenFetchingProductMetadata(){
        Map<String,String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+LoginMap.token);

        Response response = RestUtils.get(header,"/auth/products");

        List<String> dates = response.jsonPath().getList("products.meta.createdAt");

        for(String date : dates){
            assertTrue(date.matches("^\\d{4}-\\d{2}-\\d{2}T.*Z$"));
        }
    }
    //Valida lista de imagens não vazia
    @Test
    public void shouldReturnNonEmptyImagesList_whenFetchingProducts(){
        Map<String,String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+LoginMap.token);

        Response response = RestUtils.get(header,"/auth/products");

        List<List<String>> images = response.jsonPath().getList("products.images");

        for(List<String> imgList : images){
            assertFalse(imgList.isEmpty());
        }
    }
    //Campos obrigatórios não nulos
    @Test
    public void shouldNotReturnNullForRequiredFields_whenFetchingProducts(){
        Map<String,String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+LoginMap.token);

        Response response = RestUtils.get(header,"/auth/products");

        List<Map<String,Object>> products = response.jsonPath().getList("products");

        for(Map<String,Object> product : products){
            assertNotNull(product.get("id"));
            assertNotNull(product.get("title"));
            assertNotNull(product.get("price"));
        }
    }
    // Valida Dimensões válidas
    @Test
    public void shouldReturnValidProductDimensions_whenFetchingProducts(){
        Map<String,String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+LoginMap.token);

        Response response = RestUtils.get(header,"/auth/products");

        List<Map<String,Object>> dimensions = response.jsonPath().getList("products.dimensions");

        for(Map<String,Object> dim : dimensions){
            assertTrue((Float) dim.get("width") > 0);
            assertTrue((Float) dim.get("height") > 0);
            assertTrue((Float) dim.get("depth") > 0);
        }
    }
    // Valida Token invalido
    @Test
    public void shouldReturn401_whenTokenIsInvalid(){
        Map<String,String> header = new HashMap<>();
        header.put("Authorization", "Bearer token_invalido");

        Response response = RestUtils.get(header,"/auth/products");

        assertEquals(401, response.statusCode());
    }
   // Valida requisição sem token
    @Test
    public void shouldReturn401_whenTokenIsNotProvided(){
        Map<String,String> header = new HashMap<>();

        Response response = RestUtils.get(header,"/auth/products");

        assertEquals(401, response.statusCode());
    }


}
