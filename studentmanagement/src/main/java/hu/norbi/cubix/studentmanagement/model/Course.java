package hu.norbi.cubix.studentmanagement.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NamedEntityGraph(
        name = "Course.students"
        , attributeNodes = @NamedAttributeNode("students")
)
@NamedEntityGraph(
        name = "Course.teachers"
        , attributeNodes = @NamedAttributeNode("teachers")
)
@Audited
public class Course {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id;
    private String name;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();

    @ManyToMany(mappedBy = "courses")
    private List<Teacher> teachers = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    @NotAudited
    private List<ScheduledLesson> scheduledLessons;

    @NotAudited
    Semester semester;

}
