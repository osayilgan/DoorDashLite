package demos.okan.doordash.service.models;

public class NetworkState {

    public enum Status{
        LOADING,
        LOADED,
        FAILED
    }

    private final Status status;
    private final String msg;

    public static final NetworkState LOADED;
    public static final NetworkState LOADING;
    public static final NetworkState FAILED;

    public NetworkState(Status status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    static {
        LOADED = new NetworkState(Status.LOADED,"Success");
        LOADING = new NetworkState(Status.LOADING,"Running");
        FAILED = new NetworkState(Status.FAILED,"Failed");
    }

    public Status getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
