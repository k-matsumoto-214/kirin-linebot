package com.kirin.linebot.factory

import com.kirin.linebot.model.ReservationType
import com.kirin.linebot.model.type.ReservationName
import com.kirin.linebot.model.type.ReservationTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = TemplateFactory.class)
class TemplateFactorySpec extends Specification {

    @Autowired
    TemplateFactory templateFactory

    def "reservationTypeMessage_正常処理"() {
        setup:
        def reservationTypes = List.of(
                Mock(ReservationType) {
                    getReservationTime() >> ReservationTime.AM
                    getReservationName() >> ReservationName.NAO
                }
        )

        when:
        def actual = templateFactory.reservationTypeMessage(reservationTypes)


        then:
        actual.getAltText() == "LINEBOTからの返信"
    }
}
