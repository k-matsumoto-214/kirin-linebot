package com.kirin.linebot.repository.database.impl;

import com.kirin.linebot.model.ReservationDate;
import com.kirin.linebot.model.ReservationType;
import com.kirin.linebot.repository.database.ReservationRepository;
import com.kirin.linebot.repository.database.mapper.ReservationMapper;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

  private final ReservationMapper mapper;

  @Override
  public void insertReservationDate(LocalDate reservationDate, ReservationType reservationType) {
    mapper.insert(reservationDate, reservationType);
  }

  @Override
  public ReservationDate findReservation(LocalDate reservationDate,
      ReservationType reservationType) {
    return ReservationDate.from(mapper.findReservation(reservationDate, reservationType));

  }
}
