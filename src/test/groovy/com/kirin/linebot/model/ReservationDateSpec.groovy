package com.kirin.linebot.model


import com.kirin.linebot.model.type.ReservationName
import com.kirin.linebot.model.type.ReservationTime
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

    def "from_ファクトリ_引数が不正"() {
        when:
        def actual = ReservationDate.from(null)

        then:
        actual.isEmpty() == true
    }

    def "empty_空ドメイン生成"() {
        when:
        def actual = ReservationDate.empty()

        then:
        actual.getName() == ""
    }

    def "isEmpty_空ドメイン判定"() {
        when:
        def actual = ReservationDate.empty()

        then:
        actual.getName() == ""
        actual.isEmpty() == true
    }
}
