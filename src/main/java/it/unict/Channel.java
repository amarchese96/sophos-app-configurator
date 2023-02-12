package it.unict;

public class Channel {

    private final String neighborAppName;

    private final double traffic;

    public Channel(String neighborAppName, double traffic) {
        this.neighborAppName = neighborAppName;
        this.traffic = traffic;
    }

    public String getNeighborAppName() {
        return neighborAppName;
    }

    public double getTraffic() {
        return traffic;
    }
}
