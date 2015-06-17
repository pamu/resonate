package models

import java.net.URI
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

object DB {
  val uri = new URI(s"""postgres://pdfadgyjhqnnln:zcDy12Sp9maEenok4V_tTgcAc-@ec2-54-225-154-5.compute-1.amazonaws.com:5432/d2it67fp1ug27a""")

  val username = uri.getUserInfo.split(":")(0)
  
  val password = uri.getUserInfo.split(":")(1)
  
  /**
  lazy val db = Database.forURL(
     driver = "org.postgresql.Driver",
     url = "jdbc:postgresql://" + uri.getHost + ":" + uri.getPort + uri.getPath, user = username,
     password = password
    )
    * 
    */

  lazy val db = Database.forURL(
    url = s"jdbc:mysql://localhost/grito",
    driver = "com.mysql.jdbc.Driver",
    user="root",
    password="root")
 
  def init: Future[Unit] = {
    db.run(DBIO.seq((Tables.verifications.schema ++ Tables.addresses.schema ++ Tables.users.schema).create))
  }
}