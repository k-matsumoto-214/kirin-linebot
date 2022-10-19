package com.kirin.linebot.model

import com.kirin.linebot.model.dto.ReservationTypeDto
import com.kirin.linebot.model.type.ReservationName
import com.kirin.linebot.model.type.ReservationTime
import spock.lang.Specification

class ReservationTypeSpec extends Specification {

    def "of_ファクトリ"() {
        setup:
        def name = ReservationName.NAO
        def time = ReservationTime.AM

        when:
        def actual = ReservationType.of(name, time)

        then:
        actual.getReservationName() == ReservationName.NAO
        actual.getReservationTime() == ReservationTime.AM
    }

    def "from_ファクトリ"() {
        setup:
        def name = ReservationName.NAO
        def time = ReservationTime.AM

        def dto = Mock(ReservationTypeDto) {
            getReservationName() >> name.getValue()
            getReservationTime() >> time.getValue()
        }

        when:
        def actual = ReservationType.from(dto)

        then:
        actual.getReservationName() == ReservationName.NAO
        actual.getReservationTime() == ReservationTime.AM
    }
}
