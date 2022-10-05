package com.kirin.linebot.repository.mapper;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationDateMapper {

  void insert(LocalDate reservationDate, String targetName);
}
