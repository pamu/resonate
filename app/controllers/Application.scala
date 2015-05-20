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
    Ok(views.html.login(loginForm)(request.flash))
  }
  
  def loginTarget() = Action.async { implicit request =>
    Future {
      loginForm.bindFromRequest().fold(
         hasErrors => BadRequest(views.html.login(hasErrors)(flash)),
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
    Ok(views.html.signup(signupForm)(request.flash))
  }
  
  def signupTarget() = Action.async { implicit request =>
    Future {
      signupForm.bindFromRequest().fold(
        hasErrors => BadRequest(views.html.signup(hasErrors)(flash)),
        success => {
          //Datastore.saveUser(success._1, success._2._1)
          try {
            Datastore.saveVerification(success._1, success._2._1)
            sendVerificationEmail(success._1)
          }
          catch {case ex: Exception => Ok("error")}
          Redirect(routes.Application.signup()).flashing("success" -> "verification email sent")
        }
        )
    }
  }

  def verify(email: String, randstr: String) = Action.async { implicit request =>
    Future {
      if (models.Utils.validRequest(email, randstr)) {
        //create user from verification
        markVerified(email)
        createUser(email)
        Redirect(routes.Application.login()).flashing("success" -> "email verified")
      } else Redirect(routes.Application.signup()).flashing("failure" -> "session expired")
    }
  }

  def home = Action {
    Ok(views.html.home("Welcome to Home"))
  }

}