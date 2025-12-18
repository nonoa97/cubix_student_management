package hu.norbi.cubix.studentmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Cacheable
@Audited
public class Student extends StudentManagementUser {

    private int centralId;
    private int semester;
    private int freeSemestersUsed;
    private int balance;

    @Override
    public UserType getUserType() {
        return UserType.STUDENT;
    }
}
