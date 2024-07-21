package com.gotham.hotel.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gotham.hotel.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long>{

    @Query("SELECT DISTINCT r.roomType from Room r")
    List<String> findDistinctRoomTypes();

    @Query("SELECT r from Room r WHERE r.roomType LIKE %:roomType% AND r.id NOT IN (SELECT bk.room.id FROM Booking bk WHERE (bk.checkInDate <= :checkOutDate) AND (bk.checkOutDate >= :checkInDate)) AND r.roomType LIKE %:roomType%")
    List<Room> findAvailableRoomByDatesAndTypes(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

    @Query("SELECT r FROM Room r WHERE r.id NOT IN (SELECT b.room.id FROM Booking b)")
    List<Room> getAllAvailabRooms();
    
}
