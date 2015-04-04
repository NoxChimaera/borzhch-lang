/*
 */
package edu.borzhch;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionHandle;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class WaitingTable {
    static ArrayList<GOTO> waitingStart = new ArrayList<>();
    static ArrayList<GOTO> waitingEnd = new ArrayList<>();
    
    public static void pushWaitingStart(GOTO go) {
        waitingStart.add(go);
    }
    
    public static void resolveWaitingStart(InstructionHandle ih) {
        for(GOTO waiter : waitingStart) {
            waiter.setTarget(ih);
        }
        waitingStart.clear();
    }
    
    public static void pushWaitingEnd(GOTO go) {
        waitingEnd.add(go);
    }
    
    public static void resolveWaitingEnd(InstructionHandle ih) {
        for(GOTO waiter : waitingEnd) {
            waiter.setTarget(ih);
        }
        waitingEnd.clear();
    }
}
