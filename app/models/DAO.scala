package models

import java.sql.Timestamp
import java.util.Date

import models.Tables.{Verification, User}
import slick.driver.MySQLDriver.api._
import utils.Utils
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object DAO {

  /** Know if user with given email id exists
   *
   * @param email
   * @return
   */
  def userWithEmailExists(email: String): Future[Boolean] = {
    val q = for(user <- Tables.users.filter(_.email === email)) yield user
    DB.db.run(q.exists.result)
  }

  /** know if user with given id exists
   *
   * @param id
   * @return
   */
  def userWithIdExists(id: String): Future[Boolean] = {
    val q = for(user <- Tables.users.filter(_.uniqueId === id)) yield user
    DB.db.run(q.exists.result)
  }

  /** Authentication check
   *
   * @param email
   * @param password
   * @return
   */
  def authUser(email: String, password: String): Future[Boolean] = {
    val q = for(user <- Tables.users.filter(_.email === email).filter(_.password === password)) yield user
    DB.db.run(q.exists.result)
  }

  /** Get user using email
   *
   * @param email
   * @return
   */
  def getUserWithEmail(email: String): Future[User] = {
    val q = for(user <- Tables.users.filter(_.email === email)) yield user
    DB.db.run(q.result).map(_.head)
  }

  /** Get user is using Id
   *
   * @param id
   * @return
   */
  def getUserWithId(id: String): Future[User] = {
    val q = for(user <- Tables.users.filter(_.uniqueId === id)) yield user
    DB.db.run(q.result).map(_.head)
  }

  /** getUser using email and password
   *
   * @param email
   * @param password
   * @return
   */
  def getUser(email: String, password: String): Future[User] = {
    val q = for(user <- Tables.users.filter(_.email === email).filter(_.password === password)) yield user
    DB.db.run(q.result).map(_.head)
  }

  /** Check if verification with this email exists
   *
   * @param email
   * @return
   */
  def verificationExists(email: String): Future[Verification] = {
    val q = for(verification <- Tables.verifications.filter(_.email === email)) yield verification
    DB.db.run(q.result).map(_.head)
  }

  /** Save Verification info
   *
   * @param email
   * @param password
   * @return
   */
  def saveVerification(email: String, password: String): Future[Int] = {
    val verification = Verification(email, password, s"${Utils.randomString}-$email", false,
      new Timestamp((new Date().getTime)))
    DB.db.run(Tables.verifications += verification)
  }

  /** Save User info
   *
   * @param uniqueId
   * @param email
   * @param password
   * @return
   */
  def saveUser(uniqueId: String, email: String, password: String): Future[Int] = {
    val user = User(uniqueId, email, password, new Timestamp(new Date().getTime))
    DB.db.run(Tables.users += user)
  }

  /** Send Verification Email
   *
   * @param email
   * @return
   */
  def sendVerificationEmail(email: String): Future[Unit] = {
    val q = for(verification <- Tables.verifications.filter(_.email === email)) yield verification
    DB.db.run(q.result).map(_.head).flatMap { verification => {
      Utils.sendHtmlEmail("pamulabs@gmail.com", verification.email, "Verification Email from Grito",
        s"""<a href="http://grito.herokuapp.com/verification/${verification.email}/${verification.string}">
           |Click to verify before link expiry</a>""".stripMargin)
    }}
  }

  /** Mark Verified
   *
   * @param email
   * @return
   */
  def markVerified(email: String): Future[Int] = {
    val q = for(verification <- Tables.verifications.filter(_.email === email)) yield verification.verified
    DB.db.run(q.update(true))
  }

  /** Create Verified User
   *
   * @param email
   * @return
   */
  def createVerifiedUser(email: String): Future[Int] = {
    val q = for(verification <- Tables.verifications.filter(_.email === email)
      .filter(_.verified === true)) yield verification
    DB.db.run(q.result).map(_.head).flatMap { verification => {
      saveUser(s"""${Utils.randomString}-${verification.email}""", verification.email, verification.password)
    }}
  }

  /** isValidRequest
   *
   * @param email
   * @param string
   * @return
   */
  def isValidRequest(email: String, string: String): Future[Boolean] = {
    val q = for (verification <- Tables.verifications.filter(_.email === email).filter(_.string === string))
      yield verification
    DB.db.run(q.exists.result)
  }
}