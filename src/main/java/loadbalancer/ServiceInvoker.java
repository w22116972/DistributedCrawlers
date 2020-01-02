package loadbalancer;


public class ServiceInvoker {
    // singleton pattern
    private static final ServiceInvoker INSTANCE = new ServiceInvoker();

    private ServiceInvoker() {
        // Debug.info("ServiceInvoker initialized")
    }

    public static ServiceInvoker getInstance() {
        return INSTANCE;
    }

    // instance of load balancer, its setter and getter
    private volatile LoadBalancer loadBalancer;

    public void setLoadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    // behaviors of ServiceInvoker

    public void dispatchRequest(String request) {
        Endpoint endpoint = getLoadBalancer().nextEndpoint();
        if (endpoint == null) {
            // log.info("no next endpoint");
            return;
        }
        dispatchToDownstream(request, endpoint);
    }

    private void dispatchToDownstream(String request, Endpoint endpoint) {

    }

}
