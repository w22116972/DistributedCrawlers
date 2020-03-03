package ecommerce.crawler;

import ecommerce.model.Category;
import ecommerce.producer.RutenProducerFactory;
import org.apache.kafka.clients.producer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Optional;

public final class RutenCrawler extends AbstractRutenCrawler {

    private static final Logger logger = LogManager.getLogger(RutenCrawler.class);
    protected final Producer<String, Category> producer = new RutenProducerFactory().createProducer();

    public RutenCrawler() {
        super();
    }

    @Override
    public void run() {
        try {
            Document homepageDoc = Jsoup.connect(HOMEPAGE).get();
            produceCategoryCount(producer, homepageDoc);
            produceAdultCountBySelenium();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void produceCategoryCount(Producer<String, Category> producer, Document homepageDoc) throws IOException {
        for (Element homepageElement: homepageDoc.select(HOMEPAGE_SELECTOR)) {
            final String category = extractCategoryName(homepageElement);
            final String url = extractUrl(homepageElement);
            Document categoryDoc = Optional.of(Jsoup.connect(url).get()).orElseThrow(() -> new IOException(""));
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





    @Override
    public void close() {
        producer.flush();
        producer.close();
    }
}
