package models

import java.text.{SimpleDateFormat, DateFormat}
import java.util.Date
import com.google.inject.ImplementedBy
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


case class Employee(name:String,address:String,dob:Date,doj:Date,designation:String)

@ImplementedBy(classOf[EmployeeService])
trait EmployeeModel {

  def getAllEmployee:Future[ListBuffer[Employee]]
  def getEmployeeName(ename:String):Future[ListBuffer[Employee]]
  def addEmployee(ename:String,eaddress:String,edob:Date,edoj:Date,edesig:String):Future[Boolean]
  def deleteEmployee(ename:String):Boolean
  def updateEmployee(ename:String,eaddress:String,edob:Date,edoj:Date,edesig:String):Boolean

}

class EmployeeService extends EmployeeModel{

//  val empList:ListBuffer[Employee]=ListBuffer(Employee("Pallavi","Ashok Vihar",new Date(1992-3-9),new Date(2016-3-9),"Manager"))
val empList:ListBuffer[Employee]=ListBuffer(Employee("Seema","Ashok Vihar",new SimpleDateFormat("yyyy-MMM-dd").parse("1992-Mar-09"),new SimpleDateFormat("yyyy-MMM-dd").parse("2016-Jan-21"),"Software Consultant"),Employee("Himani ","Lakshmi Nagar",new SimpleDateFormat("yyyy-MMM-dd").parse("1992-May-09"),new SimpleDateFormat("yyyy-MMM-dd").parse("2016-Jan-21"),"Software Consultant"),Employee("Pallavi","Ashok Vihar",new SimpleDateFormat("yyyy-MMM-dd").parse("1992-Mar-09"),new SimpleDateFormat("yyyy-MMM-dd").parse("2016-Jan-21"),"Software Consultant"))

  def getAllEmployee:Future[ListBuffer[Employee]]=Future{
    empList
  }

  def getEmployeeName(ename:String):Future[ListBuffer[Employee]]=Future{
    empList.filter(emp=>emp.name==ename)
  }

  def addEmployee(ename:String,eaddress:String,edob:Date,edoj:Date,edesig:String):Future[Boolean] =Future {
    empList.append(Employee(ename,eaddress,edob,edoj,edesig))
    true
  }
  def deleteEmployee(ename:String):Boolean={
    if(empList.map(_.name).contains(ename)) {

      empList.remove(empList.map(_.name).indexOf(ename))
      true
    }
    false
  }
  def updateEmployee(ename:String,eaddress:String,edob:Date,edoj:Date,edesig:String):Boolean={

    if(empList.map(_.name).contains(ename)) {

      empList.update(empList.map(_.name).indexOf(ename),Employee(ename,eaddress,edob,edoj,edesig))
      true
    }
   false
  }


}
