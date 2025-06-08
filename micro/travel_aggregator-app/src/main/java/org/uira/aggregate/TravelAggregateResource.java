package org.uira.aggregate;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("travel-aggregator")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TravelAggregateResource {

    @RestClient
    @Inject
    TravelFlightService travelFlightService;

    @RestClient
    @Inject
    TravelHotelService travelHotelService;

    @RestClient
    @Inject
    TravelOrderService travelOrderService;

    @GET
    public List<TravelAggregatorDTO> travelAggregatorDTOS() {

        return travelOrderService.travelOrders().stream()
                .map(
                        order -> TravelAggregatorDTO.of(
                                order,
                                travelFlightService.findByTravelOrderId(order.getId()),
                                travelHotelService.findByTravelOrderId(order.getId())
                        )
                ).toList();

    }

    @POST
    public TravelAggregatorDTO newTravelAggregatorDTO(TravelAggregatorDTO travelAggregatorDTO) {
        TravelOrder travelOrder = new TravelOrder();
        travelOrder = travelOrderService.newTravelOrder(travelOrder);

        TravelFlight travelFlight = new TravelFlight();
        travelFlight.setTravelOrderId(travelOrder.getId());
        travelFlight.setFromAirport(travelAggregatorDTO.getFromAirport());
        travelFlight.setToAirport(travelAggregatorDTO.getToAirport());
        travelFlight = travelFlightService.newTravelFlight(travelFlight);

        TravelHotel travelHotel = new TravelHotel();
        travelHotel.setTravelOrderId(travelOrder.getId());
        travelHotel.setNights(travelAggregatorDTO.getNights());
        travelHotel = travelHotelService.newTravelHotel(travelHotel);

        return TravelAggregatorDTO.of(travelOrder, travelFlight, travelHotel);
    }

}
