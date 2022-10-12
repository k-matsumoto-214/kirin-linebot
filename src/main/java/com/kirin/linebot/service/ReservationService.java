package com.kirin.linebot.service;

import java.time.LocalDate;

import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.kirin.linebot.model.ReservationDate;
import com.kirin.linebot.model.ReservationType;
import com.kirin.linebot.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReservationService {

  private final ReservationRepository reservationRepository;

  /**
   * DBに予約情報を登録するよ
   * <p>
   * DB登録に成功した時trueが返るよ
   * 
   * @param date            予約対象日付
   * @param reservationType 予約種別情報
   * @return DB保存結果 成功:true
   */
  public boolean reserve(LocalDate date, ReservationType reservationType) {
    try {
      reservationRepository.insertReservationDate(date, reservationType);
      return true;
    } catch (Exception e) {
      log.error("予約データの登録に失敗しちゃったよ。。。　原因:{}", e.toString());
      return false;
    }
  }

  /**
   * DBから予約情報を取得する
   * 
   * @param reservationType 予約種別情報ドメイン
   * @param targeDate       予約対象日付
   * @return 予約情報
   */
  @Retryable
  public ReservationDate findReservationTarget(LocalDate targeDate, ReservationType reservationType) {
    // DBから予約情報を取得
    return reservationRepository.findReservation(targeDate, reservationType);
  }
}
