package com.kirin.linebot.factory;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;

@Component
public class TemplateFactory {

  @Value("${line.bot.image.kirin}")
  private String imagePath;

  private static final String LABEL_DATE = "%sの予約";
  private static final String DESCRIPTION_DATE = "予約したい日付を選んでください";

  private static final String LABEL_NAME = "予約";
  private static final String DESCRIPTION_NAME = "予約対象を選んでください";

  /**
   * 予約日付一覧を表示するテンプレートを生成するファクトリ
   * 
   * @param targetName 予約対象の名前
   * @return 予約日付一覧を表示するメッセージテンプレート
   */
  public TemplateMessage reservationDateMessage(String targetName) {
    URI imageUrl = URI.create(imagePath);
    LocalDate now = LocalDate.now(ZoneId.of("Asia/Tokyo"));
    CarouselTemplate carouselTemplate = new CarouselTemplate(
        Arrays.asList(
            new CarouselColumn(
                imageUrl,
                targetName,
                DESCRIPTION_DATE,
                Arrays.asList(
                    DatetimePickerAction.OfLocalDate
                        .builder()
                        .label(String.format(LABEL_DATE, targetName))
                        .initial(now)
                        .min(now)
                        .max(LocalDate.parse("2100-12-31"))
                        .data(targetName)
                        .build()))));
    return new TemplateMessage(null, carouselTemplate);
  }

  /**
   * 予約者名一覧を表示するテンプレートを生成するファクトリ
   * 
   * @param targetNames 予約対象の名前のリスト
   * @return 予約者一覧を表示するメッセージテンプレート
   */
  public TemplateMessage reservationNameMessage(List<String> targetNames) {
    URI imageUrl = URI.create(imagePath);

    List<Action> actions = targetNames.stream()
        .map(name -> new MessageAction(name, name))
        .collect(Collectors.toUnmodifiableList());

    ButtonsTemplate buttonsTemplate = new ButtonsTemplate(imageUrl, LABEL_NAME, DESCRIPTION_NAME, actions);
    return new TemplateMessage(null, buttonsTemplate);
  }
}
