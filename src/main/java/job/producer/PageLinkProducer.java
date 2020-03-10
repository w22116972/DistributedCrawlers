package job.producer;

import job.config.StringConfigFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jsoup.Jsoup;
import scala.Int;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PageLinkProducer {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    public static final String CLIENT_ID = "PageLinkProducer";
    public static final String TOPIC = "page-link";
//    private static final String TOTAL_JOB_NUMBER_SELECTOR = "#js-job-content > div:nth-child(33) > label > select";
//    private static final String HOME_PAGE = "https://www.104.com.tw/jobs/search/?ro=1&jobcat=2007001000&area=6001001000%2C6001002000&order=7&asc=0&page=1&mode=s&jobsource=2018indexpoc";
//    private static final int jobsPerPage = 20;
    private static final int TOTAL_PAGES = 150;

    private static KafkaProducer<String, String> producer = initProducer();

    public PageLinkProducer() {
        final Stream<String> pages = createTargetPages(TOTAL_PAGES);
        pages.forEach(page -> producer.send(new ProducerRecord<>(TOPIC, page)));
    }

    public static void main(String[] args) {

    }


    private static Properties initProducerConfig() {
        final StringConfigFactory configFactory = new StringConfigFactory();
        Properties producerConfig = configFactory.createProducerConfig();
        producerConfig.setProperty(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        producerConfig.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        return producerConfig;
    }

    private static KafkaProducer<String, String> initProducer() {
        return new KafkaProducer<>(initProducerConfig());
    }

//    private static String removeParenthese(String text) {
//        return text.substring(1, text.length()-1);
//    }

//    private Optional<Integer> getTotalPages() {
//        try {
//            final int totalJobNumber = Integer.parseInt(Jsoup.connect(HOME_PAGE).get().select(TOTAL_JOB_NUMBER_SELECTOR).text());
//            return  Optional.of((int) Math.ceil(totalJobNumber / jobsPerPage));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return Optional.empty();
//    }

    private Stream<String> createTargetPages(Integer totalPages) {
        return IntStream.range(0, totalPages).mapToObj(pageIndex -> buildTargetUrl(pageIndex));
    }

    private static String buildTargetUrl(Integer pageIndex) {
        return "https://www.104.com.tw/jobs/search/?ro=1&jobcat=2007001000&area=6001001000%2C6001002000&order=7&asc=0&page=" + pageIndex + "&mode=s&jobsource=2018indexpoc";
    }
}
