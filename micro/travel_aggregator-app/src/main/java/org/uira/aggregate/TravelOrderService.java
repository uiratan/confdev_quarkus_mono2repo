package org.uira.aggregate;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TravelOrderService {

    @GET
    public List<TravelOrder> travelOrders();

    @GET
    @Path("findById")
    public TravelOrder findById(@QueryParam("id") long id);

    @POST
    public TravelOrder newTravelOrder(TravelOrder travelOrder);

    @DELETE
    public Response deleteById(long id);

}
