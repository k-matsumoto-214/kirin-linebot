package com.kirin.linebot.repository.database.impl

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import com.kirin.linebot.model.ReservationType
import com.kirin.linebot.model.type.ReservationName
import com.kirin.linebot.model.type.ReservationTime
import com.kirin.linebot.repository.database.CsvDataSetLoader
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import spock.lang.Specification

import java.time.LocalDate

@MybatisTest
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners([
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
])
@Import(ReservationRepositoryImpl.class)
class ReservationRepositoryImplSpec extends Specification {

    @Autowired
    ReservationRepositoryImpl reservationRepositoryImpl

    @DatabaseSetup(
            value = "/h2/data/reservationrepository/insertReservationDate/input",
            type = DatabaseOperation.CLEAN_INSERT
    )
    @ExpectedDatabase(
            value = "/h2/data/reservationrepository/insertReservationDate/output",
            assertionMode = DatabaseAssertionMode.NON_STRICT,
            table = "reservation_date")
    def "insertReservationDate_DBに登録できる"() {
        setup:
        def reservationDate = LocalDate.of(2023, 12, 15)
        def reservationType = ReservationType.of(ReservationName.KYO, ReservationTime.AM)

        when:
        reservationRepositoryImpl.insertReservationDate(reservationDate, reservationType)

        then:
        noExceptionThrown()
    }

    @DatabaseSetup(
            value = "/h2/data/reservationrepository/findReservation/input",
            type = DatabaseOperation.CLEAN_INSERT
    )
    def "findReservation_DBから取得できる"() {
        setup:
        def reservationDate = LocalDate.of(2022, 12, 15)
        def reservationType = ReservationType.of(ReservationName.NAO, ReservationTime.PM)

        when:
        def actual = reservationRepositoryImpl.findReservation(reservationDate, reservationType)

        then:
        noExceptionThrown()
        actual.isEmpty() == false
        actual.getReservationTime() == ReservationTime.PM
        actual.getName() == ReservationName.NAO.getValue()
        actual.getDate() == reservationDate
    }

    @DatabaseSetup(
            value = "/h2/data/reservationrepository/findReservation/input",
            type = DatabaseOperation.CLEAN_INSERT
    )
    def "findReservation_DBから取得できない"() {
        setup:
        def reservationDate = LocalDate.of(2099, 12, 15)
        def reservationType = ReservationType.of(ReservationName.NAO, ReservationTime.PM)

        when:
        def actual = reservationRepositoryImpl.findReservation(reservationDate, reservationType)

        then:
        noExceptionThrown()
        actual.isEmpty() == true
    }
}
