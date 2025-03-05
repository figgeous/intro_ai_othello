import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
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
        provided.Othello.resetGame();
    }

    @Test
    public void WinningAsBlack_n6() throws InterruptedException{
        String[] args = {"pp.OurBot","provided.DumAI", "6"};
        // Run main method    
        Thread gameThread =  new Thread(() -> provided.Othello.main(args));
        gameThread.start();
        while(((winner = provided.Othello.getWinner()) <0) && timeElapsed < maxGameTime){
            Thread.sleep(millisecondsDelay);
            timeElapsed += millisecondsDelay;
        };
        assertTrue(winner == 1);
    }

    @Test
    public void WinningAsWhite_n6() throws InterruptedException{
        String[] args = {"provided.DumAI", "pp.OurBot", "6"};
        // Run main method    
        Thread gameThread =  new Thread(() -> provided.Othello.main(args));
        gameThread.start();
        while(((winner = provided.Othello.getWinner()) <0) && timeElapsed < maxGameTime){
            Thread.sleep(millisecondsDelay);
            timeElapsed += millisecondsDelay;
        };
        assertTrue(winner == 2);
    }

}


