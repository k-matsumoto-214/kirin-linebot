package com.kirin.linebot.model;

import com.kirin.linebot.model.dto.ReservationTypeDto;
import com.kirin.linebot.model.type.ReservationName;
import com.kirin.linebot.model.type.ReservationTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class ReservationType {
  private final ReservationName reservationName;
  private final ReservationTime reservationTime;

  /**
   * 予約ドメインタイプのファクトリメソッド
   * 
   * @param reservationName 予約者名
   * @param reservationTime 予約時間帯
   * @return 予約タイプドメイン
   */
  public static ReservationType of(ReservationName reservationName, ReservationTime reservationTime) {
    return ReservationType.builder()
        .reservationName(reservationName)
        .reservationTime(reservationTime)
        .build();
  }

  /**
   * 予約タイプドメインのファクトリメソッド（LINEからの返信情報受け取り）
   * 
   * @param dto LINEからの返信情報DTO
   * @return 予約タイプドメイン
   */
  public static ReservationType from(ReservationTypeDto dto) {
    return ReservationType.builder()
        .reservationName(ReservationName.from(dto.getReservationName()))
        .reservationTime(ReservationTime.from(dto.getReservationTime()))
        .build();
  }
}
