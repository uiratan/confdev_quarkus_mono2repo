package org.uira.aggregate;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.uira.travel_flight.TravelFlight;
import org.uira.travel_flight.TravelFlightResource;
import org.uira.travel_hotel.TravelHotel;
import org.uira.travel_hotel.TravelHotelResource;
import org.uira.travel_order.TravelOrder;
import org.uira.travel_order.TravelOrderResource;

import java.util.List;
import java.util.stream.Collectors;

@Path("travel-aggregator")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TravelAggregateResource {

    @Inject
    TravelFlightResource travelFlightResource;

    @Inject
    TravelHotelResource travelHotelResource;

    @Inject
    TravelOrderResource travelOrderResource;

    @GET
    public List<TravelAggregatorDTO> travelAggregatorDTOS() {

        return TravelOrder.<TravelOrder>listAll().stream()
                .map(
                        order -> TravelAggregatorDTO.of(
                                order,
                                travelFlightResource.findByTravelOrderId(order.id),
                                travelHotelResource.findByTravelOrderId(order.id)
                        )
                ).toList();

    }

    @POST
    public TravelAggregatorDTO newTravelAggregatorDTO(TravelAggregatorDTO travelAggregatorDTO) {
        TravelOrder travelOrder = new TravelOrder();
        travelOrder = travelOrderResource.newTravelOrder(travelOrder);

        TravelFlight travelFlight = new TravelFlight();
        travelFlight.travelOrderId = travelOrder.id;
        travelFlight.fromAirport = travelAggregatorDTO.getFromAirport();
        travelFlight.toAirport = travelAggregatorDTO.getToAirport();
        travelFlight = travelFlightResource.newTravelFlight(travelFlight);

        TravelHotel travelHotel = new TravelHotel();
        travelHotel.travelOrderId = travelOrder.id;
        travelHotel.nights = travelAggregatorDTO.getNights();
        travelHotel = travelHotelResource.newTravelHotel(travelHotel);

        return TravelAggregatorDTO.of(travelOrder, travelFlight, travelHotel);
    }

}
