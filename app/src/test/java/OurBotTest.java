import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OurBotTest{

    int winner;                     //0 = draw, 1=black, 2=white.
    int millisecondsDelay = 500;    //How frequently are we gonna check if game has terminated.
    int maxGameTime = 60 * 1000;     //Total time we allow game to run in millieseconds.
    int timeElapsed;                //estimate for how long game has run in millieseconds
  
    @Before
    public void BeforeTest(){
        winner = -1;
        timeElapsed = 0;
        Provided.Othello.resetGame();
    }

    @Test
    public void WinningAsBlack_n8() throws InterruptedException{
        String[] args = {"PythonProgrammers.OurBot","Provided.DumAI", "8"};
        // Run main method    
        Thread gameThread =  new Thread(() -> Provided.Othello.main(args));
        gameThread.start();
        while(((winner = Provided.Othello.getWinner()) <0) && timeElapsed < maxGameTime){
            Thread.sleep(millisecondsDelay);
            timeElapsed += millisecondsDelay;
        };
        assertTrue(winner == 1);
    }

    @Test
    public void WinningAsWhite_n8() throws InterruptedException{
        String[] args = {"Provided.DumAI", "PythonProgrammers.OurBot", "8"};
        // Run main method    
        Thread gameThread =  new Thread(() -> Provided.Othello.main(args));
        gameThread.start();
        while(((winner = Provided.Othello.getWinner()) <0) && timeElapsed < maxGameTime){
            Thread.sleep(millisecondsDelay);
            timeElapsed += millisecondsDelay;
        };
        assertTrue(winner == 2);
    }

}


