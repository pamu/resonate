package controllers

import models.DAO
import models.Tables.User
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future

trait Secured {
  def id(requestHeader: RequestHeader) = requestHeader.session.get("id")
  def onUnauthorized(requestHeader: RequestHeader) = Results.Redirect(routes.Auth.login).withNewSession.flashing("success" -> "Please login.")
  def withAuth[A](p: BodyParser[A])(f: => String => Request[A] => Future[Result]) = Security.Authenticated(id, onUnauthorized) { id => {
    Action.async(p){ request => f(id)(request) }
  }}
  def withUser[A](p: BodyParser[A])(f: User => Request[A] => Future[Result]) = withAuth(p) { id => request => {
    DAO.getUserWithId(id).flatMap { user => f(user)(request)}.recover { case th => Results.Redirect(routes.Auth.login).withNewSession.flashing("success" -> "Please login.") }
  }}
}