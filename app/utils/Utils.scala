package utils

import java.security.SecureRandom
import org.apache.commons.mail.{DefaultAuthenticator, HtmlEmail}
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import constants.Constants

object Utils {
  
  def randomString: String = BigInt(130, new SecureRandom()).toString(32)
  
  lazy val email = {
    val email = new HtmlEmail()
    email setHostName "smtp.gmail.com"
    email setSmtpPort 465
    email setAuthenticator new DefaultAuthenticator(Constants.emailCredentials.email, Constants.emailCredentials.password)
    email setSSLOnConnect true
    email
  }
  
  def sendHtmlEmail(from: String, to: String, subject: String, htmlBody: String): Future[Unit] = {
    Future {
      email setFrom from
      email addTo to
      email setSubject subject
      email setHtmlMsg htmlBody
      email send
    }
  }
  
}