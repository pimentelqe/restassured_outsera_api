package steps;

import config.EnvConfig;
import io.cucumber.java.Before;
import utils.RestUtils;

public class Hooks {

    @Before
    public void setup() {
        RestUtils.setBaseURI(EnvConfig.getBaseUrl());
    }
}
