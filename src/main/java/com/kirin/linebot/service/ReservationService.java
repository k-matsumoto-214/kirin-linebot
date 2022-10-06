package com.kirin.linebot.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.kirin.linebot.repository.ReservationDateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReservationService {

  private final ReservationDateRepository reservationRepository;

  /**
   * DBに予約情報を登録するよ
   * <p>
   * DB登録に成功した時trueが返るよ
   * 
   * @param date 予約対象日付
   * @param name 予約対象者
   * @return DB保存結果 成功:true
   */
  public boolean reserve(LocalDate date, String name) {
    try {
      reservationRepository.insertReservationDate(date, name);
      return true;
    } catch (Exception e) {
      log.error("予約データの登録に失敗しちゃったよ。。。　原因:{}", e.toString());
      return false;
    }
  }
}
