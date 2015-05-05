package controllers

import play.api.libs.json.{JsError, JsSuccess, JsPath, Reads}
import play.api.mvc.{Action, Controller}

object Application extends Controller {

  def login = Action {
    Ok(views.html.login("Login to Grito"))
  }

  def index = Action {
    //Ok(views.html.index("Grito"))
    Redirect(routes.Application.login)
  }

  case class FbUser(email: String)
  implicit val reads: Reads[FbUser] = (
    (JsPath \ "email").read[String].map(email =>  FbUser(email))
    )

  def fbHome = Action(parse.json) { implicit request =>
    request.body.validate[FbUser] match {
      case success: JsSuccess[FbUser] => Ok(s"hello ${success.get.email}")
      case failure: JsError => Ok("")
    }
  }

  def home = Action {
    Ok(views.html.home("Welcome to Home"))
  }

}