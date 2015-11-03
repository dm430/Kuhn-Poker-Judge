import java.io.File;

/**
 * Created by devin on 11/2/15.
 */
public class Driver {
    public static void main(String[] args) {
        if (args.length > 0) {
            File playerAIJar = new File(args[0]);
            Judge judge = new Judge(playerAIJar);

            try {
                judge.startGame();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.err.println("Please supply the path to the jar containing your AI");
        }
    }
}
