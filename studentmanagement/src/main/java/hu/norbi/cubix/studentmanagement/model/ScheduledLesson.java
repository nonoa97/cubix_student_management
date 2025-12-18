package hu.norbi.cubix.studentmanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledLesson {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    private Course course;

    private int dayOfWeek;

    private LocalTime startTime;


    private LocalTime endTime;

}