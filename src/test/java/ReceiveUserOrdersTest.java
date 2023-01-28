import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import orderpojos.OrderApiPojo;
import orderpojos.OrderPojo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import userpojos.*;

import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class ReceiveUserOrdersTest {
    private NewUserPojo newUser;
    private ExistingUserPojo oldUser;
    private String accessToken;
    private Response responseInfo;
    private String success;
    private Response responseCreate;
    private List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa75", "61c0c5a71d1f82001bdaaa75", "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa6c"); // список ингредиентов

    @Before
    public void Preconditions() {
        newUser = NewUserPojo.getRandomUser();
        responseCreate = UserApiPojo.createUser(newUser);
        accessToken = responseCreate.then().statusCode(SC_OK).extract().body().as(AuthPojo.class).getAccessToken();
        oldUser = new ExistingUserPojo(newUser.getEmail(), newUser.getPassword());
    }

    @Test
    @DisplayName("Получаем заказы авторизованного пользователя.")
    public void checkUserOrdersLogin() {
        UserApiPojo.loginUser(oldUser);
        OrderPojo order = new OrderPojo(ingredients);
        Response responseOrder = OrderApiPojo.createOrder(order, accessToken);
        responseInfo = OrderApiPojo.informationOrders(accessToken);
        // Проверяем код ответа и поле ответа "success"
        success = responseInfo.then().statusCode(SC_OK).extract().body().as(AuthPojo.class).getSuccess();
        assertEquals("true", success);
        // Проверяем, что у пользователя есть нужный заказ
        String stringOrder = responseOrder.getBody().asString();
        JsonPath jsonPath = new JsonPath(stringOrder);
        int expected = jsonPath.getInt("order.number");
        String stringInfo = responseInfo.getBody().asString();
        JsonPath jsonPathToo = new JsonPath(stringInfo);
        int actual = jsonPathToo.getInt("orders[0].number");
        assertEquals(expected, actual);
        // Проверяем, что вернулся 1 заказ
        int ordersSize = jsonPathToo.getList("orders").size();
        assertEquals(1, ordersSize);
    }

    @Test
    @DisplayName("Получаем заказы неавторизованного пользователя.")
    public void checkUserOrdersLogout() {
        responseInfo = OrderApiPojo.informationOrders("");
        // Проверяем код ответа и поле ответа "success"
        success = responseInfo.then().statusCode(SC_UNAUTHORIZED).extract().body().as(ErrorsPojo.class).getSuccess();
        assertEquals("false", success);
    }

    @After
    public void Postconditions() {
        if (accessToken != null) {
            UserApiPojo.deleteUser(accessToken);
        }
    }
}
