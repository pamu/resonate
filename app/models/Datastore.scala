package models

import scala.slick.driver.PostgresDriver.simple._
import java.net.URI
import models.Tables._
import java.util.Date
import java.sql.Timestamp

object Datastore {
  val uri = new URI("""postgres://pdfadgyjhqnnln:zcDy12Sp9maEenok4V_tTgcAc-@ec2-54-225-154-5.
  compute-1.amazonaws.com:5432/d2it67fp1ug27a""")

  val username = uri.getUserInfo.split(":")(0)
  
  val password = uri.getUserInfo.split(":")(1)
  
  lazy val db = Database.forURL(
     driver = "org.postgresql.Driver",
     url = "jdbc:postgresql://" + uri.getHost + ":" + uri.getPort + uri.getPath, user = username,
     password = password
    )
  
  val addresses = TableQuery[Addresses]
  val users = TableQuery[Users]
  
  def createInCase = db.withSession { implicit sx =>
    {
      import scala.slick.jdbc.meta._
      if (MTable.getTables(Tables.addressTable).list.isEmpty) {
        addresses.ddl.create
        if (MTable.getTables(Tables.usersTable).list.isEmpty) {
          users.ddl.create
        }
      }
    }
  }
  
  def saveUser(email: String, password: String): Unit = db.withSession { implicit sx => {
    val date = new Date()
    users += User(email, password, new Timestamp(date.getTime))
  }}
  
}