package ecommerce.crawler;

import ecommerce.WebDriverFactory;
import ecommerce.model.Category;
import ecommerce.producer.RutenProducerFactory;
import ecommerce.selector.RutenSelector;
import org.apache.kafka.clients.producer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.SerializationException;
import org.jsoup.nodes.Element;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import util.ConfigurationFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public abstract class BaseRutenCrawler implements Crawler {

    protected static final Logger logger = LogManager.getLogger(BaseRutenCrawler.class);
    protected static final String RUTEN_HOMEPAGE_CONFIG = "ecommerce.ruten.homepage";
    protected static final Properties CONFIG = ConfigurationFactory.loadConfig();
    protected static final String RUTEN_CATEGORY_INPUT = "ruten-category-input";
    protected static final String RUTEN_CATEGORY_OUTPUT = "ruten-category-output";

    protected static final String HOMEPAGE = CONFIG.getProperty(RUTEN_HOMEPAGE_CONFIG);

    protected static final String HOMEPAGE_SELECTOR = RutenSelector.getHomepageSelector();
    protected static final String CATEGORY_SELECTOR = RutenSelector.getCategorySelector();

//    private static ConcurrentHashMap<String, String> categoryIdToName = new ConcurrentHashMap<>();

    protected final Producer<String, Category> producer = new RutenProducerFactory().createProducer();

    protected static final String ADULT_URL = "http://www.ruten.com.tw/category/main?0025";

    abstract public void run();
    abstract public void close();


    protected static boolean isSubCategoryValid(Element element) {
        return !element.text().contains("@");
    }

    protected static String extractCategoryName(Element element) {
        return element.tagName("span").text();
    }

    protected static String extractUrl(Element element) {
        return element.absUrl("href");
    }

    protected static String extractCategoryId(String url) {
        return url.substring(url.lastIndexOf("?") + 1);
    }

    protected static Long extractCount(String text) {
        return Long.parseLong(text.substring(text.lastIndexOf("(") + 1, text.lastIndexOf(")")));
    }

    protected void produceAdultCountBySelenium() {
        WebDriverFactory factory = new WebDriverFactory();
        final WebDriver driver = factory.createDriver();

        driver.get(ADULT_URL);
        bypassAdultCheck(driver);
        Long adultCount = getCategoryCountByDriver(driver);

        producer.send(new ProducerRecord<>(RUTEN_CATEGORY_INPUT, new Category("成人專區", adultCount)));
//        records.add(new RutenRecord("成人專區", adultCount));
        logger.info("成人專區(" + adultCount + ") ... ok");
        System.out.println("成人專區(" + adultCount + ") ... ok");

        driver.close();
        driver.quit();

    }

    protected void bypassAdultCheck(WebDriver driver) {
        logger.debug("bypassing adult check...");
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.findElement(By.id("checkAdult")).click();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        final Alert alert = driver.switchTo().alert();
        alert.accept();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    protected Long getCategoryCountByDriver(WebDriver driver) {
        return driver.findElements(By.className("rt-text-notice")).stream().mapToLong(e -> extractCount(e.getText())).sum();
    }

    // for async send message to topic
    protected class CategoryProducerCallback implements Callback {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                logger.error(e);
            }
        }
    }

    protected void asyncSendToTopic(ProducerRecord<String, Category> record) {
        producer.send(record, new CategoryProducerCallback());
    }

    protected void syncSendToTopic(ProducerRecord<String, Category> record) {
        try {
            producer.send(record).get();
        } catch (InterruptedException e) {
            logger.error("Sending thread is interrupted", e);
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    protected void fireAndForgetSendToTopic(ProducerRecord<String, Category> record) {
        try {
            producer.send(record);
        } catch (SerializationException e) {
            logger.error("Failed to serialize message", e);
        } catch (BufferExhaustedException e) {
            logger.error("Buffer is full", e);
        }
    }

}
