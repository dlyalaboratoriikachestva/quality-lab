package terekhov.properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * Единая конечная точка для запросов свойств
 */

@Slf4j
public class PropertiesManager {

    /**
     * Уникальный инстанс менеджера
     */
    private static volatile PropertiesManager manager;

    /**
     * Коллекция свойств
     */
    Properties prop = new Properties();

    /**
     * Реализация ленивого синглтона
     *
     * @return инстанс менеджера
     */
    public static PropertiesManager getInstance() {
        PropertiesManager temp = manager;
        if (temp == null) {
            synchronized (PropertiesManager.class) {
                temp = manager;
                if (temp == null) {
                    manager = temp = new PropertiesManager();
                }
            }
        }
        return temp;
    }

    /**
     * Конструктор, в котором происходит инициализация свойств из корневого classpath
     */
    private PropertiesManager() {
        try {
            List<String> files = IOUtils.readLines(PropertiesManager.class.getClassLoader()
                    .getResourceAsStream(""), Charsets.UTF_8);

            for (String propFileName : files) {
                if (propFileName.endsWith(".properties"))
                    initSingleFile(propFileName);
            }
        } catch (IOException e) {
            log.error("невозможно получить список файлов с настройками");
        }
    }

    /**
     * Инициализация свойств из файла
     *
     * @param propFileName имя файла
     */
    private void initSingleFile(String propFileName) throws IOException {
        InputStream inputStream = null;

        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                Reader reader = new InputStreamReader(inputStream, "UTF-8");
                prop.load(reader);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * Возвращает значение свойства
     *
     * @param propName имя запрашиваемого свойства
     * @return значение свойства или null, если оно не было найдено
     */
    public String getProperties(String propName) {
        return prop.getProperty(propName);
    }
}
