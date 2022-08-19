package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import ru.netology.testmode.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

public class AuthTest {

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

//    @AfterEach
//    void setDown() {
//        closeWindow();
//    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("input[name='login']").setValue(registeredUser.getLogin());
        $("input[name='password']").setValue(registeredUser.getPassword());
        $(".button__text").click();
        $x("//h2[contains(text(), 'Личный кабинет')]").shouldHave(Condition.exactText("Личный кабинет"),
                Duration.ofSeconds(15));

    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("input[name='login']").setValue(notRegisteredUser.getLogin());
        $("input[name='password']").setValue(notRegisteredUser.getPassword());
        $(".button__text").click();
        $("[class='notification__content']").shouldHave(Condition.exactText(("Ошибка! Неверно указан логин или пароль")),
                Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("input[name='login']").setValue(blockedUser.getLogin());
        $("input[name='password']").setValue(blockedUser.getPassword());
        $(".button__text").click();
        $("[class='notification__content']").shouldHave(Condition.exactText(("Ошибка! Пользователь заблокирован")),
                Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("input[name='login']").setValue(wrongLogin);
        $("input[name='password']").setValue(registeredUser.getPassword());
        $(".button__text").click();
        $("[class='notification__content']").shouldHave(Condition.exactText(("Ошибка! Неверно указан логин или пароль")),
                Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("input[name='login']").setValue(registeredUser.getLogin());
        $("input[name='password']").setValue(wrongPassword);
        $(".button__text").click();
        $("[class='notification__content']").shouldHave(Condition.exactText(("Ошибка! Неверно указан логин или пароль")),
                Duration.ofSeconds(15));
    }

}
