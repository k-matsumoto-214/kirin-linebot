package com.kirin.linebot.model.type

import com.kirin.linebot.model.ReservationDate
import com.kirin.linebot.repository.database.entity.ReservationDto
import spock.lang.Specification

import java.time.LocalDate

class ReservationDateSpec extends Specification {

    def "from_ファクトリメソッド"() {
        setup:
        def now = LocalDate.now()
        def dto = Mock(ReservationDto) {
            getDate() >> now
            getName() >> ReservationName.NAO.getValue()
            getReservationTime() >> ReservationTime.PM.getValue()
        }

        when:
        def actual = ReservationDate.from(dto)

        then:
        actual.getReservationTime() == ReservationTime.PM
        actual.getName() == ReservationName.NAO.getValue()
        actual.getDate() == now
    }
}
