/*
 */
package edu.borzhch;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.bcel.generic.InstructionHandle;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class WaitingTable {
    static ArrayList<InstructionHandle> waitingStart = new ArrayList<>();
    static ArrayList<InstructionHandle> waitingEnd = new ArrayList<>();
    
    public static void pushWaitingStart(InstructionHandle ih) {
        waitingStart.add(ih);
    }
    
    public static void resolveWaitingStart(InstructionHandle ih) {
        for(InstructionHandle waiter : waitingStart) {
            waiter = ih;
        }
        waitingStart.clear();
    }
    
    public static void pushWaitingEnd(InstructionHandle ih) {
        waitingEnd.add(ih);
    }
    
    public static void resolveWaitingEnd(InstructionHandle ih) {
        for(InstructionHandle waiter : waitingEnd) {
            waiter = ih;
        }
        waitingEnd.clear();
    }
}
