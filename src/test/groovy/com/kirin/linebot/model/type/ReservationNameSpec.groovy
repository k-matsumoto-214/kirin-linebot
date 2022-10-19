package com.kirin.linebot.model.type

import spock.lang.Specification

class ReservationNameSpec extends Specification {
    def "from_ファクトリ"() {
        when:
        def actual = ReservationName.from(testValue)

        then:
        actual == expected

        where:
        testValue | expected
        "尚大"    | ReservationName.NAO
        "匡平"    | ReservationName.KYO
    }
}
