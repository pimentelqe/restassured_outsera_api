package maps;

import java.util.HashMap;
import java.util.Map;

import static base.BaseApiTest.getApiPassword;
import static base.BaseApiTest.getApiUsername;

public class LoginMap {
    private static Map<String, Object> login;
    public static String token;

    public static Map<String, Object> getLogin() {
        return login;
    }

    public static  void initLogin(){
        login = new HashMap<>();
        login.put("username", getApiUsername());
        login.put("password", getApiPassword());
    }

}
