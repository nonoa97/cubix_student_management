package hu.norbi.cubix.studentmanagement.model;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"year", "semesterType"})
public class Semester {

    private int year;
    private SemesterType semesterType;
    @Transient private LocalDate semesterStart;
    @Transient private LocalDate semesterEnd;

    public static Semester createSemesterFrom(LocalDate day){
        Semester semester = new Semester();
        semester.setYear(day.getYear());
        int month = day.getMonthValue();

        if(month >= 9){
            semester.setSemesterType(SemesterType.FALL);
        }else if(month >= 2 && month <= 5){
            semester.setSemesterType(SemesterType.SPRING);
        }else {
            throw new IllegalArgumentException("The provided date is not within the semester period");
        }
        semester.semesterStart = getSemesterFirstMonday(semester.getYear(), semester.getSemesterType());
        semester.semesterEnd = semester.semesterStart.plusWeeks(14);
        return semester;
    }

    private static LocalDate getSemesterFirstMonday(int year, SemesterType type){
        Month month = Month.SEPTEMBER;
        if(type == SemesterType.SPRING){
            month = Month.FEBRUARY;
        }
        return LocalDate.of(year, month, 1)
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))
                .plusWeeks(1);
    }

}
