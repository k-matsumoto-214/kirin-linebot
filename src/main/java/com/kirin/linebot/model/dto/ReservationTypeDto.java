package com.kirin.linebot.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
/**
 * LINEからの返信情報を受け取りオブジェクトにマッピングする際のDTOオブジェクト
 */
public class ReservationTypeDto {
  private String reservationName;
  private String reservationTime;
}
