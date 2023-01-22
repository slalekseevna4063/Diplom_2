package UsersPojos;

import org.apache.commons.lang3.RandomStringUtils;

public class ExistingUserPojo {
    private String email;

    private String password;

    public ExistingUserPojo(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public ExistingUserPojo(){}

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

    public void setRandomPassword() {
        this.password = RandomStringUtils.randomAlphabetic(10);
    }

    public void setRandomEmail() {
        this.email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
    }
}
