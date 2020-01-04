package crawler;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import org.jsoup.Jsoup;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class CrawlerActorTest {
    @ClassRule
    public static final TestKitJunitResource testKitResource = new TestKitJunitResource();


    private static TestProbe<CrawlerActor.StartCrawlerResponse> probe;
    private static ActorRef<CrawlerActor.Command> crawlerActor;

    @BeforeClass
    public static void setUp() {
        probe = testKitResource.createTestProbe(CrawlerActor.StartCrawlerResponse.class);
        crawlerActor = testKitResource.spawn(CrawlerActor.create());
    }

    @Test
    public void testStartCrawlerSuccess() {
        String targetUrl = "https://www.google.com/";
        crawlerActor.tell(new CrawlerActor.StartCrawler("testId", targetUrl, probe.ref()));
        String receivedResult = probe.receiveMessage().result;
        assertEquals("Google", Jsoup.parse(receivedResult).title());
    }

    @Test
    public void testCrawlFailure() {
        String targetUrl = "http://www.fjasifjasf.com/";
        crawlerActor.tell(new CrawlerActor.StartCrawler("testId", targetUrl, probe.getRef()));
        assertEquals(CrawlerActor.CrawlerFailure.INSTANCE.toString(), probe.receiveMessage().state.toString());
    }

    @Test
    public void testInvalidUrl() {
        String targetUrl = "htt:///";

        crawlerActor.tell(new CrawlerActor.StartCrawler("testId", targetUrl, probe.getRef()));


        assertEquals(CrawlerActor.TargetInvalid.INSTANCE.toString(), probe.receiveMessage().state.toString());
    }

}
