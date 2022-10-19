package com.kirin.linebot.repository.database.entity;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
/**
 * DBの予約情報を受け取りオブジェクトにマッピングする際のDTOオブジェクト
 */
public class ReservationDto {

  private String Name;
  private LocalDate date;
  private String reservationTime;
}
