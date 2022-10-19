package com.kirin.linebot.service

import com.kirin.linebot.factory.TemplateFactory
import com.kirin.linebot.model.ReservationType
import com.linecorp.bot.client.LineMessagingClient
import com.linecorp.bot.model.message.TemplateMessage
import com.linecorp.bot.model.response.BotApiResponse
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class LineMessageServiceSpec extends Specification {

    @Autowired
    private LineMessageService lineMessageService

    @SpringBean
    private LineMessagingClient lineMessagingClient = Mock()

    @SpringBean
    private TemplateFactory templateFactory = Mock()

    def "sendText_正常"() {
        setup:
        1 * lineMessagingClient.replyMessage(*_) >> Mock(CompletableFuture<BotApiResponse>) {
            get() >> GroovyMock(BotApiResponse)
        }

        when:
        lineMessageService.sendText("token", "message")

        then:
        noExceptionThrown()
    }

    def "sendText_tokenが空"() {
        setup:

        when:
        lineMessageService.sendText("", "message")

        then:
        0 * lineMessagingClient.replyMessage(*_)
        thrown(IllegalArgumentException)
    }

    def "sendText_LINE通知で例外発生"() {
        setup:
        1 * lineMessagingClient.replyMessage(*_) >> { throw new Exception() }

        when:
        lineMessageService.sendText("token", "message")

        then:
        thrown(RuntimeException)
    }

    def "sendReservationType_正常"() {
        setup:
        1 * lineMessagingClient.replyMessage(*_) >> Mock(CompletableFuture<BotApiResponse>) {
            get() >> GroovyMock(BotApiResponse)
        }

        1 * templateFactory.reservationTypeMessage(*_) >> GroovyMock(TemplateMessage)

        def reservationTypes = List.of(Mock(ReservationType))

        when:
        lineMessageService.sendReservationType("token", reservationTypes)

        then:
        noExceptionThrown()
    }

    def "sendReservationType_tokenが空"() {
        setup:
        0 * lineMessagingClient.replyMessage(*_)

        0 * templateFactory.reservationTypeMessage(*_)

        def reservationTypes = List.of(Mock(ReservationType))

        when:
        lineMessageService.sendReservationType("", reservationTypes)

        then:
        thrown(IllegalArgumentException)
    }

    def "sendReservationType_LINE通知で例外発生"() {
        setup:
        1 * lineMessagingClient.replyMessage(*_) >> { throw new Exception() }

        1 * templateFactory.reservationTypeMessage(*_) >> GroovyMock(TemplateMessage)

        def reservationTypes = List.of(Mock(ReservationType))

        when:
        lineMessageService.sendReservationType("token", reservationTypes)

        then:
        thrown(RuntimeException)
    }
}
