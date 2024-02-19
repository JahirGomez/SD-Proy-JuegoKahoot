package User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class KahootGame {
    private static ArrayList<Vector<String>> questions = new ArrayList<>();
    private Vector<Integer> amountQuestion = new Vector<>();

    KahootGame(String filename) {
        try {
            int amount = 0;
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] separatedInfo = data.split(" ");
                Vector<String> questionInfo = new Vector<>(Arrays.asList(separatedInfo[0], separatedInfo[1],
                        separatedInfo[2], separatedInfo[3], separatedInfo[4]));
                amount++;
                questions.add(questionInfo);
            }

            for (int i = 0; i < amount; i++) {
                amountQuestion.add(i);
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public Vector<String> getRandomQuestion() {
        int randomNum = ThreadLocalRandom.current().nextInt(0,
                amountQuestion.size());

        int questionNum = amountQuestion.get(randomNum);
        amountQuestion.remove(randomNum);
        return questions.get(questionNum);
    }

    public boolean isOver() {
        return amountQuestion.isEmpty();
    }

}
