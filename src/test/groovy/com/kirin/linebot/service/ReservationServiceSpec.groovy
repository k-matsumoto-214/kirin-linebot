package com.kirin.linebot.service

import com.kirin.linebot.model.ReservationDate
import com.kirin.linebot.model.ReservationType
import com.kirin.linebot.repository.database.ReservationRepository
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ReservationServiceSpec extends Specification {

    @Autowired
    ReservationService reservationService

    @SpringBean
    private ReservationRepository reservationRepository = Mock()

    def "reserve_正常"() {
        setup:
        1 * reservationRepository.insertReservationDate(*_)

        when:
        def actual = reservationService.reserve(Mock(LocalDate), Mock(ReservationType))

        then:
        actual == true
    }

    def "reserve_登録失敗"() {
        setup:
        1 * reservationRepository.insertReservationDate(*_) >> { throw new RuntimeException() }

        when:
        def actual = reservationService.reserve(Mock(LocalDate), Mock(ReservationType))

        then:
        actual == false
    }

    def "findReservationTarget_正常"() {
        setup:
        1 * reservationRepository.findReservation(*_) >> Mock(ReservationDate)

        when:
        def actual = reservationService.findReservationTarget(GroovyMock(LocalDate), Mock(ReservationType))

        then:
        noExceptionThrown()
    }

    def "findReservationTarget_失敗時3回リトライ"() {
        setup:
        3 * reservationRepository.findReservation(*_) >> { throw new RuntimeException() }

        when:
        def actual = reservationService.findReservationTarget(GroovyMock(LocalDate), Mock(ReservationType))

        then:
        thrown(RuntimeException)
    }
}
