package terekhov.mailcase;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
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
public class MailRuSendingTest extends AbstractBaseTest {
    private String mailRecepient = null;
    private String mailHeader = null;
    private String mailText = null;
    private String mailruURL = null;
    private String mailruPassword = null;
    private String mailruLogin = null;

    @Before
    public void initProperties() {
        mailRecepient = PropertiesManager.getInstance().getProperties("mail.recipient");
        mailHeader = PropertiesManager.getInstance().getProperties("mail.header");
        mailText = PropertiesManager.getInstance().getProperties("mail.text");
        mailruURL = PropertiesManager.getInstance().getProperties("mailru.url");
        mailruPassword = PropertiesManager.getInstance().getProperties("mailru.pass");
        mailruLogin = PropertiesManager.getInstance().getProperties("mailru.user");
    }

    /**
     * Тест, проверяющий отправку письма на Mail.ru
     *
     * @throws Exception
     */
    @Test
    public void sendingTest() throws Exception {
        log.info("Заходим на Mail.ru и отправляем письмо");
        MailRuMainPage mailRuMainPage = new MailRuMainPage(driver);
        Assert.assertTrue("Не удалось залогиниться", mailRuMainPage.logIn(mailruURL, mailruLogin, mailruPassword));
        MailRuInboxPage mailRuInboxPage = new MailRuInboxPage(driver);
        Assert.assertTrue("Не удалось отправить письмо", mailRuInboxPage.sendMail(mailRecepient, mailHeader, mailText));

    }
}
