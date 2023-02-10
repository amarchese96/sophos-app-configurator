package it.unict;

import java.util.Map;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import io.smallrye.mutiny.Uni;

@RegisterRestClient(configKey = "telemetry")
public interface TelemetryService {

    @GET
    @Path("/metrics/apps/cpu-usage")
    Uni<Double> getCpuUsage(@QueryParam("app-group") String appGroupName, @QueryParam("app") String appName);

    @GET
    @Path("/metrics/apps/memory-usage")
    Uni<Double> getMemoryUsage(@QueryParam("app-group") String appGroupName, @QueryParam("app") String appName);

    @GET
    @Path("/metrics/apps/traffic")
    Uni<Map<String,Double>> getTraffic(@QueryParam("app-group") String appGroupName, @QueryParam("app") String appName, @QueryParam("direction") @DefaultValue("all") String direction);
}
