import UsersPojos.AuthPojo;
import UsersPojos.ErrorsPojo;
import UsersPojos.NewUserPojo;
import UsersPojos.UserApiPojo;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;

public class CreateUsersTest {
    NewUserPojo user;
    String accessToken;
    String success;
    Response responseCreate;

    @Before
    public void createNewUser() {
        user = NewUserPojo.getRandomUser();
    }


    @Test
    @DisplayName("Создаём уникального пользователя.")
    public void checkCreateUser() {
        responseCreate = UserApiPojo.createUser(user);
        accessToken = responseCreate.then().statusCode(SC_OK).extract().body().as(AuthPojo.class).getAccessToken();
        // Проверяем код ответа и поле ответа "success"
        success = responseCreate.then().statusCode(SC_OK).extract().body().as(AuthPojo.class).getSuccess();
        assertEquals("true", success);
    }

    @Test
    @DisplayName("Создаём пользователя, с данными, используемыми при регистрации ранее.")
    public void checkWrongCreateUser() {
        responseCreate = UserApiPojo.createUser(user);
        accessToken = responseCreate.then().statusCode(SC_OK).extract().body().as(AuthPojo.class).getAccessToken();
        responseCreate = UserApiPojo.createUser(user);
        // Проверяем код ответа и поле ответа "success"
        success = responseCreate.then().statusCode(SC_FORBIDDEN).extract().body().as(ErrorsPojo.class).getSuccess();
        assertEquals("false", success);
    }

    @Test
    @DisplayName("Создаём пользователя без почты.")
    public void checkWrongEmail() {
        user.setEmail(null);
        responseCreate = UserApiPojo.createUser(user);
        // Проверяем код ответа и поле ответа "success"
        success = responseCreate.then().statusCode(SC_FORBIDDEN).extract().body().as(ErrorsPojo.class).getSuccess();
        assertEquals("false", success);
    }

    @Test
    @DisplayName("Создаём пользователя без пароля.")
    public void checkWrongPassword() {
        user.setPassword(null);
        responseCreate = UserApiPojo.createUser(user);
        // Проверяем код ответа и поле ответа "success"
        success = responseCreate.then().statusCode(SC_FORBIDDEN).extract().body().as(ErrorsPojo.class).getSuccess();
        assertEquals("false", success);
    }

    @Test
    @DisplayName("Создаём пользователя без имени.")
    public void checkWrongName() {
        user.setName(null);
        responseCreate = UserApiPojo.createUser(user);
        success = responseCreate.then().statusCode(SC_FORBIDDEN).extract().body().as(ErrorsPojo.class).getSuccess();
        assertEquals("false", success);
    }
    @After
    public void Postconditions() {
        if (accessToken != null) {
            UserApiPojo.deleteUser(accessToken);
        }
    }
}
