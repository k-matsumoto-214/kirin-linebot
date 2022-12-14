package com.kirin.linebot.model.type;

import java.util.Arrays;
import java.util.Objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReservationTime {
  AM("午前", "0"), // 午前予約
  PM("午後", "1"); // 午後予約

  private final String discription;
  private final String value;

  /**
   * ファクトリメソッド
   * 
   * @param value 予約開始種別の値
   * @return 予約種別ドメイン
   */
  public static ReservationTime from(String value) {
    return Arrays.stream(ReservationTime.values())
        .filter(reservationTime -> Objects.equals(reservationTime.getValue(), value))
        .findFirst()
        .orElse(null);
  }
}
