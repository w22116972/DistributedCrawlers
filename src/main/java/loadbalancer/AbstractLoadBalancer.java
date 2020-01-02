package loadbalancer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scala.concurrent.impl.FutureConvertersImpl;


import java.util.Random;

public abstract class AbstractLoadBalancer implements LoadBalancer {
    private final static Logger LOGGER = LogManager.getLogger(AbstractLoadBalancer.class);
    protected volatile CandidateEndpoints candidateEndpoints;

    protected final Random random;
    private Thread heartbeatThread;


    public AbstractLoadBalancer(CandidateEndpoints candidateEndpoints) {
        if (candidateEndpoints == null) {
            LOGGER.debug("candidateEndpoints is null");
            throw new IllegalArgumentException("Invalid candidate " + candidateEndpoints);
        }
        this.candidateEndpoints = candidateEndpoints;
        this.random = new Random();
    }

    public synchronized void initThread() throws Exception {
        if (heartbeatThread == null) {
            heartbeatThread = new Thread(new HeartbeatTask(), "Load balancer Heartbeat");
            heartbeatThread.setDaemon(true); // Daemon thread will be terminated automatically if all other non-damon threads are terminated
            heartbeatThread.start();
        }
    }

    @Override
    public abstract Endpoint nextEndpoint();

    @Override
    public void updateCandidate(CandidateEndpoints candidateEndpoints) {
        if (candidateEndpoints == null || candidateEndpoints.getEndpointsCount() == 0) {
            LOGGER.debug("Invalid candidate");
            throw new IllegalArgumentException("Invalid candidate " + candidateEndpoints);
        }
        this.candidateEndpoints = candidateEndpoints;
    }

    protected void monitorEndpoints() {
        final CandidateEndpoints currentEndpoints = candidateEndpoints;
        boolean isTheEndpointOnline;
        // health check downstream endpoints
        for (Endpoint endpoint : currentEndpoints) {
            if (detectEndpoint(endpoint) != endpoint.isOnLine()) {
                endpoint.setOnLine(!endpoint.isOnLine());
                LOGGER.debug("Setting endpoint to correct state");
            }
        }
    }

    // TODO: need another proper way to detect endpoint state
    private boolean detectEndpoint(Endpoint endpoint) {
        return endpoint.isOnLine();
    }

    private class HeartbeatTask implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    monitorEndpoints();
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {

            }
        }
    }
}
