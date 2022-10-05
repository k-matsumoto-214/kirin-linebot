package com.kirin.linebot.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.kirin.linebot.service.LineMessageService;
import com.kirin.linebot.service.ReservationService;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@LineMessageHandler
public class KirinReservationController {

    @Autowired
    private LineMessageService lineMessageService;
    @Autowired
    private ReservationService reservationService;

    private static final DateTimeFormatter DTF_TO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DTF_TO_STRING = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    private static final String DEFAULT_REPLY = "\"予約\"や\"よやく\"と入力して予約を開始しよう!";

    private static final List<String> NAMES = List.of("尚大", "匡平");

    private static final String POST_BACK_DATE_KEY = "date";

    private static final String RESERVATION_SUCCESS_MESSAGE = "%sの予約に成功したよ!(%s)";
    private static final String RESERVATION_FAILURE_MESSAGE = "%sの予約に失敗しちゃいました。。。(%s)";

    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) {
        String replyToken = event.getReplyToken();
        String targetName = event.getPostbackContent().getData();
        LocalDate targeDate = LocalDate.parse(
                event.getPostbackContent().getParams().get(POST_BACK_DATE_KEY), DTF_TO_DATE);
        String targetDateString = targeDate.format(DTF_TO_STRING);

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
            case "よ":
            case "よや":
            case "よやく":
            case "予約":
                lineMessageService.sendReservationDate(replyToken, NAMES);
                break;
            default:
                lineMessageService.sendText(replyToken, DEFAULT_REPLY);
                break;
        }
    }
}
