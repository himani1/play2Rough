package controllers

import java.text.SimpleDateFormat

import com.google.inject.Inject
import models.EmployeeModel
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import java.util.Date


import scala.concurrent.Future

case class AddEmployee(name:String,address:String,dob:Date,doj:Date,designation:String)

class DashBoardController @Inject()(emp: EmployeeModel) extends Controller{

  val filterForm=Form(
    single(
      "name"->text
    )
  )

  val addForm=Form(
    mapping (
      "name"->nonEmptyText,
      "address"->nonEmptyText,
      "dob"->date,
      "doj"->date,
      "designation"->nonEmptyText
    )(AddEmployee.apply)(AddEmployee.unapply)
  )


  def getList=Action.async{implicit request=>
    val list=emp.getAllEmployee
    list.map{values=>Ok(views.html.dashBoard(values.toList,filterForm))}

  }

  def getByName=Action.async{implicit request=>
    filterForm.bindFromRequest.fold(
      fomWithErrors=>{
        Future {
          Redirect(routes.DashBoardController.getList)
        }
      },
      formData=>{
          val nameOfEmp=emp.getEmployeeName(formData)
          nameOfEmp.map{values=>Ok(views.html.dashBoard(values.toList,filterForm))}
      }
    )
}
   def renderEmployeeForm=Action{implicit request=>
     Ok(views.html.addEmployeeView(addForm))
   }

   def addEmp=Action.async{implicit request=>
     addForm.bindFromRequest.fold(
       formWithErrors=>{
         Future{
           Redirect(routes.DashBoardController.renderEmployeeForm)
         }
       },
       formData=>{
         val nameOfEmp=emp.addEmployee(formData.name,formData.address,formData.dob,formData.doj,formData.designation)
         nameOfEmp.map{values=>Redirect(routes.DashBoardController.getList)}
       }
     )

   }

  def updateEmp=Action{implicit request=>
    addForm.bindFromRequest.fold(
      Errors=>{
        Redirect(routes.DashBoardController.getList)
      },
      userData=>{
        emp.updateEmployee(userData.name,userData.address,userData.dob,userData.doj,userData.designation)
        Redirect(routes.DashBoardController.getList)
       // Ok(views.html.dashBoard(userData,addForm))
      }
    )

  }

  def editEmployee(name:String)=Action.async{
    val nameOfEmp=emp.getEmployeeName(name)
    nameOfEmp.map{values=>
      val empp = values.head
      val v = AddEmployee(empp.name,empp.address, empp.dob, empp.doj, empp.designation)
      Ok(views.html.editEmployeeView(addForm.fill(v)))
    }

  }

  def deleteEmployee=Action{implicit request=>
    val st=request.session.get("name").get
    emp.deleteEmployee(st)
    Redirect(routes.DashBoardController.getList)
  }

}
