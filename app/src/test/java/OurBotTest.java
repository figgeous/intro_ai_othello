import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import static org.junit.Assert.*;

public class OurBotTest{


    @Test
    public void testMainWithValidAIPlayers() {
        String[] args = {"pp.OurBot", "provided.DumAI"}; // Replace with actual AI class names
        System.out.println("hello");
        // Run main method
        try{
            provided.Othello.main(args);
            // Thread.sleep(15000);
        } catch(Exception e){
            System.out.println("this is the rorresdasdasd!!!!!!");
            System.out.println(e);
        }
        
    }

}


