package com.kirin.linebot.model.type

import spock.lang.Specification

class ReservationTimeSpec extends Specification {
    def "from_ファクトリ"() {
        when:
        def actual = ReservationTime.from(testValue)

        then:
        actual == expected

        where:
        testValue | expected
        "0"       | ReservationTime.AM
        "1"       | ReservationTime.PM
    }
}
