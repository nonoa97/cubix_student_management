package hu.norbi.centraleducationsystem.config;

import hu.norbi.centraleducationsystem.ws.StudentXmlWs;
import jakarta.xml.ws.Endpoint;
import lombok.RequiredArgsConstructor;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebServiceConfig {

    private final Bus bus;
    private final StudentXmlWs studentXmlWs;

    @Bean
    Endpoint endpoint(){
        EndpointImpl endpoint = new EndpointImpl(bus, studentXmlWs);
        endpoint.publish("/student");
        return endpoint;
    }

}
