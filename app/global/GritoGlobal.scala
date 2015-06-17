package global

import models.DB
import play.api.{Logger, Application, GlobalSettings}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.util.{Failure, Success}

/**
 * Created by pamu on 17/4/15.
 */
object GritoGlobal extends GlobalSettings {
  override def onStart(app: Application): Unit = {
    super.onStart(app)
    Logger.info("Grito Started")
    DB.init onComplete {
      case Success(x) => Logger.info("Init complete")
      case Failure(x) => Logger.info(s"Init failed due to ${x.getMessage}")
    }
  }
  override def onStop(app: Application): Unit = {
    super.onStop(app)
    Logger.info("Grito Stopped")
  }
}
