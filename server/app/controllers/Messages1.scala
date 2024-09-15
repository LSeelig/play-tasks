package controllers

import javax.inject._

import shared.SharedMessages
import play.api.mvc._

@Singleton
class Messages1 @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  
  // Login, main page still reachable without redirect if browser goes "back" after signing in, but actions are blocked as "unauthorized"

  def login = Action { implicit request => 
    if (request.session.get("username").isDefined) Redirect(routes.Messages1.messages)
    else Ok(views.html.login1())
  }

  def validateLogin = Action { implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args => 
      val username = args("username").head
      val password = args("password").head
      if (models.MessagesInMemoryModel.validateUser(username, password)) {
        Redirect(routes.Messages1.messages).withSession("username" -> username)
      } else {
        Redirect(routes.Messages1.login).flashing("error" -> "Invalid username/password combination.")
      }
    }.getOrElse(Redirect(routes.Messages1.login))
  }

  def createUser = Action { implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args => 
      val username = args("username").head
      val password = args("password").head
      if (models.MessagesInMemoryModel.createUser(username, password)) {
        Redirect(routes.Messages1.messages).withSession("username" -> username)
      } else {
        Redirect(routes.Messages1.login).flashing("error" -> "User creation failed.")
      }
    }.getOrElse(Redirect(routes.Messages1.login))
  }
 
  def messages = Action { implicit request => 
    val usernameOption = request.session.get("username")
    usernameOption.map { username => 
      val messages = models.MessagesInMemoryModel.getPersonal(username)
      val general = models.MessagesInMemoryModel.getGeneral()
      Ok(views.html.messages1(username, messages, general))
    }.getOrElse(Redirect(routes.Messages1.login))
  }

  def logout = Action {
    Redirect(routes.Messages1.login).withNewSession
  }

  def sendPersonal = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args => 
        val message = args("newMessage").head
        val recipient = args("recipient").head
        if (models.MessagesInMemoryModel.sendPersonal(username, message, recipient)) {
          Redirect(routes.Messages1.messages).flashing("success" -> "Message sent.")
        } else {
          Redirect(routes.Messages1.messages).flashing("error" -> "Message not sent: Invalid recipient.")
        }
      }.getOrElse(Redirect(routes.Messages1.messages))
    }.getOrElse(Redirect(routes.Messages1.login))
  }

  def sendGeneral = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val postVals = request.body.asFormUrlEncoded
      postVals.map { args => 
        val message = args("newMessage").head
        models.MessagesInMemoryModel.sendGeneral(username, message)
        Redirect(routes.Messages1.messages).flashing("success" -> "Message sent.")
      }.getOrElse(Redirect(routes.Messages1.messages))
    }.getOrElse(Redirect(routes.Messages1.login))
  }


}
