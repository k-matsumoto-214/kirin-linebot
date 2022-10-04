package com.kirin.linebot.repository;

import java.time.LocalDate;

public interface ReservationRepository {

  /**
   * DBに予約予定を挿入します
   * 
   * @param reservationDate 予約対象日付
   * @param targetName      予約したい名前
   */
  void insertReservationDate(LocalDate reservationDate, String targetName);
}
