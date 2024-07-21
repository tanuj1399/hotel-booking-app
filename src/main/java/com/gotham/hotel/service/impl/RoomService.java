package com.gotham.hotel.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gotham.hotel.dto.Response;
import com.gotham.hotel.dto.RoomDTO;
import com.gotham.hotel.entity.Room;
import com.gotham.hotel.exception.OurException;
import com.gotham.hotel.repo.RoomRepository;
import com.gotham.hotel.service.AwsS3Service;
import com.gotham.hotel.service.interfac.IRoomService;
import com.gotham.hotel.utils.Utils;

@Service
public class RoomService implements IRoomService{

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AwsS3Service awsS3Service;

    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        
        Response response = new Response();
        
        try{
            String imageUrl = awsS3Service.saveImagesToS3(photo);
            Room room = new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(description);
            Room savedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(savedRoom);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoom(roomDTO);

        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error adding a room" + e.getMessage());
        }

        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {

        Response response = new Response();
        
        try{
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoomList(roomDTOList);

        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting rooms" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();
        
        try{
            roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not found"));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("Successful");
        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error deleting room" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        Response response = new Response();
        
        try{
            String imageUrl = null;
           if(photo != null && !photo.isEmpty()){
                imageUrl = awsS3Service.saveImagesToS3(photo);
           }
           Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not found"));
           if(roomType != null){
                room.setRoomType(roomType);
           }
            if(roomPrice != null){
                room.setRoomPrice(roomPrice);
            }
            if(description != null){
                room.setRoomDescription(description);
            }
            if(imageUrl != null){
                room.setRoomPhotoUrl(imageUrl);
            }

            Room updatedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoom(roomDTO);
        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error updating a room" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();
        
        try{
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new OurException("Room not found"));
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTOPlusBookings(room);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setRoom(roomDTO);
        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting a room" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOuDate, String roomType) {
        Response response = new Response();
        
        try{
           List<Room> availableRooms = roomRepository.findAvailableRoomByDatesAndTypes(checkInDate, checkOuDate, roomType);
           List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);
           response.setStatusCode(200);
           response.setMessage("Successful");
           response.setRoomList(roomDTOList);
        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting available rooms" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();
        
        try{
           List<Room> roomList = roomRepository.getAllAvailabRooms();
           List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);
           response.setStatusCode(200);
           response.setMessage("Successful");
           response.setRoomList(roomDTOList);
        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting available rooms" + e.getMessage());
        }

        return response;
    }
    
}
