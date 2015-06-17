package models

import java.sql.Date
import java.sql.Timestamp
import org.joda.time.DateTime
import slick.driver.MySQLDriver.api._

object Tables {
  
  val addressTable = "addresses"
  case class Address(country: String, province: String, pin: Long, desc: String, timestamp: Timestamp,
      id: Option[Long] = None)
  class Addresses(tag: Tag) extends Table[Address](tag, addressTable) {
    def country = column[String]("country")
    def province = column[String]("province")
    def pin = column[Long]("pin")
    def desc = column[String]("desc")
    def timestamp = column[Timestamp]("timestamp")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    
    def * =  (country, province, pin, desc, timestamp, id.?) <> (Address.tupled, Address.unapply _)
  }
  val addresses = TableQuery[Addresses]
  
  val usersTable = "users"
  implicit val dateTimeMapper = MappedColumnType.base[DateTime, Date](
      dateTime => new Date(dateTime.getMillis), 
      date => new DateTime(date)
    )
  case class User(uniqueId: String, email: String, password: String, timestamp: Timestamp,
                  firstName: Option[String] = None, lastName: Option[String] = None,
                  gender: Option[Char] = None,  dob: Option[DateTime] = None, addressId: Option[Long] = None,
                  bio: Option[String] = None, id: Option[Long] = None)
  class Users(tag: Tag) extends Table[User](tag, usersTable) {
    def uniqueId = column[String]("uniqueId")
    def email = column[String]("email")
    def password = column[String]("password")
    def firstName = column[String]("firstName")
    def lastName = column[String]("lastName")
    def gender = column[Char]("gender")
    def dob = column[DateTime]("dob")
    def addressId = column[Long]("address_id")
    def bio = column[String]("bio")
    def timestamp = column[Timestamp]("timestamp")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    
    def * = (uniqueId, email, password, timestamp, firstName.?, lastName.?, gender.?, dob.?, addressId.?, bio.?, id.?) <>
    (User.tupled, User.unapply _)
    
    def addressIdFk = foreignKey("userAddressIdFk", addressId, TableQuery[Addresses])(_.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade)
  }
  val users = TableQuery[Users]
  
  val verificationTable = "verifications"
  case class Verification(email: String, password: String, string: String, verified: Boolean,
                          timestamp: Timestamp, id: Option[Long] = None)
  class Verifications(tag: Tag) extends Table[Verification](tag, verificationTable) {
    def email = column[String]("email")
    def password = column[String]("password")
    def string = column[String]("string")
    def verified = column[Boolean]("verified")
    def timestamp = column[Timestamp]("timestamp")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    
    def * = (email, password, string, verified, timestamp, id.?) <> (Verification.tupled, Verification.unapply _)
  }
  val verifications = TableQuery[Verifications]

  val topicsTable = "topics"
  case class Topic(name: String, desc: String, timestamp: Timestamp, id: Option[Long] = None)
  class Tags(tag: Tag) extends Table[Topic](tag, topicsTable) {
    def name = column[String]("name")
    def desc = column[String]("desc")
    def timestamp = column[Timestamp]("timestamp")
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def * = (name, desc, timestamp, id.?) <> (Topic.tupled, Topic.unapply)
  }
  
  val placesTable = "places"
  case class Place(address: String, name: String, province: String, country: String, timestamp: Timestamp, lat: Option[Double] = None, 
      long: Option[Double] = None, id: Option[Long] = None)

  val complaintsTable = "complaints"
  case class Complaint(complainerId: Long, heading: String, isAnonymous: Boolean, privacyId: Int, timestamp: Timestamp, desc: Option[String] = None, 
      placeId: Option[Long] = None, id: Option[Long] = None)

  val complaintTopicTable = "complaintTopics"    
  case class ComplaintTopic(complaintId: Long, topicId: Long, timestamp: Timestamp, id: Option[Long] = None)
  
  val supportTable = "supports"
  case class Support(supporterId: Long, complaintId: Long, timestamp: Timestamp, id: Option[Long] = None)

  val commentTable = "comments"
  case class Comment(commenterId: Long, complaintId: Long, text: String, timestamp: Timestamp, id: Option[Long] = None)

  val reportTable = "reports"
  case class Report(reporterId: Long, complaintId: Long, reportType: Int, timestamp: Timestamp, id: Option[Long] = None)

}