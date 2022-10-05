package com.kirin.linebot.repository.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import com.kirin.linebot.repository.ReservationRepository;
import com.kirin.linebot.repository.mapper.ReservationDateMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

  private final ReservationDateMapper mapper;

  @Override
  public void insertReservationDate(LocalDate reservationDate, String targetName) {
    mapper.insert(reservationDate, targetName);
  }
}
