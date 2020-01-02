package crawler;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CrawlerGroupExampleReaderTest {

    // automatically shutdown when the test is complete
    @ClassRule
    public static final TestKitJunitResource testKitResource = new TestKitJunitResource();

    private TestProbe<CrawlerManagerExample.ParseAllResponse> requester;

    @Before
    public void setUp() throws Exception {
        requester = testKitResource.createTestProbe(CrawlerManagerExample.ParseAllResponse.class);
    }

    @Test
    public void testResponseFromHealthyCrawlers() {
        TestProbe<CrawlerActorExample.Command> crawler1 = testKitResource.createTestProbe(CrawlerActorExample.Command.class);
        TestProbe<CrawlerActorExample.Command> crawler2 = testKitResource.createTestProbe(CrawlerActorExample.Command.class);

        Map<String, ActorRef<CrawlerActorExample.Command>> crawlerIdToActor = new HashMap<>();
        crawlerIdToActor.put("crawler1", crawler1.getRef());
        crawlerIdToActor.put("crawler2", crawler2.getRef());

        ActorRef<CrawlerGroupReader.Command> readerActor = testKitResource.spawn(CrawlerGroupReader.create(
                crawlerIdToActor,
                "id1",
                requester.getRef(),
                Duration.ofSeconds(3)));
        // assert that crawler will receive Read command after starting readerActor
        crawler1.expectMessageClass(CrawlerActorExample.Read.class);
        crawler2.expectMessageClass(CrawlerActorExample.Read.class);

        readerActor.tell(new CrawlerGroupReader.WrappedResponse(new CrawlerActorExample.Response("1", "crawler1", Optional.of("ExampleResult"))));
        readerActor.tell(new CrawlerGroupReader.WrappedResponse(new CrawlerActorExample.Response("1", "crawler2", Optional.of("ExampleResult"))));

        CrawlerManagerExample.ParseAllResponse response = requester.receiveMessage();
        assertEquals("id1", response.requestId);

        Map<String, CrawlerManagerExample.CrawlerState> expectedStates = new HashMap<>();
        expectedStates.put("crawler1", new CrawlerManagerExample.AvailableCrawler("ExampleResult"));
        expectedStates.put("crawler2", new CrawlerManagerExample.AvailableCrawler("ExampleResult"));

        Map<String, CrawlerManagerExample.CrawlerState> actualCrawlerIdToQueryState = response.crawlerIdToQueryState;
        assertFalse(actualCrawlerIdToQueryState.isEmpty());
        assertTrue(actualCrawlerIdToQueryState.containsKey("crawler1") && actualCrawlerIdToQueryState.containsKey("crawler2"));
        assertTrue(actualCrawlerIdToQueryState.get("crawler1") instanceof CrawlerManagerExample.AvailableCrawler);
        assertTrue(actualCrawlerIdToQueryState.get("crawler2") instanceof CrawlerManagerExample.AvailableCrawler);
    }

    @Test
    public void testResponseFromFailedCrawlers() {
        TestProbe<CrawlerActorExample.Command> crawler1 = testKitResource.createTestProbe(CrawlerActorExample.Command.class);
        TestProbe<CrawlerActorExample.Command> crawler2 = testKitResource.createTestProbe(CrawlerActorExample.Command.class);

        Map<String, ActorRef<CrawlerActorExample.Command>> crawlerIdToActor = new HashMap<>();
        crawlerIdToActor.put("crawler1", crawler1.getRef());
        crawlerIdToActor.put("crawler2", crawler2.getRef());

        ActorRef<CrawlerGroupReader.Command> readerActor = testKitResource.spawn(CrawlerGroupReader.create(
                crawlerIdToActor,
                "id1",
                requester.getRef(),
                Duration.ofSeconds(3)));

    }
}
