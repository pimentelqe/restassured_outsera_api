package steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.http.ContentType;
import maps.LoginMap;
import utils.RestUtils;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class AuthProductsSteps {

    @Dado("que tenha um payload valido da API de login")
    public void queTenhaUmPayloadValidoDaAPIDeLogin() {
        LoginMap.initLogin();
    }

    @Dado("que tenha um payload da API de login com as seguintes informacoes")
    public void queTenhaUmPayloadDaAPIDeLoginComAsSeguintesInformacoes(Map<String, Object> map) {
        LoginMap.initFromMap(map);
    }

    @Quando("envio uma requisicao do tipo POST de login")
    public void envioUmaRequisicaoDoTipoPOSTDeLogin() {
        RestUtils.post(LoginMap.getLogin(), ContentType.JSON, "/auth/login");
    }

    @Entao("armazeno o token que recebo do response")
    public void armazenoOTokenQueReceboDoResponse() {
        String token = RestUtils.getResponse().jsonPath().getString("accessToken");
        assertNotNull("Access token deve estar presente no response", token);
        assertFalse("Access token nao deve estar vazio", token.isBlank());
        LoginMap.token = token;
    }
}
