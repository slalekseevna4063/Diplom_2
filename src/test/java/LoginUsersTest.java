import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import userpojos.*;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class LoginUsersTest {
    private ExistingUserPojo oldUser;
    private ExistingUserPojo defectUser; // пользователь с невалидными данными
    private NewUserPojo newUser;

    private String accessToken;
    private Response responseLogin;
    private String success;
    private Response responseCreate;

    @Before
    public void Preconditions() {
        newUser = NewUserPojo.getRandomUser();
        responseCreate = UserApiPojo.createUser(newUser);
        accessToken = responseCreate.then().statusCode(SC_OK).extract().body().as(AuthPojo.class).getAccessToken();
        oldUser = new ExistingUserPojo(newUser.getEmail(), newUser.getPassword());
    }


    @Test
    @DisplayName("Логин под уже существующим пользователем.")
    public void checkLoginUser() {
        responseLogin = UserApiPojo.loginUser(oldUser);
        success = responseLogin.then().statusCode(SC_OK).extract().body().as(AuthPojo.class).getSuccess();
        assertEquals("true", success);
    }

    @Test
    @DisplayName("Логин с невалидной почтой.")
    public void checkLoginWrongEmail() {
        defectUser = new ExistingUserPojo(newUser.getEmail(), newUser.getPassword());
        defectUser.setRandomEmail();
        responseLogin = UserApiPojo.loginUser(defectUser);
        // Проверяем код ответа и поле ответа "success"
        success = responseLogin.then().statusCode(SC_UNAUTHORIZED).extract().body().as(ErrorsPojo.class).getSuccess();
        assertEquals("false", success);
    }

    @Test
    @DisplayName("Логин с невалидным паролем.")
    public void checkLoginWrongPassword() {
        defectUser = new ExistingUserPojo(newUser.getEmail(), newUser.getPassword());
        defectUser.setRandomPassword();
        responseLogin = UserApiPojo.loginUser(defectUser);
        // Проверяем код ответа и поле ответа "success"
        success = responseLogin.then().statusCode(SC_UNAUTHORIZED).extract().body().as(ErrorsPojo.class).getSuccess();
        assertEquals("false", success);
    }

    @After
    public void Postconditions() {
        if (accessToken != null) {
            UserApiPojo.deleteUser(accessToken);
        }
    }
}
