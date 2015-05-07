package models

import scala.slick.driver.PostgresDriver.simple._

object Utils {
  def checkUserDetails(email: String, password: String): Boolean = {
    if (userExists(email)) {
      Datastore.db.withSession { implicit sx => {
         val query = for(user <- Datastore.users.filter(_.password === password)) yield user
         query.exists.run
       } }
    } else false
  }
  def userExists(email: String): Boolean = {
    Datastore.db.withSession { implicit sx => {
      val query = for(user <- Datastore.users.filter(_.email === email)) yield user
      query.exists.run
    } }
  }
}