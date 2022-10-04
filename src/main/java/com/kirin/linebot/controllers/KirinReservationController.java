package com.kirin.linebot.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kirin.linebot.service.LineMessageService;
import com.kirin.linebot.service.ReservationService;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.RequiredArgsConstructor;

@LineMessageHandler
@RequiredArgsConstructor
public class KirinReservationController {

    private final LineMessageService lineMessageService;

    private final ReservationService reservationService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String TARGET_NAME_NAO = "尚大";
    private static final String TARGET_NAME_KYO = "匡平";

    private static final String DEFAULT_REPLY = "\"なおひろ\"や\"きょうへい\"のように名前を送信してね!";

    private static final String POST_BACK_DATE_KEY = "date";

    private static final String RESERVATION_SUCCESS_MESSAGE = "%sの予約の予約成功です! 対象日付: %s";
    private static final String RESERVATION_FAILURE_MESSAGE = "%sの予約の予約に失敗しちゃいました。。。 対象日付: %s";

    @EventMapping
    @Transactional(rollbackFor = Exception.class)
    public void handlePostbackEvent(PostbackEvent event) {
        String replyToken = event.getReplyToken();
        String targetDateString = event.getPostbackContent().getParams().get(POST_BACK_DATE_KEY);
        String targetName = event.getPostbackContent().getData();
        LocalDate targeDate = LocalDate.parse(targetDateString, DATE_TIME_FORMATTER);

        boolean isReservationSuccess = reservationService.reserve(targeDate, targetName);

        if (isReservationSuccess) {
            lineMessageService.sendText(replyToken,
                    String.format(RESERVATION_SUCCESS_MESSAGE, targetName, targetDateString));
        } else {
            lineMessageService.sendText(replyToken,
                    String.format(RESERVATION_FAILURE_MESSAGE, targetName, targetDateString));
        }
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        String replyToken = event.getReplyToken();
        TextMessageContent message = event.getMessage();
        String text = message.getText();

        switch (text) {
            case "よやく":
            case "予約":
                lineMessageService.sendReservationName(replyToken, List.of(TARGET_NAME_NAO, TARGET_NAME_KYO));
                break;
            case "尚大":
            case "尚":
            case "なおひろ":
            case "なお":
            case "なおちゃん":
                lineMessageService.sendReservationDate(replyToken, TARGET_NAME_NAO);
                break;
            case "きょうへい":
            case "匡平":
            case "きょうちゃん":
                lineMessageService.sendReservationDate(replyToken, TARGET_NAME_KYO);
                break;
            default:
                lineMessageService.sendText(replyToken, DEFAULT_REPLY);
                break;
        }
    }
}
