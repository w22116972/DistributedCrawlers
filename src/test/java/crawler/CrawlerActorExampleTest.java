package crawler;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import org.jsoup.Jsoup;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.Optional;

import static org.junit.Assert.*;


public class CrawlerActorExampleTest {

    // automatically shutdown when the test is complete
    @ClassRule public static final TestKitJunitResource testKitResource = new TestKitJunitResource();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static TestProbe<CrawlerActorExample.Response> probe;
    private static ActorRef<CrawlerActorExample.Command> crawlerActor;

    @BeforeClass
    public static void setUp() {
        probe = testKitResource.createTestProbe(CrawlerActorExample.Response.class);
        crawlerActor = testKitResource.spawn(CrawlerActorExample.create("testGroup", "testCrawler1"));
    }

    @Test
    public void testParsePageThenReplyContent() {
        String targetUrl = "https://www.google.com/";
        crawlerActor.tell(new CrawlerActorExample.Parse("42", targetUrl, probe.getRef()));
//        crawlerActor.tell(new CrawlerActor.Read("42", probe.getRef()));
        final Optional<String> result = probe.receiveMessage().result;
        assertEquals("Google", Jsoup.parse(result.get()).title());
    }

    @Test
    public void testParseInvalidPageThenReplyEmpty() {
//        thrown.expect(NoSuchElementException.class);
        String targetUrl = "http://www.fjasifjasf.com/";
        crawlerActor.tell(new CrawlerActorExample.Parse("42", targetUrl, probe.getRef()));
        final Optional<String> result = probe.receiveMessage().result;
        assertFalse(result.isPresent());
    }



    @Test
    public void testReplyWithEmptyQueryIfTargetIsUnknown() {
//        thrown.expect(IllegalStateException.class);
////        thrown.expectMessage("Url is invalid");
//        String testUrl = "";
//        TestProbe<CrawlerActor.Response> probe = testKitResource.createTestProbe(CrawlerActor.Response.class);
//        ActorRef<CrawlerActor.Command> crawlerActor = testKitResource.spawn(CrawlerActor.create("group1", "crawler1"));
//        crawlerActor.tell(new CrawlerActor.Parse("42", testUrl, probe.getRef()));
//        testKitResource.stop(crawlerActor, Duration.ofSeconds(10));
//        // probe.expectMessage(new CrawlerActor.Parse("42", probe.getRef(), testUrl));
//        CrawlerActor.Response response = probe.receiveMessage();
//        Throwable exception = assertThrows(IllegalStateException.class, () -> {
//            crawlerActor.tell(new CrawlerActor.Parse("42", probe.getRef(), testUrl));
//            // probe.expectMessage(new CrawlerActor.Parse("42", probe.getRef(), testUrl));
//            CrawlerActor.Response response = probe.receiveMessage();
//        });

//        assertEquals("Url is invalid", exception.getMessage());
//        assertEquals("42", response.requestId);
//        assertEquals(Optional.empty(), response.responseBody);
    }

    @Test
    public void testReplyWithLatestTargetQuery() {

//        TestProbe<CrawlerActor.Parse> parseProbe = testKitResource.createTestProbe(CrawlerActor.Parse.class);
//        TestProbe<CrawlerActor.ParseResponse> responseProbe = testKitResource.createTestProbe(CrawlerActor.ParseResponse.class);
//
//        ActorRef<CrawlerActor.Command> crawlerActor = testKitResource.spawn(CrawlerActor.create("group", "crawler"));

    }

    @AfterClass
    public static void cleanup() {
        testKitResource.testKit().shutdownTestKit();
    }
}
