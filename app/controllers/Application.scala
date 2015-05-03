package controllers

import play.api.mvc.{Action, Controller}

object Application extends Controller {

  def login = Action {
    Ok(views.html.login("Login to Grito"))
  }

  def index = Action {
    //Ok(views.html.index("Grito"))
    Redirect(routes.Application.login)
  }

  def home = Action {
    Ok(views.html.home("Welcome to Home"))
  }

}