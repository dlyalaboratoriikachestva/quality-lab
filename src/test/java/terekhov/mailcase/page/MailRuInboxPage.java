package terekhov.mailcase.page;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * Created by Aris on 02.11.2016.
 */
@Slf4j
public class MailRuInboxPage {
    @FindBy(xpath = "//div[@class='b-sticky']//a[@data-name='compose'][@data-shortcut='n']")
    private WebElement writeMessageButton;
    @FindBy(xpath = "//div[@class='b-sticky']//div[@data-name='send']")
    private WebElement sendMessageButton;
    @FindBy(xpath = "//textarea[@data-original-name='To']")
    private WebElement recepientInput;
    @FindBy(xpath = "//input[@name='Subject']")
    private WebElement headerInput;
    @FindBy(xpath = "//body[@id='tinymce']")
    private WebElement mainTextInput;
    @FindBy(xpath = "//td/iframe")
    private WebElement tinyMceEditorFrame;
    private WebDriver driver;
    /**
     * Надежнее использовать js эмуляцию нажатия на элемент (из-за возможных наложений элементов)
     */
    private JavascriptExecutor executor;


    /**
     * Метод, реализующий работу с внутренним интерфейсом почты
     *
     * @param mailRecepient  адрес получателя письма
     * @param mailHeader    тема письма
     * @param mailText  текст письма
     */
    public boolean sendMail(String mailRecepient, String mailHeader, String mailText)
    {
        log.info("Кликаем кнопку Написать письмо");
        executor.executeScript("arguments[0].click();", writeMessageButton);
        log.info("Заполняем поле Кому");
        recepientInput.sendKeys(mailRecepient);
        log.info("Заполняем поле Тема");
        headerInput.sendKeys(mailHeader);
        log.info("Заполняем текст письма");
        executor.executeScript("tinyMCE.activeEditor.setContent('"+mailText+"')");
        //driver.switchTo().frame(tinyMceEditorFrame);
        //mainTextInput.sendKeys(mailText);
        //driver.switchTo().defaultContent();
        log.info("Кликаем по кнопке Отправить");
        executor.executeScript("arguments[0].click();", sendMessageButton);
        List<WebElement> messageSentElements = driver.findElements(By.xpath("//div[contains(@class,'message-sent')]"));
        if(messageSentElements.size()==0){return true;}else{return false;}

    }
    /**
     * Инициализатор класса
     *
     * @param driver   инстанс Selenium WebDriver
     */
    public MailRuInboxPage(WebDriver driver) throws Exception {
        executor = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }
}
