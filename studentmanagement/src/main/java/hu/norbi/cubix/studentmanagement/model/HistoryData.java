package hu.norbi.cubix.studentmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.RevisionType;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryData<T> {

    private T data;
    private RevisionType revisionType;
    private int revision;
    private Date date;
}
