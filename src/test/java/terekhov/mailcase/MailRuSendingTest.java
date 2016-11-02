package terekhov.mailcase;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import terekhov.base.AbstractBaseTest;
import terekhov.mailcase.page.MailRuInboxPage;
import terekhov.mailcase.page.MailRuMainPage;
import terekhov.properties.PropertiesManager;

/**
 * Тест, осуществляющий отправку электронной почты через веб-интерфейс Mail.ru
 * Created by Aris on 02.11.2016.
 */
@Slf4j
public class MailRuSendingTest extends AbstractBaseTest{
    @Getter
    private String mailRecepient = null;
    @Getter
    private String mailHeader = null;
    @Getter
    private String mailText = null;
    @Getter
    private String mailruURL = null;
    @Getter
    private String mailruPassword = null;
    @Getter
    private String mailruLogin = null;
    @Getter
    private String screenshotFolder = null;

    @Before
    public void initProperties() {
        mailRecepient = PropertiesManager.getInstance().getProperties("mail.recipient").toString();
        mailHeader = PropertiesManager.getInstance().getProperties("mail.header").toString();
        mailText = PropertiesManager.getInstance().getProperties("mail.text").toString();
        mailruURL = PropertiesManager.getInstance().getProperties("mailru.url").toString();
        mailruPassword = PropertiesManager.getInstance().getProperties("mailru.pass").toString();
        mailruLogin = PropertiesManager.getInstance().getProperties("mailru.user").toString();
        screenshotFolder = PropertiesManager.getInstance().getProperties("folder.screenshots").toString();
    }

    /**
     * Тест инициализирующий работу по странице новостей
     *
     * @throws Exception
     */
    @Test
    public void checkNewsSearch() throws Exception {
        log.info("Проверяем глобальный поиск по новостям");
        MailRuMainPage mailRuMainPage = new MailRuMainPage(driver, screenshotFolder);
        mailRuMainPage.logIn(mailruURL, mailruLogin, mailruPassword);
        MailRuInboxPage mailRuInboxPage = new MailRuInboxPage(driver);
        mailRuInboxPage.sendMail(mailRecepient, mailHeader, mailText);

    }
}
