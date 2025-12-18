package hu.norbi.cubix.studentmanagement.repository;

import hu.norbi.cubix.studentmanagement.model.SpecialDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SpecialDayRepository extends JpaRepository<SpecialDay, Integer> {


    @Query("SELECT s from SpecialDay s WHERE s.source BETWEEN :from and :until OR s.target BETWEEN :from and :until")
    List<SpecialDay> findBySourceDayOrTargetDay(LocalDate from, LocalDate until);
}
