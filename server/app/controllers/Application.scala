package controllers

import javax.inject._

import shared.SharedMessages
import play.api.mvc._

@Singleton
class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action { implicit request =>
    // Old
    // Ok(views.html.index(SharedMessages.itWorks))
    Ok(views.html.index())
  }

}
