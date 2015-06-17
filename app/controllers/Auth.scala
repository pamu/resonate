package controllers

import models.DAO
import play.api.Logger
import play.api.mvc.{Results, Controller, Action}
import play.api.data.Form
import play.api.data.Forms._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Auth extends Controller {
  
  val loginForm = Form(
      tuple (
          "email" -> email,
          "password" -> nonEmptyText(minLength = 6)
          )
      )
  
  def login = Action { implicit request =>
    Ok(views.html.login(loginForm)(request.flash))
  }
  
  def loginPost() = Action.async { implicit request =>
    loginForm.bindFromRequest().fold(
      hasErrors => Future(BadRequest(views.html.login(hasErrors)(flash))),
      success => {
        val (email, password) = success
        DAO.userWithEmailExists(email).flatMap { exists => {
          if(exists) {
            DAO.getUser(email, password).map { user => {
              Redirect(routes.Application.home).withNewSession.withSession("id" -> user.uniqueId)
            }}.recover { case th => Redirect(routes.Auth.login()).withNewSession.flashing("failure" -> "Authentication Failed.")}
          } else {
            Future(Redirect(routes.Auth.login).withNewSession.flashing("failure" -> "User doesn't exist."))
          }
        }}
      }
    )
  }
  
  val signupForm = Form(
      tuple(
          "email" -> email,
          "passwords" -> tuple(
              "main_password" -> nonEmptyText(minLength = 6),
              "confirm_password" -> nonEmptyText
              )
          )
      )
      
  def signup() = Action { implicit request =>
    Ok(views.html.signup(signupForm)(request.flash))
  }
  
  def signupPost() = Action.async { implicit request =>
    signupForm.bindFromRequest().fold(
      hasErrors => Future(BadRequest(views.html.signup(hasErrors)(flash))),
      success => {
        val (email, passwords) = success
        val (a, b) = passwords
        if (a == b) {
          DAO.userWithEmailExists(email).flatMap { exists => {
            if (exists) {
              Future(Redirect(routes.Auth.signup).withNewSession.flashing("failure" -> "Email already exists."))
            } else {
              DAO.verificationExists(email).flatMap { verification => {
                if (verification.verified) {
                  DAO.createVerifiedUser(verification.email).map {status => {
                    if (status > 0) {
                      Redirect(routes.Auth.login()).withNewSession.flashing("success" -> "Successful. Please login.")
                    } else {
                      Redirect(routes.Auth.signup()).flashing("failure" -> "Sending Email Failed :(. Try again.")
                    }
                  }}.recover {case th => Redirect(routes.Auth.signup()).withNewSession.flashing("failure" -> "Sending Email Failed :(. Try again.") }
                } else {
                  DAO.sendVerificationEmail(email).map { unit => {
                    Logger.info("email sent")
                    Redirect(routes.Auth.signup()).withNewSession.flashing("success" -> "verification email sent.")
                  }}.recover {case th => {
                    Logger.info(s"email sending failed ${th.getMessage}")
                    Redirect(routes.Auth.signup()).withNewSession.flashing("failure" -> "Sending Email Failed :(. Try again.")
                  }}
                }
              }}.recoverWith {case th => {
                Logger.info(s" message $th ")
                th match {
                  case x if x.isInstanceOf[UnsupportedOperationException] =>
                    DAO.saveVerification(email, a).flatMap { status => {
                      if (status > 0) {
                        DAO.sendVerificationEmail(email).map { unit => {
                          Logger.info("email sent")
                          Redirect(routes.Auth.signup()).withNewSession.flashing("success" -> "verification email sent.")
                        }}.recover { case th => {
                          Logger.info(s"email sending failed ${th.getMessage}")
                          Redirect(routes.Auth.signup()).withNewSession.flashing("failure" -> "Sending Email Failed :(. Try again.")
                        }}
                      } else {
                        Future(Redirect(routes.Auth.signup).withNewSession.flashing("failure" -> "Something went wrong :(. Please try again."))
                      }
                    }
                    }
                  case _ =>
                    Future(Redirect(routes.Auth.signup).withNewSession.flashing("failure" -> "Something went wrong :( try again."))
                }
              }}
            }
          }}.recover { case throwable => Redirect(routes.Auth.signup).withNewSession.flashing("failure" -> "Something went wrong. Please reload.")}
        } else {
          Future(Redirect(routes.Auth.signup()).withNewSession.flashing("failure" -> "Passwords do not match."))
        }
      })
  }

  def verify(email: String, string: String) = Action.async { implicit request =>
    DAO.isValidRequest(email, string).flatMap { isValid => {
      if (isValid) {
        DAO.createVerifiedUser(email).map { status => {
          if (status > 0) {
            Redirect(routes.Auth.login).withNewSession.flashing("success" -> "Email verified.")
          } else {
            Ok(views.html.error("Something went wrong :(. Please try signing up."))
          }
        }}.recover { case th => Ok(views.html.error("Something went wrong :(. Please try signing up."))}
      } else {
        Future(Redirect(routes.Auth.signup).withNewSession.flashing("failure" -> "Invalid Request. Try to signup again."))
      }
    }}.recover {case th => Ok(views.html.error("Something went wrong :(. Please reload."))}
  }
}