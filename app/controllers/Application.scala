package controllers

import play.api.mvc.{Action, Controller}
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Application extends Controller with Secured {

  def index = withAuth(parse.anyContent) { id => request =>
    Future(Ok(""))
  }

  def home = withUser(parse.anyContent) { id => request =>
    Future(Ok(views.html.home("Welcome to Home")))
  }

}