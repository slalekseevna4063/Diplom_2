import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import userspojos.*;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class ChangeCredentialsTest {
    public ExistingUserPojo oldUser;
    public NewUserPojo newUser;
    public String accessToken;
    public Response responseCreate;
    public String message = "You should be authorised";
    public Response responseLogin;
    public Response responseInfo;
    public String success;
    public String messageError;

    @Before
    public void Preconditions() {
        newUser = NewUserPojo.getRandomUser();
        responseCreate = UserApiPojo.createUser(newUser);
        oldUser = new ExistingUserPojo(newUser.getEmail(), newUser.getPassword());
        accessToken = responseCreate.then().statusCode(SC_OK).extract().body().as(AuthPojo.class).getAccessToken();
    }

    @Test
    @DisplayName("Изменяем имя уже авторизованного пользователя.")
    public void checkChangeName() {
        responseLogin = UserApiPojo.loginUser(oldUser);
        newUser.setRandomName();
        responseInfo = UserApiPojo.changeInformation(accessToken, newUser);
        success = responseInfo.then().statusCode(SC_OK).extract().body().as(AuthPojo.class).getSuccess();
        assertEquals("true", success);
    }

    @Test
    @DisplayName("Изменяем почту уже авторизованного пользователя.")
    public void checkChangeEmail() {
        responseLogin = UserApiPojo.loginUser(oldUser);
        newUser.setRandomEmail();
        responseInfo = UserApiPojo.changeInformation(accessToken, newUser);
        success = responseInfo.then().statusCode(SC_OK).extract().body().as(AuthPojo.class).getSuccess();
        assertEquals("true", success);
    }

    @Test
    @DisplayName("Изменяем пароль уже авторизованного пользователя.")
    public void checkChangePassword() {
        responseLogin = UserApiPojo.loginUser(oldUser);
        newUser.setRandomPassword();
        responseInfo = UserApiPojo.changeInformation(accessToken, newUser);
        success = responseInfo.then().statusCode(SC_OK).extract().body().as(AuthPojo.class).getSuccess();
        assertEquals("true", success);
    }

    @Test
    @DisplayName("Изменяем имя неавторизованного пользователя.")
    public void checkWrongChangeName() {
        newUser.setRandomName();
        responseInfo = UserApiPojo.changeInformation("", newUser);
        success = responseInfo.then().statusCode(SC_UNAUTHORIZED).extract().body().as(ErrorsPojo.class).getSuccess();
        messageError = responseInfo.then().statusCode(SC_UNAUTHORIZED).extract().body().as(ErrorsPojo.class).getMessage();
        assertEquals("false", success);
        assertEquals(message, messageError);
    }

    @Test
    @DisplayName("Изменяем почту неавторизованного пользователя.")
    public void checkWrongChangeEmail() {
        newUser.setRandomEmail();
        responseInfo = UserApiPojo.changeInformation("", newUser);
        success = responseInfo.then().statusCode(SC_UNAUTHORIZED).extract().body().as(ErrorsPojo.class).getSuccess();
        messageError = responseInfo.then().statusCode(SC_UNAUTHORIZED).extract().body().as(ErrorsPojo.class).getMessage();
        assertEquals("false", success);
        assertEquals(message, messageError);
    }

    @Test
    @DisplayName("Изменяем пароль неавторизованного пользователя.")
    public void checkWrongChangePassword() {
        newUser.setRandomPassword();
        responseInfo = UserApiPojo.changeInformation("", newUser);
        success = responseInfo.then().statusCode(SC_UNAUTHORIZED).extract().body().as(ErrorsPojo.class).getSuccess();
        messageError = responseInfo.then().statusCode(SC_UNAUTHORIZED).extract().body().as(ErrorsPojo.class).getMessage();
        assertEquals("false", success);
        assertEquals(message, messageError);
    }

    @After
    public void Postconditions() {
        if (accessToken != null) {
            UserApiPojo.deleteUser(accessToken);
        }
    }
}
