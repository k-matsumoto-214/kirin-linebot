package com.kirin.linebot.factory;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kirin.linebot.model.ReservationType;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;

@Component
public class TemplateFactory {

        @Value("${line.bot.image.kirin}")
        private String imagePath;

        private static final String TEXT_TYPE = "予約";
        private static final String LABEL_TYPE = "%sの予約 %s";
        private static final String DESCRIPTION_TYPE = "予約種別を選んでください";
        private static final String DEFAULT_ALT_TEXT = "LINEBOTからの返信";
        private static final ZoneId ZONE_ID = ZoneId.of("Asia/Tokyo");
        private static final LocalDate MAX_DATE = LocalDate.of(2030, 12, 31);

        /**
         * 予約日付一覧を表示するテンプレートを生成するファクトリ
         * 
         * @param targetName 予約対象の名前
         * @return 予約日付一覧を表示するメッセージテンプレート
         */
        public TemplateMessage reservationTypeMessage(List<ReservationType> reservationTypes) {
                LocalDate now = LocalDate.now(ZONE_ID);
                List<Action> datetimePickerActions = reservationTypes.stream()
                                .map(reservationType -> DatetimePickerAction.OfLocalDate
                                                .builder()
                                                .label(String.format(
                                                                LABEL_TYPE,
                                                                reservationType.getReservationName().getValue(),
                                                                reservationType.getReservationTime().getDiscription()))
                                                .initial(now)
                                                .min(now)
                                                .max(MAX_DATE)
                                                .data("{"
                                                                + "\"reservationName\":\""
                                                                + reservationType.getReservationName().getValue()
                                                                + "\","
                                                                + "\"reservationTime\":\""
                                                                + reservationType.getReservationTime().getValue()
                                                                + "\""
                                                                + "}")
                                                .build())
                                .collect(Collectors.toUnmodifiableList());
                ButtonsTemplate buttonTemplate = new ButtonsTemplate(
                                URI.create(imagePath),
                                TEXT_TYPE,
                                DESCRIPTION_TYPE,
                                datetimePickerActions);
                return new TemplateMessage(DEFAULT_ALT_TEXT, buttonTemplate);
        }
}
