package com.kirin.linebot.controller

import com.kirin.linebot.model.ReservationDate
import com.kirin.linebot.model.type.ReservationName
import com.kirin.linebot.model.type.ReservationTime
import com.kirin.linebot.service.LineMessageService
import com.kirin.linebot.service.ReservationService
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.PostbackEvent
import com.linecorp.bot.model.event.message.TextMessageContent
import com.linecorp.bot.model.event.postback.PostbackContent
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = KirinReservationController)
class KirinReservationControllerSpec extends Specification {

    @Autowired
    KirinReservationController controller

    @SpringBean
    LineMessageService lineMessageService = Mock()

    @SpringBean
    ReservationService reservationService = Mock()

    def "handlePostbackEvent_正常処理"() {
        setup:

        def data = "{\"reservationName\":\"" + ReservationName.NAO.getValue() + "\",\"reservationTime\":\"" + ReservationTime.PM.getValue() + "\"}"

        def content = PostbackContent.builder()
                .data(data)
                .params(Map.of("date", "2022-10-10"))
                .build()

        def event = PostbackEvent.builder()
                .replyToken("test")
                .postbackContent(content)
                .build()

        1 * reservationService.findReservationTarget(*_) >> Mock(ReservationDate) {
            isEmpty() >> true
        }

        1 * reservationService.reserve(*_) >> true

        1 * lineMessageService.sendText(*_)

        when:

        controller.handlePostbackEvent(event)

        then:
        noExceptionThrown()
    }

    def "handlePostbackEvent_正常処理_予約済み"() {
        setup:

        def data = "{\"reservationName\":\"" + ReservationName.NAO.getValue() + "\",\"reservationTime\":\"" + ReservationTime.PM.getValue() + "\"}"

        def content = PostbackContent.builder()
                .data(data)
                .params(Map.of("date", "2022-10-10"))
                .build()

        def event = PostbackEvent.builder()
                .replyToken("test")
                .postbackContent(content)
                .build()

        1 * reservationService.findReservationTarget(*_) >> Mock(ReservationDate) {
            isEmpty() >> false
        }

        0 * reservationService.reserve(*_)

        1 * lineMessageService.sendText(*_)

        when:

        controller.handlePostbackEvent(event)

        then:
        noExceptionThrown()
    }

    def "handlePostbackEvent_予約失敗"() {
        setup:

        def data = "{\"reservationName\":\"" + ReservationName.NAO.getValue() + "\",\"reservationTime\":\"" + ReservationTime.PM.getValue() + "\"}"

        def content = PostbackContent.builder()
                .data(data)
                .params(Map.of("date", "2022-10-10"))
                .build()

        def event = PostbackEvent.builder()
                .replyToken("test")
                .postbackContent(content)
                .build()

        1 * reservationService.findReservationTarget(*_) >> Mock(ReservationDate) {
            isEmpty() >> true
        }

        1 * reservationService.reserve(*_) >> false

        1 * lineMessageService.sendText(*_)

        when:

        controller.handlePostbackEvent(event)

        then:
        noExceptionThrown()
    }

    def "handleTextMessageEvent_正常処理"() {
        setup:
        def event = MessageEvent<TextMessageContent>.builder()
                .replyToken("token")
                .message(TextMessageContent.builder().text(testMessage).build())
                .build()

        expectedReservationType * lineMessageService.sendReservationType(*_)
        expectedSendText * lineMessageService.sendText(*_)

        when:
        controller.handleTextMessageEvent(event)

        then:
        noExceptionThrown()

        where:
        testMessage | expectedReservationType | expectedSendText
        "よ"         | 1                       | 0
        "よや"        | 1                       | 0
        "よやく"       | 1                       | 0
        "予約"      | 1                       | 0
        "てすと"       | 0                       | 1

    }
}
