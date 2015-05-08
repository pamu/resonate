package controllers

import play.api.libs.json.{JsError, JsSuccess, JsPath, Reads}
import play.api.mvc.{Action, Controller}
import play.api.data.Form
import play.api.data.Forms._
import models.Utils._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import models.Datastore

object Application extends Controller {

  def index = Action {
    //Ok(views.html.index("Grito"))
    Redirect(routes.Application.login)
  }
  
  val loginForm = Form(
      tuple (
          "email" -> email,
          "password" -> nonEmptyText(minLength = 6)
          ).verifying(
              "error.login_failed",
              data => checkUserDetails(data._1, data._2)
              )
      )
  
  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }
  
  def loginTarget() = Action.async { implicit request =>
    Future {
      loginForm.bindFromRequest().fold(
         hasErrors => BadRequest(views.html.login(hasErrors)), 
         success => Redirect(routes.Application.home)
         ) 
    }
  }
  
  val signupForm = Form(
      tuple(
          "email" -> email,
          "passwords" -> tuple(
              "main_password" -> nonEmptyText(minLength = 6),
              "confirm_password" -> nonEmptyText(minLength = 6)
              ).verifying("error.passwords_do_not_match", data => data._1 == data._2)
          ).verifying("error.signup_failed", data => ! userExists(data._1))
      )
      
  def signup() = Action { implicit request =>
    Ok(views.html.signup(signupForm))
  }
  
  def signupTarget() = Action.async { implicit request =>
    Future {
      signupForm.bindFromRequest().fold(
        hasErrors => BadRequest(views.html.signup(hasErrors)),
        success => {
          //Datastore.saveUser(success._1, success._2._1)
          Datastore.saveVerification(success._1, success._2._1)
          Redirect(routes.Application.login)
        }
        )
    }
  }

  def verify(email: String, randstr: String) = Action.async { implicit request =>
    Future {
      if (models.Utils.timedout(email, randstr)) {
        Redirect(routes.Application.signup())
      } else {
        //create user from verification
        Redirect(routes.Application.login())
      }
    }
  }

  def home = Action {
    Ok(views.html.home("Welcome to Home"))
  }

}