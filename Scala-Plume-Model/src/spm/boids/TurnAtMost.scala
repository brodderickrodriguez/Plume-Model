
package spm.boids

// Brodderick Rodriguez
// Auburn University - CSSE
// 19 Feb. 2019

import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import org.nlogo.api._
import spm.helper.Helper


object TurnAtMost {
    def go(uav: org.nlogo.agent.Turtle, requestedTurn: Double, maxTurnAllowed: Double): Unit = {
        
        uav.turnRight(requestedTurn)
        
//        if (requestedTurn > maxTurnAllowed)
//            if (requestedTurn > 0) uav.turnRight(-10)
//            else uav.turnRight(maxTurnAllowed)
//        else
//            if (requestedTurn > 0) uav.turnRight(requestedTurn)
//            else uav.turnRight(requestedTurn)
    } // go()
} // TurnAtMost


class TurnAtMostReporter extends Command {
    override def getSyntax: Syntax = commandSyntax(right = List(NumberType, NumberType))
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val uav = Helper.ContextHelper.getTurtle(context)
        val requestedTurn = Math.abs(Helper.getInput(args, 0).getDoubleValue)
        val maxTurnAllowed = Helper.getInput(args, 1).getDoubleValue
        
        TurnAtMost.go(uav, requestedTurn, maxTurnAllowed)
    } // perform()
} // TurnAtMostReporter
