package crawler;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

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

    }

    @Test
    public void testInvalidUrl() {

    }
}
