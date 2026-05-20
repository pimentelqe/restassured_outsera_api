package steps;

import base.BaseApiTest;
import io.cucumber.java.pt.Entao;
import io.restassured.RestAssured;
import org.junit.Assert;
import utils.RestUtils;

public class AssertSteps {
    public static void setBaseURI(String uri){
        RestAssured.baseURI = uri;
    }

    @Entao("Valido o status {int} no response")
    public void validoOStatusNoResponse(int statusCode) {
        Assert.assertEquals(statusCode, RestUtils.getResponse().statusCode());

    }

}
