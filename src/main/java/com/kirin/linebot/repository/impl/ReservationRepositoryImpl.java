package com.kirin.linebot.repository.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import com.kirin.linebot.model.ReservationDate;
import com.kirin.linebot.model.ReservationType;
import com.kirin.linebot.repository.ReservationRepository;
import com.kirin.linebot.repository.mapper.ReservationMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReservationRepositoryImpl implements ReservationRepository {

  private final ReservationMapper mapper;

  @Override
  public void insertReservationDate(LocalDate reservationDate, ReservationType reservationType) {
    mapper.insert(reservationDate, reservationType);
  }

  @Override
  public ReservationDate findReservation(LocalDate reservationDate, ReservationType reservationType) {

    log.error(reservationDate.toString() + " : " + reservationType.getReservationName().getValue());
    return ReservationDate.from(mapper.findReservation(reservationDate, reservationType));

  }
}
