package loadbalancer;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

public final class CandidateEndpoints implements Iterable<Endpoint> {

    private final Set<Endpoint> endpoints;
    public final int totalWeight;

    public CandidateEndpoints(Set<Endpoint> endpoints) {
        int sum = 0;
        for (Endpoint endpoint: endpoints) {
            sum += endpoint.weight;
        }
        this.endpoints = endpoints;
        this.totalWeight = sum;
    }

    public int getEndpointsCount() {
        return endpoints.size();
    }


    @Override
    public Iterator<Endpoint> iterator() {
        return endpoints.iterator();
    }
}
