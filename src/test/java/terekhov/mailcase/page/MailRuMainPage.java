package terekhov.mailcase.page;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * Класс, оборачивающий главную страницу Mail.ru
 * Created by Aris on 02.11.2016.
 */
@Slf4j
public class MailRuMainPage {
    @FindBy(xpath = "//input[@name='Login']")
    private WebElement loginInput;
    @FindBy(xpath = "//input[@name='Password']")
    private WebElement passwordInput;
    @FindBy(xpath = "//input[@type='submit'][contains(@id,'auth')]")
    private WebElement loginButton;

    private WebDriver driver;
    private String screensFolder;

    /**
     * Надежнее использовать js эмуляцию нажатия на элемент (из-за возможных наложений элементов)
     */
    private JavascriptExecutor executor;

    /**
     * Метод, реализующий вход в аккаунт пользователя
     *
     * @param mailruURL       URL главной страницы Mail.ru
     * @param mailruLogin     логин пользователя
     * @param mailruPassword  пароль пользователя
     */
    public boolean logIn (String mailruURL, String mailruLogin, String mailruPassword)
    {
        log.info("Переходим на главную страницу Mail.ru");
        driver.navigate().to(mailruURL);
        log.info("Вводим логин");
        loginInput.sendKeys(mailruLogin);
        log.info("Вводим пароль");
        passwordInput.sendKeys(mailruPassword);
        log.info("Кликаем по кнопке Войти");
        executor.executeScript("arguments[0].click();",loginButton);
        if (driver.getCurrentUrl().contains("/inbox/")){return true;}else{return false;}
    }


    /**
     * Инициализатор класса
     *
     * @param driver   инстанс Selenium WebDriver
     * @param folder   папка для хранения скриншотов, в случае ошибки
     */
    public MailRuMainPage(WebDriver driver, String folder) throws Exception {
        executor = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }


}
