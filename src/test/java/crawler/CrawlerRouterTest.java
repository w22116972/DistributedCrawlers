package crawler;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import model.CrawlerRouterConfig;
import org.jsoup.Jsoup;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class CrawlerRouterTest {
    @ClassRule
    public static final TestKitJunitResource testKitResource = new TestKitJunitResource();

    private static TestProbe<CrawlerManager.StartRouterResponse> probe;
    private static ActorRef<CrawlerRouter.Command> crawlerRouter;
    private final static String targetUrl1 = "https://www.google.com/";
    private final static String targetUrl2 = "https://www.apple.com/";
    private final static List<String> targetUrls = Stream.of(targetUrl1, targetUrl2).collect(Collectors.toList());
    private final static CrawlerRouterConfig routerConfig = new CrawlerRouterConfig("testId", 2, CrawlerManager.PoolStrategy.ROUND_ROBIN);

    @BeforeClass
    public static void setUp() {
//        probe = testKitResource.createTestProbe(CrawlerManager.StartRouterResponse.class);
        crawlerRouter = testKitResource.spawn(CrawlerRouter.create(routerConfig, targetUrls));
    }

    @Test
    public void test() {
        final TestProbe<CrawlerManager.StartRouterResponse> startRouterResponseTestProbe = testKitResource.createTestProbe(CrawlerManager.StartRouterResponse.class);
        final TestProbe<CrawlerManager.ReadRouterResponse> readRouterResponseTestProbe = testKitResource.createTestProbe(CrawlerManager.ReadRouterResponse.class);
//        System.out.println("crawlerRouter tell: "+ probe.getRef().path());
        crawlerRouter.tell(new CrawlerManager.StartRouter("testId", startRouterResponseTestProbe.ref()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        crawlerRouter.tell(new CrawlerManager.ReadRouter("testId", readRouterResponseTestProbe.ref()));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final CrawlerManager.ReadRouterResponse readRouterResponse = readRouterResponseTestProbe.receiveMessage();
        assertEquals("Google", Jsoup.parse(readRouterResponse.results.get(targetUrl1)).title());
        assertEquals("Apple", Jsoup.parse(readRouterResponse.results.get(targetUrl2)).title());
    }


}
