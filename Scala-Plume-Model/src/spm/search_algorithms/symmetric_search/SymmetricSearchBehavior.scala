package spm.search_algorithms.symmetric_search

// Brodderick Rodriguez
// Auburn University - CSSE
// 05 Feb. 2019

import scala.collection.mutable.ListBuffer
import org.nlogo.core.LogoList
import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax._
import spm.boids.TurnTowards
import spm.helper.Helper
import spm.uav_behavior.{CheckBoundsUav, ComputeHeading}


object _UAVSubregionGenerator {
    object OptimalSubregionDimensionsCompute {
        def get(n: Int): (Int, Int, Int) = {
            var optimal = (0, 0, Int.MaxValue)
            for (y <- Range(1, (n / 2) + 1) if n % y == 0) {
                val x = n / y
                val cost = Math.abs(x - y)
                if (cost < optimal._3) optimal = (x, y, cost)
            }
            optimal
        } // get()
    } // _OptimalSubregionDimensions
    
    def buildRegions(args: Array[Argument], context: Context): ListBuffer[List[Double]] = {
        val population = Helper.ContextHelper.getObserverVariable(context, "population").asInstanceOf[Double].toInt
        val SubregionDimensions = OptimalSubregionDimensionsCompute.get(population)
        val (worldWidth, worldHeight): (Double, Double) = (context.world.worldWidth - 1, context.world.worldHeight - 1)
        val (regionWidth, regionHeight) = (worldWidth / SubregionDimensions._1, worldHeight / SubregionDimensions._2)
        var (x, y) = (0.0, 0.0)
        val regions = new ListBuffer[List[Double]]()
        
        while (y <= worldHeight) {
            while (Math.round(x) < worldWidth) {
                regions.append(List(x, y, x + regionWidth, y + regionHeight))
                x += regionWidth
            } // while x
            y += regionHeight
            x = 0
        } // while y
        regions
    } // buildRegions()
} // _UAVSubregionGenerator


class UAVRegionSetup extends Command {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(), ret = ListType)
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val regions = _UAVSubregionGenerator.buildRegions(args, context)
        val it = Helper.ContextHelper.getWorld(context).getBreed("UAVS").iterator
        
        while (it.hasNext) {
            val uav = it.next().asInstanceOf[org.nlogo.agent.Turtle]
            val subregion = regions.head.toLogoList
            Helper.BreedHelper.setBreedVariable(uav, "UAV-region", subregion)
            regions.remove(0)
        } // while
    } // perform()
    
} // AssignUAVSubregions()


object _UavUpdateSymmetricSearchIndividual {
    def behave(context: Context, uav: org.nlogo.agent.Turtle): Unit = {
        val threshold = Helper.ContextHelper.getObserverVariable(context, "symmetric-search-region-threshold").asInstanceOf[Double]
        val uavRegion = Helper.BreedHelper.getBreedVariable(uav, "UAV-region").asInstanceOf[LogoList].toList.map(_.asInstanceOf[Double])
        val maxTurn = Helper.ContextHelper.getObserverVariable(context, "symmetric-search-max-turn").asInstanceOf[Double]
    
        if (CheckBoundsUav.uavInsideWorld(context, uav)) {
            if (!spm.uav_behavior.CheckBoundsUav.uavInside(uav, threshold, uavRegion)) {
                val (regionCenterX, regionCenterY) = ((uavRegion(2) + uavRegion.head) / 2, (uavRegion(3) + uavRegion(1)) / 2)
                val newDesiredHeading = ComputeHeading.get(uav, regionCenterX, regionCenterY) - 180
        
                Helper.BreedHelper.setBreedVariable(uav, "desired-heading", newDesiredHeading.toLogoObject)
            } else
                spm.search_algorithms.random_search._UavRandomSearchBehavior.behave(context, uav)
        }
        
        spm.uav_behavior.TurnUav.go(uav, context, maxTurn)
    } // behave()
} // _UavUpdateSymmetricSearch


class UavUpdateSymmetricSearchIndividual extends Command {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(), ret = ListType)
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val maxTurn = Helper.ContextHelper.getObserverVariable(context,"symmetric-search-max-turn").asInstanceOf[Double]
        val uav = Helper.ContextHelper.getTurtle(context)
        _UavUpdateSymmetricSearchIndividual.behave(context, uav)
//        spm.uav_behavior.TurnUav.go(uav, context, maxTurn)
    } // perform()
} // UavUpdateSymmetricSearchIndividual


class UavUpdateSymmetricSearch extends Command {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(), ret = ListType)
    
    override def perform(args: Array[Argument], context: Context): Unit = {
        val maxTurn = Helper.ContextHelper.getObserverVariable(context,"symmetric-search-max-turn").asInstanceOf[Double]
        val it = Helper.ContextHelper.getWorld(context).getBreed("UAVS").iterator
        
        while (it.hasNext) {
            val uav = it.next().asInstanceOf[org.nlogo.agent.Turtle]
            _UavUpdateSymmetricSearchIndividual.behave(context, uav)
//            spm.uav_behavior.TurnUav.go(uav, context, maxTurn)
        } // while
        
    } // perform()
} // UavUpdateSymmetricSearch
