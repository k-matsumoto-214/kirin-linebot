package com.kirin.linebot.repository.database;

import com.kirin.linebot.model.ReservationDate;
import com.kirin.linebot.model.ReservationType;
import java.time.LocalDate;

public interface ReservationRepository {

  /**
   * DBに予約予定を挿入します
   *
   * @param reservationDate 予約対象日付
   * @param reservationType 予約種別情報
   */
  void insertReservationDate(LocalDate reservationDate, ReservationType reservationType);

  /**
   * 予約日,予約種別情報をキーにしてDBから予約情報を取得する
   *
   * @param reservationDate 検索対象の日付
   * @param reservationType 予約種別ドメイン
   * @return 予約日付情報のモデル
   */
  ReservationDate findReservation(LocalDate reservationDate, ReservationType reservationType);
}
