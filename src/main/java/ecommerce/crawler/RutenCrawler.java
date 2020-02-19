package ecommerce.crawler;

import ecommerce.WebDriverFactory;
import ecommerce.model.Category;
import ecommerce.producer.RutenProducerFactory;
import ecommerce.selector.RutenSelector;
import org.apache.kafka.clients.producer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.SerializationException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import util.ConfigurationFactory;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class RutenCrawler implements Crawler {

    private static final Logger logger = LogManager.getLogger(RutenCrawler.class);
    private static final String RUTEN_HOMEPAGE_CONFIG = "ecommerce.ruten.homepage";
    private static final Properties CONFIG = ConfigurationFactory.loadConfig();
    private static final String RUTEN_CATEGORY_INPUT = "ruten-category-input";
    private static final String RUTEN_CATEGORY_OUTPUT = "ruten-category-output";

    private static final String HOMEPAGE = CONFIG.getProperty(RUTEN_HOMEPAGE_CONFIG);

    private static final String HOMEPAGE_SELECTOR = RutenSelector.getHomepageSelector();
    private static final String CATEGORY_SELECTOR = RutenSelector.getCategorySelector();

//    private static ConcurrentHashMap<String, String> categoryIdToName = new ConcurrentHashMap<>();

    private final Producer<String, Category> producer = new RutenProducerFactory().createProducer();

    private static final String ADULT_URL = "http://www.ruten.com.tw/category/main?0025";

    @Override
    public void run() {
        try {
            // v2
            Jsoup.connect(HOMEPAGE).get().select(HOMEPAGE_SELECTOR).stream().parallel().forEach(element ->

                    );



            // v1
            Document homepageDoc = Jsoup.connect(HOMEPAGE).get();
            produceCategoryCount(producer, homepageDoc);
            produceAdultCountBySelenium();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // for async send message to topic
    private class CategoryProducerCallback implements Callback {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                logger.error(e);
            }
        }
    }

    private void asyncSendToTopic(ProducerRecord<String, Category> record) {
        producer.send(record, new CategoryProducerCallback());
    }

    private void syncSendToTopic(ProducerRecord<String, Category> record) {
        try {
            producer.send(record).get();
        } catch (InterruptedException e) {
            logger.error("Sending thread is interrupted", e);
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void fireAndForgetSendToTopic(ProducerRecord<String, Category> record) {
        try {
            producer.send(record);
        } catch (SerializationException e) {
            logger.error("Failed to serialize message", e);
        } catch (BufferExhaustedException e) {
            logger.error("Buffer is full", e);
        }
    }


    private void produceCategoryCount(Producer<String, Category> producer, Document homepageDoc) throws IOException {
//        final List<Category> categoryList = new ArrayList<>();
        for (Element homepageElement: homepageDoc.select(HOMEPAGE_SELECTOR)) {
            final String category = extractCategoryName(homepageElement);
            final String url = extractUrl(homepageElement);
//            categoryIdToName.put(extractCategoryId(url), category);
            Document categoryDoc = Optional.of(Jsoup.connect(url).get()).orElseThrow(() -> new IOException(""));
//            try {
//                categoryDoc = Jsoup.connect(url).get();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            long count = 0;
            for (Element subCategoryElement:  categoryDoc.select(CATEGORY_SELECTOR)) {
                if (isSubCategoryValid(subCategoryElement)) {
                    count += extractCount(subCategoryElement.text());
                }
            }
            if (count != 0) {
                producer.send(new ProducerRecord<>(RUTEN_CATEGORY_INPUT, new Category(category, count)));
//                categoryList.add(new Category(category, count));
                logger.info((category + "(" + count + ") ... ok"));
                System.out.println(category + "(" + count + ") ... ok");
            }
        }
//        return categoryList;
    }

    private static boolean isSubCategoryValid(Element element) {
        return !element.text().contains("@");
    }

    private static String extractCategoryName(Element element) {
        return element.tagName("span").text();
    }

    private static String extractUrl(Element element) {
        return element.absUrl("href");
    }

    private static String extractCategoryId(String url) {
        return url.substring(url.lastIndexOf("?") + 1);
    }

    private static long extractCount(String text) {

        return Long.parseLong(text.substring(text.lastIndexOf("(") + 1, text.lastIndexOf(")")));
    }

    private void produceAdultCountBySelenium() {
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

    private void bypassAdultCheck(WebDriver driver) {
        logger.debug("bypassing adult check...");
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        driver.findElement(By.id("checkAdult")).click();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        final Alert alert = driver.switchTo().alert();
        alert.accept();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    private Long getCategoryCountByDriver(WebDriver driver) {
        return driver.findElements(By.className("rt-text-notice")).stream().mapToLong(e -> extractCount(e.getText())).sum();
    }



    @Override
    public void close() {

    }
}
