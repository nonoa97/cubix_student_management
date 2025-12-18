package hu.norbi.cubix.studentmanagement.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class StudentManagementUser {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id;
    private String name;
    private LocalDate birthdate;
    private String username;
    private String password;
    private String facebookId;
    private String googleId;

    @ManyToMany
    @JoinTable(
            name = "user_course",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses = new ArrayList<>();

    public abstract UserType getUserType();

}
