package constants

case class EmailCredentials(email: String, password: String)

object Constants {
  val emailCredentials = EmailCredentials("reactive999@gmail.com", "palakurthy")
}