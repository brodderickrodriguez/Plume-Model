
import org.nlogo.api.ScalaConversions._
import org.nlogo.api._
import org.nlogo.core.Syntax
import org.nlogo.core.Syntax.{ListType, NumberType}


class TestReporter extends Reporter {
    override def getSyntax: Syntax = Syntax.reporterSyntax(right = List(NumberType), ret = ListType)
    
    
    def report(args: Array[Argument], context: Context): AnyRef = {
//        val n = try args(0).getIntValue

        (0 until 89).toLogoList
    }
}


/*


        val uav = Helper.ContextHelper.getAgent(context)
        
        
        Helper.BreedHelper.setBreedVariable(uav, "flockmates", "hope")
        
        val c = Helper.BreedHelper.getTurtleVariable(uav, "color").asInstanceOf[Double]
        
        Helper.BreedHelper.setTurtleVariable(uav, "color", c / 2)
        


 */