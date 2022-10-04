package com.kirin.linebot.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kirin.linebot.factory.TemplateFactory;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LineMessageService {

  private final LineMessagingClient lineMessagingClient;
  private final TemplateFactory templateFactory;

  /**
   * LINEでメッセージを送信する
   * 
   * @param replyToken 返信用トークン
   * @param message    送信メッセージ
   */
  public void sendText(@NonNull String replyToken, @NonNull String message) {
    if (replyToken.isEmpty()) {
      throw new IllegalArgumentException("replyToken must not be empty");
    }
    if (message.length() > 1000) {
      message = message.substring(0, 1000 - 2) + "……";
    }
    try {
      BotApiResponse apiResponse = lineMessagingClient
          .replyMessage(
              new ReplyMessage(replyToken, Collections.singletonList(new TextMessage(message)), false))
          .get();
      log.info("Sent messages: {}", apiResponse);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * LINEで日付一覧を送信する
   * 
   * @param replyToken 返信用トークン
   * @param targetName 予約を行う対象の名前
   */
  public void sendReservationDate(@NonNull String replyToken, @NonNull String targetName) {
    if (replyToken.isEmpty()) {
      throw new IllegalArgumentException("replyToken must not be empty");
    }
    TemplateMessage message = templateFactory.reservationDateMessage(targetName);
    try {
      BotApiResponse apiResponse = lineMessagingClient
          .replyMessage(new ReplyMessage(replyToken, Collections.singletonList(message), false))
          .get();
      log.info("Sent messages: {}", apiResponse);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * LINEで予約可能者の一覧を送信する
   * 
   * @param replyToken  返信用トークン
   * @param targetNames 予約可能者の一覧
   */
  public void sendReservationName(String replyToken, List<String> targetNames) {
    if (replyToken.isEmpty()) {
      throw new IllegalArgumentException("replyToken must not be empty");
    }
    TemplateMessage message = templateFactory.reservationNameMessage(targetNames);
    try {
      BotApiResponse apiResponse = lineMessagingClient
          .replyMessage(new ReplyMessage(replyToken, Collections.singletonList(message), false))
          .get();
      log.info("Sent messages: {}", apiResponse);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
