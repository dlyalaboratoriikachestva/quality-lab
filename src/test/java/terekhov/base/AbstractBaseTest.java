package terekhov.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import terekhov.properties.PropertiesManager;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Абстрактный класс, позволяющий единообразно подключаться к selenium и корректно завершать работу.
 * Является базовым для всех текущих тестов, использующий selenium.
 */
@Slf4j
public abstract class AbstractBaseTest {

    private static final String DEFAULT_BROWSER = BrowserType.CHROME;

    private static final int DEFAULT_WIDTH = 1024;
    private static final int DEFAULT_HEIGHT = 800;

    private static final int DEFAULT_PAGE_LOAD_TIMEOUT = 20;
    private static final int DEFAULT_IMPLICITLY_TIMEOUT = 20;
    private static final int DEFAULT_SCRIPT_TIMEOUT = 10;

    protected WebDriver driver = null;


    @Before
    public void init() throws IOException {

        //log.info("\n-------------------------------------------------------");

        int width = DEFAULT_WIDTH;
        int height = DEFAULT_HEIGHT;

        try {

            //log.info("Hub is: " + PropertiesManager.getInstance().getProperties("hub"));

            String browserProp;
            /* Поиск значения типа браузера */
            try {
                browserProp = PropertiesManager.getInstance().getProperties("browser");
                if (!browserProp.equals(BrowserType.CHROME) && !browserProp.equals(BrowserType.FIREFOX)) {
                    browserProp = DEFAULT_BROWSER;
                }
            } catch (Exception e) {
                //log.warn("Browser was not set");
                browserProp = DEFAULT_BROWSER;
            }

            /* Поиск значения ширины окна браузера */
            try {
                width = Integer.parseInt(PropertiesManager.getInstance().getProperties("window.size.width"));
            } catch (Exception e) {
                //log.warn("Width was not set");
            }

            /* Поиск значения высоты окна браузера */
            try {
                height = Integer.parseInt(PropertiesManager.getInstance().getProperties("window.size.height"));
            } catch (Exception e) {
                //log.warn("Height was not set");
            }

            DesiredCapabilities capability = null;
            if (browserProp.equals(BrowserType.FIREFOX)) {
                capability = DesiredCapabilities.firefox();
            }
            if (browserProp.equals(BrowserType.CHROME)) {
                capability = DesiredCapabilities.chrome();
            }

            if (capability == null) {
                throw new Exception("Requested browser type is not supported");
            }

            /* Инициализация параметров запуска Chrome */
            ChromeOptions options = new ChromeOptions();

            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_setting_values.notifications", 2);
            options.setExperimentalOption("prefs", prefs);
            options.setExperimentalOption("excludeSwitches", Arrays.asList("test-type"));

            LoggingPreferences logs = new LoggingPreferences();
            logs.enable(LogType.DRIVER, Level.ALL);

            options.addArguments("--disable-extensions");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-impl-side-painting");
            //capability.setCapability("marionette", true);

            capability.setCapability("chrome.switches",
                    Arrays.asList("--verbose"));
            capability.setCapability(ChromeOptions.CAPABILITY, options);
            capability.setCapability(CapabilityType.LOGGING_PREFS, logs);

            //log.info("Подключение к удаленному хабу");
            driver = new RemoteWebDriver(new URL(PropertiesManager.getInstance().getProperties("hub").toString()), capability);

            log.info("Настройка размеров окна");
            driver.manage().window().setSize(new Dimension(width, height));

            log.info("Инициализация драйвера завершена");

        } catch (Exception e) {
            //log.error("Error: " + e.getMessage());
        }

        if (driver == null) {
            /* Попытка запуститься локально в случае ошибки */
            driver = new FirefoxDriver();
            driver.manage().window().setSize(new Dimension(width, height));
        }

        /* Установка дефолтный таймингов */
        driver.manage().timeouts().pageLoadTimeout(DEFAULT_PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(DEFAULT_IMPLICITLY_TIMEOUT, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(DEFAULT_SCRIPT_TIMEOUT, TimeUnit.SECONDS);
    }


    @After
    public void close() {
        driver.quit();
        log.info("Завершение сеанса браузера");
    }
}
