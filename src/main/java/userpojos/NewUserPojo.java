package userpojos;

import org.apache.commons.lang3.RandomStringUtils;

public class NewUserPojo {
    private String email;
    private String password;
    private String name;

    public NewUserPojo(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public NewUserPojo() {
    }

    public static NewUserPojo getRandomUser() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(8);
        return new NewUserPojo(email, password, name);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRandomPassword() {
        this.password = RandomStringUtils.randomAlphabetic(10);
    }

    public void setRandomEmail() {
        this.email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
    }

    public void setRandomName() {
        this.name = RandomStringUtils.randomAlphabetic(8);
    }
}
