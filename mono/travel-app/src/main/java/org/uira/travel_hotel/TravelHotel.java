package org.uira.travel_hotel;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class TravelHotel extends PanacheEntity {

    public Long travelOrderId;
    public Integer nights;

    public static TravelHotel findByTravelOrderId(long id) {
        return find("travelOrderId", id).firstResult();
    }
}
