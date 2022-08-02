package ru.zychkov.testissue;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

public class IssueTest {

    @BeforeEach
    void initDriver() throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
//        options.addArguments("--start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");

        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", false,
                "sessionTimeout", "240s"
        ));
//        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        final String url = "http://localhost:4444/wd/hub";
        WebDriver driver = new RemoteWebDriver(URI.create(url).toURL(), capabilities);
        driver.manage().window().setSize(new Dimension(1920,1024));
        WebDriverRunner.setWebDriver(driver);
    }

    @AfterEach
    public void stopDriver() {
        Optional.of(WebDriverRunner.getWebDriver()).ifPresent(WebDriver::quit);
    }

    @Test
    @DisplayName("Открываем сщуствующую Issue")
    public void testIssue() {
        step("Открываем главную страницу", () -> {
            open("https://github.com");
        });
        step("Открываем страницу с репозиторием", () -> {
            $x("//*[contains(@class, 'header-search-input')]").click();
            $x("//*[contains(@class, 'header-search-input')]").sendKeys("eroshenkoam/allure-example");
            $x("//*[contains(@class, 'header-search-input')]").submit();
            $x("//a[@href='/eroshenkoam/allure-example']").click();
        });
        step("Открываем страницу с задачами в репозитории", () -> {
            $x("//a[contains(@data-selected-links, 'repo_issues')]").click();
        });
        step("Проверяем title", () -> {
            $x("//a[@id='issue_76_link']").click();
            $x("//h1[contains(@class, 'gh-header-title')]").should(Condition.text("С Новым Годом (2022)"));
        });

    }

}
