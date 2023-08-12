package common;
import org.apache.commons.lang3.RandomStringUtils;
public class AuthUser
{
    private String name;
    private String password;
    private String email;

    public static AuthUser userRandomCreate()
    {
        AuthUser authUser = new AuthUser();
        String randomName = RandomStringUtils.randomAlphabetic(15);
        authUser.setEmail(randomName.toLowerCase() + "@yandex.ru");
        authUser.setName(randomName.toLowerCase());
        authUser.setPassword("Qwerty123");
        return authUser;
    }

    public AuthUser(String email, String password, String name)
    {
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public AuthUser() {}
    public String getName() {return name;}

    public void setName(String name) {this.name = name;}
    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}
    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

}
