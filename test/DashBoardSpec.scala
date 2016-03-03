import controllers.DashBoardController
import models.{Employee, EmployeeModel}
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import play.api.test._
import play.api.test.Helpers._
import org.mockito.Mockito._
import scala.collection.mutable.ListBuffer
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import java.util.Date


class DashBoardSpec extends PlaySpecification with Mockito{

  "DashBoardSpec" should {
    val service = mock[EmployeeModel]
    val controller = new DashBoardController(service)

    "get all Employee details" in new WithApplication() {



      when(service.getAllEmployee).thenReturn(Future(ListBuffer(Employee("", "", new Date(), new Date(), ""))))

      val res = call(controller.getList, (FakeRequest(GET, "/")))

      status(res) must equalTo(OK)

    }

    "get specific employees details" in new WithApplication() {

      when(service.getEmployeeName("Seema")).thenReturn(Future(ListBuffer(Employee("Seema", "", new Date(), new Date(), ""))))
      val res = call(controller.getByName, (FakeRequest(GET, "/getByName")))

      status(res) must equalTo(SEE_OTHER)

    }

    "when employee does not exists" in new WithApplication() {

      when(service.getEmployeeName("simran")).thenReturn(Future(ListBuffer(Employee("simran", "", new Date(), new Date(), ""))))
      val res = call(controller.getByName, (FakeRequest(GET, "/getByName")))

      status(res) must equalTo(SEE_OTHER)

    }

    "get add employee form" in new WithApplication() {

      val res=route(FakeRequest(GET,"/renderEmployeeForm")).get
      status(res) must equalTo(OK)
    }
    "saving an employee record with correct values" in new WithApplication() {

      when(service.addEmployee("","",new Date(),new Date(),"")).thenReturn(Future(true))

      val res=call(controller.addEmp,(FakeRequest(POST,"/addEmp")))

      status(res) must equalTo(SEE_OTHER)
    }

    "saving an employee record with incorrect values" in new WithApplication() {

      when(service.addEmployee("","",new Date(),new Date(),"")).thenReturn(Future(false))

      val res=call(controller.addEmp,(FakeRequest(POST,"/addEmp")))

      status(res) must equalTo(SEE_OTHER)
    }

    "get employee data to be updated in form" in new WithApplication(){

      when(service.getEmployeeName("")).thenReturn(Future(ListBuffer(Employee("", "", new Date(), new Date(), ""))))
      val res=call(controller.editEmployee(""),FakeRequest(GET,"/editEmployee/''"))
      status(res)must equalTo(OK)
    }
    "Update employee record" in new WithApplication() {

      when(service.addEmployee("","",new Date(),new Date(),"")).thenReturn(Future(true))

      val res=call(controller.updateEmp,FakeRequest(POST,"/getEmp"))
    }

    "Delete an employee record" in new WithApplication(){
      when(service.deleteEmployee("")).thenReturn(true)
      val res=call(controller.deleteEmployee,FakeRequest(GET,"/deleteEmp"))
    }

  }
}
