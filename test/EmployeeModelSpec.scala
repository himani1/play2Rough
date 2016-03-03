import models.EmployeeService
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.test.{WithApplication, PlaySpecification}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import java.util.Date

class EmployeeModelSpec extends PlaySpecification{

  "EmployeeModelSpec" should {
    val obj = new EmployeeService


    "add an employee record" in new WithApplication(){
      val det = obj.addEmployee("Ram","Delhi",new Date(),new Date,"team lead")
      val resultAsync = Await.result(det, 60.second)
      resultAsync must equalTo(true)

    }

    "Update an employee record " in new WithApplication() {
      val res=obj.updateEmployee("Seema","",new Date(),new Date(),"")
      res must equalTo(true)
    }
    
  }

}
