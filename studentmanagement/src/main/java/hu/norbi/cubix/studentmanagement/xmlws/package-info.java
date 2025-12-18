@XmlJavaTypeAdapter(value = LocalDateXmlAdapter.class, type = LocalDate.class)
package hu.norbi.cubix.studentmanagement.xmlws;

import io.github.threetenjaxb.core.LocalDateXmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDate;