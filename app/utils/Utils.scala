package utils

import java.security.SecureRandom
import com.typesafe.plugin._
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Utils {
  def randomString: String = BigInt(130, new SecureRandom()).toString(32)
  def sendHtmlEmail(from: String, to: String, subject: String, htmlBody: String): Unit = {
    val email = use[MailerPlugin].email
    email.setFrom(from)
    email.setRecipient(to)
    email.setSubject(subject)
    email.sendHtml(htmlBody)
  }
}