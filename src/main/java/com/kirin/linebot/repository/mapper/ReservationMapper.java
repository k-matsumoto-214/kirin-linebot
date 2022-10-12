package com.kirin.linebot.repository.mapper;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;

import com.kirin.linebot.model.ReservationType;
import com.kirin.linebot.repository.entity.ReservationDto;

@Mapper
public interface ReservationMapper {

  void insert(LocalDate reservationDate, ReservationType reservationType);

  ReservationDto findReservation(LocalDate reservationDate, ReservationType reservationType);
}
