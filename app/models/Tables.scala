package models

import java.sql.Date
import java.sql.Timestamp
import scala.slick.driver.PostgresDriver.simple._
import org.joda.time.DateTime

object Tables {
  val addressTable = "addresses"
  case class Address(country: String, province: String, pin: Long, desc: String, timestamp: Timestamp,
      id: Option[Long] = None)
  class Addresses(tag: Tag) extends Table[Address](tag, addressTable) {
    def country = column[String]("country", O.NotNull)
    def province = column[String]("province", O.NotNull)
    def pin = column[Long]("pin", O.NotNull)
    def desc = column[String]("desc", O.NotNull)
    def timestamp = column[Timestamp]("timestamp", O.NotNull)
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    
    def * =  (country, province, pin, desc, timestamp, id.?) <> (Address.tupled, Address.unapply _)
  }
  
  val usersTable = "users"
  implicit val dateTimeMapper = MappedColumnType.base[DateTime, Date](
      dateTime => new Date(dateTime.getMillis), 
      date => new DateTime(date)
    )
  case class User(email: String, password: String, timestamp: Timestamp, firstName: Option[String] = None, lastName: Option[String] = None, 
      gender: Option[Char] = None,  dob: Option[DateTime] = None, addressId: Option[Long] = None, 
      bio: Option[String] = None, id: Option[Long] = None)
  class Users(tag: Tag) extends Table[User](tag, usersTable) {
    def email = column[String]("email", O.NotNull)
    def password = column[String]("password", O.NotNull)
    def firstName = column[String]("firstName", O.Nullable)
    def lastName = column[String]("lastName", O.Nullable)
    def gender = column[Char]("gender", O.Nullable)
    def dob = column[DateTime]("dob", O.Nullable)
    def addressId = column[Long]("address_id", O.Nullable)
    def bio = column[String]("bio", O.Nullable)
    def timestamp = column[Timestamp]("timestamp", O.NotNull)
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    
    def * = (email, password, timestamp, firstName.?, lastName.?, gender.?, dob.?, addressId.?, bio.?, id.?) <> (User.tupled, User.unapply _)
    
    def addressIdFk = foreignKey("userAddressIdFk", addressId, TableQuery[Addresses])(_.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade)
  }
      
}