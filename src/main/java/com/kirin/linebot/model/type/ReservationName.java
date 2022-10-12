package com.kirin.linebot.model.type;

import java.util.Arrays;
import java.util.Objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReservationName {
  NAO("尚大"),
  KYO("匡平");

  private final String value;

  /**
   * ファクトリメソッド
   * 
   * @param value 予約者名の値
   * @return 予約者名ドメイン
   */
  public static ReservationName from(String value) {
    return Arrays.stream(ReservationName.values())
        .filter(reservationName -> Objects.equals(reservationName.getValue(), value))
        .findFirst()
        .orElse(null);
  }
}
