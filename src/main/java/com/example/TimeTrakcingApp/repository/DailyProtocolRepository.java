package com.example.TimeTrakcingApp.repository;

import com.example.TimeTrakcingApp.entity.DailyProtocol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DailyProtocolRepository extends JpaRepository<DailyProtocol, Long> {
    List<DailyProtocol> getProtocolByProtocolDate(String protocolDate);

    //    @Query("SELECT dp FROM DailyProtocol dp WHERE YEARWEEK(dp.protocolDate, 1) = :weekNumber")
//    List<DailyProtocol> findDailyProtocolsByWeekNumber(@Param("weekNumber") int weekNumber);
    @Query("SELECT dp FROM DailyProtocol dp WHERE WEEK(dp.protocolDate) = :weekNumber")
    List<DailyProtocol> findAllByWeekNumber(@Param("weekNumber") int weekNumber);
}
