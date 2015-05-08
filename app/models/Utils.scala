package models

import java.sql.Timestamp

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

  def validRequest(email: String, randstr: String): Boolean = {
    Datastore.db.withSession { implicit  sx => {
      val query = for (verification <- Datastore.verifications
        .filter(_.email === email).filter(_.string === randstr)) yield verification
        query.firstOption match {
          case Some(verification) =>
            val thatTime = verification.timestamp
            val now = new Timestamp(new java.util.Date().getTime)
            val diff = now.getTime - thatTime.getTime
            if (thatTime.before(now) && diff > 0 && diff < 1000 * 60 * 5)  true else false
          case None => false
        }
    } }
  }

  def createUser(email: String): Unit = Datastore.db.withSession {implicit sx => {
    val query = for(verification <- Datastore.verifications.filter(_.email === email)
      .filter(_.verified === true)) yield verification
    if (query.exists.run) {
      Datastore.saveUser(email, query.first.password)
    }
  }}

  def markVerified(email: String): Unit = Datastore.db.withSession {implicit sx => {
    val query = for(verification <- Datastore.verifications.filter(_.email === email)) yield verification.verified
    query.update(true)
  }}

  def sendVerificationEmail(email: String): Unit = Datastore.db.withSession {implicit sx => {
    val query = for(verification <- Datastore.verifications.filter(_.email === email)) yield verification
    if (query.exists.run) {
      val verification = query.first
      utils.Utils.sendHtmlEmail("pamulabs@gmail.com", verification.email, "Verification Email from Grito",
        s"""<a href="http://grito.herokuapp.com/${verification.email}/${verification.string}">
           |Click to verify before link expiry</a>""".stripMargin)
    }
  }}
  
}