package loadbalancer;

public interface LoadBalancer {
    void updateCandidate(final CandidateEndpoints candidateEndpoints);
    Endpoint nextEndpoint();
}
