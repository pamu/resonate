package utils

import java.security.SecureRandom
import org.apache.commons.mail.{DefaultAuthenticator, HtmlEmail}

object Utils {
  def randomString: String = BigInt(130, new SecureRandom()).toString(32)
  def sendHtmlEmail(from: String, to: String, subject: String, htmlBody: String): Unit = {
    val email = new HtmlEmail()
    email.setHostName("smtp.gmail.com")
    email.setSmtpPort(465);
    email.setAuthenticator(new DefaultAuthenticator("reactive999@gmail.com", "palakurthy"))
    email.setSSLOnConnect(true);
    email.setFrom(from);
    email.addTo(to)
    email.setSubject(subject)
    email.setHtmlMsg(htmlBody)
    email.send()
  }
}