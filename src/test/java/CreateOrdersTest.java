import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import orderspojos.OrderApiPojo;
import orderspojos.OrderPojo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import userspojos.*;

import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class CreateOrdersTest {
    public NewUserPojo newUser;
    public ExistingUserPojo oldUser;
    public String accessToken;
    public Response responseOrder;
    public Response responseCreate;
    public Response responseLogin;
    public String success;
    public OrderPojo order;
    public List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa7a", "61c0c5a71d1f82001bdaaa6d");

    @Before
    public void Preconditions() {
        newUser = NewUserPojo.getRandomUser();
        responseCreate = UserApiPojo.createUser(newUser);
        accessToken = responseCreate.then().statusCode(SC_OK).extract().body().as(AuthPojo.class).getAccessToken();
        oldUser = new ExistingUserPojo(newUser.getEmail(), newUser.getPassword());
    }


    @Test
    @DisplayName("Создаем заказ через авторизованного пользователя с ингредиентами.")
    public void checkOrderLoginWithIngredients() {
        responseLogin = UserApiPojo.loginUser(oldUser);
        order = new OrderPojo(ingredients);
        responseOrder = OrderApiPojo.createOrder(order, accessToken);
        success = responseOrder.then().statusCode(SC_OK).extract().body().as(AuthPojo.class).getSuccess();
        assertEquals("true", success);
    }

    @Test
    @DisplayName("Создаем заказ через неавторизованного пользователя с ингредиентами.")
    public void checkOrderLogout() {
        order = new OrderPojo(ingredients);
        responseOrder = OrderApiPojo.createOrder(order, accessToken);
        success = responseOrder.then().statusCode(SC_OK).extract().body().as(AuthPojo.class).getSuccess();
        assertEquals("true", success);
    }

    @Test
    @DisplayName("Создаем заказ через авторизованного пользователя без ингредиентов.")
    public void checkOrderWithoutIngredients() {
        responseLogin = UserApiPojo.loginUser(oldUser);
        order = new OrderPojo(null);
        responseOrder = OrderApiPojo.createOrder(order, accessToken);
        success = responseOrder.then().statusCode(SC_BAD_REQUEST).extract().body().as(ErrorsPojo.class).getSuccess();
        assertEquals("false", success);
    }

    @Test
    @DisplayName("Создаем заказ через авторизованного пользователя с невалидным id ингредиента.")
    public void checkOrderWithWrongIngredients() {
        responseLogin = UserApiPojo.loginUser(oldUser);
        order = new OrderPojo(List.of("61c0c5a71d1f82001bda"));
        responseOrder = OrderApiPojo.createOrder(order, accessToken);
        responseOrder.then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void Postconditions() {
        if (accessToken != null) {
            UserApiPojo.deleteUser(accessToken);
        }
    }
}

