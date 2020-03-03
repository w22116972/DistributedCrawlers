package ecommerce.crawler;

import ecommerce.model.Category;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class AsyncRutenCrawler extends AbstractRutenCrawler {

    public AsyncRutenCrawler() {
        super();
    }

    @Override
    public void run() {
        CompletableFuture.runAsync(() -> produceAdultCountBySelenium());
        createHomeElementStream().forEach(element ->
                CompletableFuture.runAsync(() ->
                        produceCategoryCount(element)));
    }

    @Override
    public void close() {
        producer.flush();
        producer.close();
    }

    private void produceCategoryCount(Element homepageElement) {
        final String categoryName = extractCategoryName(homepageElement);
        final Stream<Element> categoryStream = createCategoryStream(homepageElement);
        final Long categoryCount = categoryStream.filter(AbstractRutenCrawler::isSubCategoryValid)
                .map(element -> element.text())
                .mapToLong(AbstractRutenCrawler::extractCount)
                .reduce(0, Long::sum);
        producer.send(new ProducerRecord<>(RUTEN_CATEGORY_INPUT, new Category(categoryName, categoryCount)));
        logger.info((categoryName + "(" + categoryCount + ") ... ok"));
    }

    private Stream<Element> createHomeElementStream() {
        try {
            return Jsoup.connect(HOMEPAGE).get().select(HOMEPAGE_SELECTOR).parallelStream();
        } catch (IOException e) {
            logger.error("createHomeElementStream() failed...", e);
            e.printStackTrace();
        }
        return Stream.empty();
    }

    private Stream<Element> createCategoryStream(Element homepageElement) {
        try {
            Jsoup.connect(extractUrl(homepageElement)).get().select(CATEGORY_SELECTOR).parallelStream();
        } catch (IOException e) {
            logger.error("createCategoryStream() failed...", e);
            e.printStackTrace();
        }
        return Stream.empty();
    }

}
