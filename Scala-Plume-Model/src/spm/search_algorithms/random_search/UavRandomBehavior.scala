package spm.search_algorithms.random_search

// Brodderick Rodriguez
// Auburn University - CSSE
// 13 Feb. 2019

import scala.util.Random
import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import spm.helper.{Helper, MathHelper}
import spm.uav_behavior.CheckBoundsUav


object UavRandomBehavior {
    def behave(context: Context, uav: org.nlogo.agent.Turtle): Unit = {
        val randomSearchTime = Helper.BreedHelper.getBreedVariable(uav, "random-search-time").asInstanceOf[Double]
        val ticks = Helper.ContextHelper.getTicks(context)
        
        if (CheckBoundsUav.uavInsideWorld(context, uav) && ticks > randomSearchTime) {
            val randomSearchMaxHeadingTime = Helper.ContextHelper.getObserverVariable(context, "random-search-max-heading-time").asInstanceOf[Double]
            val newSearchTime = Random.nextInt(randomSearchMaxHeadingTime.toInt) + ticks
            val newHeading = Random.nextInt(360) * MathHelper.random1()
            
            Helper.BreedHelper.setBreedVariable(uav, "random-search-time", newSearchTime.toLogoObject)
            Helper.BreedHelper.setBreedVariable(uav, "desired-heading", newHeading.toLogoObject)
        } // if
    } // behave()
} // UavRandomBehavior()



