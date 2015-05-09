package global

import play.api.{Logger, Application, GlobalSettings}
import models.Datastore._

/**
 * Created by pamu on 17/4/15.
 */
object GritoGlobal extends GlobalSettings {
  override def onStart(app: Application): Unit = {
    super.onStart(app)
    Logger.info("Grito Started")
    //clean
    //createInCase
  }
  override def onStop(app: Application): Unit = {
    super.onStop(app)
    Logger.info("Grito Stopped")
  }
}
