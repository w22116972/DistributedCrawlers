package loadbalancer;

import java.util.HashMap;
import java.util.Map;

public class WeightedRoundRobinLoadBalancer extends AbstractLoadBalancer {


    private WeightedRoundRobinLoadBalancer(CandidateEndpoints candidateEndpoints) {
        super(candidateEndpoints);
    }

    public static LoadBalancer getInstance(CandidateEndpoints candidateEndpoints) throws Exception {
        WeightedRoundRobinLoadBalancer loadBalancer = new WeightedRoundRobinLoadBalancer(candidateEndpoints);
        loadBalancer.initThread();
        return loadBalancer;
    }

    @Override
    public Endpoint nextEndpoint() {
        Endpoint selectedEndpoint = null;
        int subWeight = 0;
        int dynamicTotalWeight;
        final double rawRandom = super.random.nextDouble(); // 0.0~1.0
        int random;

        final CandidateEndpoints candidateEndpoints = super.candidateEndpoints;
        dynamicTotalWeight = candidateEndpoints.totalWeight;
        for (Endpoint endpoint: candidateEndpoints) {
            if (!endpoint.isOnLine()) {
                dynamicTotalWeight -= endpoint.weight;
                continue;
            }
            random = (int) (rawRandom * dynamicTotalWeight);
            subWeight += endpoint.weight;
            if (random <= subWeight) {
                selectedEndpoint = endpoint;
                break;
            }
        }
        return selectedEndpoint;
    }

    // CAS atomically update memoryLocation to new value only if memory location matched to old value
    // In other words, V should have the value A; if it does, put B there, otherwise don’t change it but tell me I was wrong
//    boolean compareAndSwap(Object memoryLocation, Object oldValue, Object newValue) {
//
//    }

    private Map<String, Integer> registry = new HashMap<>();
    public Map<String, Integer> getRegistry() {
        return registry;
    }

}
