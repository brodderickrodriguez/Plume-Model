package spm.search_algorithms.symmetric_search

// Brodderick Rodriguez
// Auburn University - CSSE
// 22 Feb. 2019

import org.nlogo.api.{Argument, Command, Context}
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.ListType
import org.nlogo.api.ScalaConversions._
import spm.helper.Helper

import scala.collection.mutable.ListBuffer

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
            UavUpdateSymmetricSearchRegionTime.go(context, uav)
            regions.remove(0)
        } // while
    } // perform()
    
} // AssignUAVSubregions()