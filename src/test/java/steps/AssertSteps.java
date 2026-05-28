package steps;

import io.cucumber.java.pt.Entao;
import org.junit.Assert;
import utils.RestUtils;

public class AssertSteps {

    @Entao("Valido o status {int} no response")
    public void validoOStatusNoResponse(int expectedStatus) {
        int actualStatus = RestUtils.getResponse().statusCode();
        Assert.assertEquals(
                "Status HTTP esperado: " + expectedStatus + ", recebido: " + actualStatus,
                expectedStatus,
                actualStatus
        );
    }

    @Entao("Valido que o response contem o campo {string}")
    public void validoQueOResponseContemOCampo(String field) {
        Object value = RestUtils.getResponse().jsonPath().get(field);
        Assert.assertNotNull("Campo '" + field + "' deve estar presente no response", value);
    }

    @Entao("Valido que a mensagem de erro e {string}")
    public void validoQueAMensagemDeErroE(String expectedMessage) {
        String actualMessage = RestUtils.getResponse().jsonPath().getString("message");
        Assert.assertEquals(
                "Mensagem de erro esperada: '" + expectedMessage + "', recebida: '" + actualMessage + "'",
                expectedMessage,
                actualMessage
        );
    }
}
