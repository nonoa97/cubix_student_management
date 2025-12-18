package hu.norbi.cubix.studentmanagement.xmlws;

import jakarta.xml.ws.Endpoint;
import lombok.RequiredArgsConstructor;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class XmlWebServiceConfig {

    private final Bus bus;
    private final SearchScheduleXmlWs searchScheduleXmlWs;

    @Bean
    Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, searchScheduleXmlWs);
        endpoint.publish("/schedule");
        return endpoint;
    }

}