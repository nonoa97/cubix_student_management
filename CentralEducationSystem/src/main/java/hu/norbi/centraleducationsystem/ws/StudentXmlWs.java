package hu.norbi.centraleducationsystem.ws;

import jakarta.jws.WebService;

@WebService
public interface StudentXmlWs {

    public int getFreeSemesterUsed(int centralId);
}
