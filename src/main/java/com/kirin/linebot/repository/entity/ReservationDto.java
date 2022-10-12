package com.kirin.linebot.repository.entity;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationDto {
  private String Name;
  private LocalDate date;
  private String reservationTime;
}
