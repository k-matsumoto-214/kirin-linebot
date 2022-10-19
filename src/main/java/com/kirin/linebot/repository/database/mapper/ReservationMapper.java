package com.kirin.linebot.repository.database.mapper;

import com.kirin.linebot.model.ReservationType;
import com.kirin.linebot.repository.database.entity.ReservationDto;
import java.time.LocalDate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper {

  void insert(LocalDate reservationDate, ReservationType reservationType);

  ReservationDto findReservation(LocalDate reservationDate, ReservationType reservationType);
}
