package hu.norbi.centraleducationsystem.ws;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class StudentXmlWsImpl implements StudentXmlWs {

    public int getFreeSemesterUsed(int centralId) {

        Random random = new Random();
//
//        if (random.nextInt(2) == 0) {
//            return random.nextInt(8);
//        } else {
//            throw new RuntimeException("External system error for centralId: " + centralId);
//        }

        return random.nextInt(8);

    }

}
