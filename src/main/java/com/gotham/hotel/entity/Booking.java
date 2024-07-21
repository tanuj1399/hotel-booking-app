package com.gotham.hotel.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Check in date is required")
    private LocalDate checkInDate;
    @Future(message = "Check out date must be in future")
    private LocalDate checkOutDate;

    @Min(message = "Number of adults must not be less than 1", value = 1)
    private int numberOfAdults;

    @Min(message = "Number of adults must not be less than 0", value = 0)
    private int numberOfChildren;
    private int totalNumOfGuest;
    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public void calculateTotalNumberOfGuest(){
        this.totalNumOfGuest = this.numberOfAdults + this.numberOfChildren;
    }

    public void setNumberOfAdults(int numberOfAdults){
        this.numberOfAdults = numberOfAdults;
        calculateTotalNumberOfGuest();
    }    

    public void setNumberOfChildren(int numberOfChildren){
        this.numberOfChildren = numberOfChildren;
        calculateTotalNumberOfGuest();
    }

    @Override
    public String toString() {
        return "Booking [id=" + id + ", checkInDate=" + checkInDate + ", checkOutDate=" + checkOutDate
                + ", numberOfAdults=" + numberOfAdults + ", numberOfChildren=" + numberOfChildren + ", totalNumOfGuest="
                + totalNumOfGuest + ", bookingConfirmationCode=" + bookingConfirmationCode + "]";
    }


}
