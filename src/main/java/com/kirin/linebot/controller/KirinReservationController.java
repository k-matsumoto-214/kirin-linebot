package com.kirin.linebot.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirin.linebot.model.ReservationDate;
import com.kirin.linebot.model.ReservationType;
import com.kirin.linebot.model.dto.ReservationTypeDto;
import com.kirin.linebot.model.type.ReservationName;
import com.kirin.linebot.model.type.ReservationTime;
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

    private static final List<ReservationType> reservationTypes = List.of(
            ReservationType.of(ReservationName.NAO, ReservationTime.AM),
            ReservationType.of(ReservationName.NAO, ReservationTime.PM),
            ReservationType.of(ReservationName.KYO, ReservationTime.AM),
            ReservationType.of(ReservationName.KYO, ReservationTime.PM));

    private static final String DEFAULT_REPLY = "\"予約\"や\"よやく\"と入力して予約を開始しよう!";

    private static final String POST_BACK_DATE_KEY = "date";

    private static final String RESERVATION_SUCCESS_MESSAGE = "%sの予約の予約に成功したよ!(%s %s)";
    private static final String RESERVATION_FAILURE_MESSAGE = "%sの予約の予約に失敗しちゃいました。。。(%s %s)";
    private static final String RESERVATION_DUPUILCATION_MESSAGE = "既に予約の予約済みだよ！";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @EventMapping
    /**
     * BOTに向けて送信された日付情報を解釈します
     * 
     * @param event POSTイベント
     */
    public void handlePostbackEvent(PostbackEvent event) throws JsonMappingException, JsonProcessingException {
        String replyToken = event.getReplyToken();
        String reservationTypeString = event.getPostbackContent().getData();

        // 予約種別情報を取得
        ReservationTypeDto dto = objectMapper.readValue(reservationTypeString, ReservationTypeDto.class);
        ReservationType reservationType = ReservationType.from(dto);

        // 予約対象の日付を取得
        LocalDate targeDate = LocalDate.parse(
                event.getPostbackContent().getParams().get(POST_BACK_DATE_KEY), DTF_TO_DATE);

        // 表示用に日付を成形する
        String targetDateString = targeDate.format(DTF_TO_STRING);

        // DBに登録済みか確認する
        ReservationDate reservationDate = reservationService.findReservationTarget(targeDate, reservationType);

        if (!reservationDate.isEmpty()) {
            // 登録済みの時処理終了
            lineMessageService.sendText(replyToken, RESERVATION_DUPUILCATION_MESSAGE);
            return;
        }

        // DB登録実行
        boolean isReservationSuccess = reservationService.reserve(targeDate, reservationType);

        // DB登録結果によって送信メッセージを変更
        if (isReservationSuccess) {
            lineMessageService.sendText(replyToken,
                    String.format(RESERVATION_SUCCESS_MESSAGE,
                            reservationType.getReservationName().getValue(),
                            targetDateString,
                            reservationType.getReservationTime().getDiscription()));
        } else {
            lineMessageService.sendText(replyToken,
                    String.format(RESERVATION_FAILURE_MESSAGE,
                            reservationType.getReservationName().getValue(),
                            targetDateString,
                            reservationType.getReservationTime().getDiscription()));
        }
    }

    @EventMapping
    /**
     * BOT宛に送信されたメッセージを解釈します
     * 
     * @param event メッセージイベント
     */
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        String replyToken = event.getReplyToken();
        TextMessageContent message = event.getMessage();
        String text = message.getText();

        switch (text) {
            case "よ":
            case "よや":
            case "よやく":
            case "予約":
                lineMessageService.sendReservationType(replyToken, reservationTypes);
                break;
            default:
                lineMessageService.sendText(replyToken, DEFAULT_REPLY);
                break;
        }
    }
}
