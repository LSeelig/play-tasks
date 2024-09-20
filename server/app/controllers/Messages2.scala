package controllers

import javax.inject._

import shared.SharedMessages
import play.api.mvc._
import play.api.i18n._
import models.{MessagesInMemoryModel => MsgModel}

@Singleton
class Messages2 @Inject()(cc: ControllerComponents) extends AbstractController(cc) {


    def load = Action { implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map { username =>
            Ok(views.html.version2Main(routes.Messages2.messages.toString))
        }.getOrElse(Ok(views.html.version2Main(routes.Messages2.login.toString)))
    }

    def login = Action {
        Ok(views.html.login2())
    }
    
    def messages = Action { implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map { username =>
            Ok(views.html.messages2(username, MsgModel.getPersonal(username), MsgModel.getGeneral()))
        }.getOrElse(Ok(views.html.login2()))
    }

    def validate() = Action { implicit request =>
        val postVals = request.body.asFormUrlEncoded
        postVals.map { args => 
            val username = args("username").head
            val password = args("password").head
            if (MsgModel.validateUser(username, password)) {
                Ok(views.html.messages2(username, 
                    MsgModel.getPersonal(username), 
                    MsgModel.getGeneral()))
                .withSession("username" -> username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
            } else { Ok(views.html.login2()) }
        }.getOrElse(Ok(views.html.login2()))
    }

    def create() = Action { implicit request =>
        val postVals = request.body.asFormUrlEncoded
        postVals.map { args => 
            val username = args("username").head
            val password = args("password").head
            if (MsgModel.createUser(username, password)) {
                Ok(views.html.messages2(username, 
                    MsgModel.getPersonal(username), 
                    MsgModel.getGeneral())
                ).withSession("username" -> username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
            } else { Ok(views.html.login2()) }
        }.getOrElse(Ok(views.html.login2()))
    }

    def sendPersonal = Action { implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map { username => 
            val postVals = request.body.asFormUrlEncoded
            postVals.map { args => 
                val message = args("message").head
                val recipient = args("recipient").head
                if (MsgModel.sendPersonal(username, message, recipient)) {
                    Ok(views.html.messages2(username, 
                        MsgModel.getPersonal(username), 
                        MsgModel.getGeneral())
                    )
                } else {
                    Ok(views.html.messages2(username, 
                        MsgModel.getPersonal(username), 
                        MsgModel.getGeneral())
                    )
                }
            }.getOrElse(Ok(views.html.messages2(username, 
                MsgModel.getPersonal(username), 
                MsgModel.getGeneral())
            ))
        }.getOrElse(Ok(views.html.login2()))
    }
  
    def sendGeneral = Action { implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map { username => 
            val postVals = request.body.asFormUrlEncoded
            postVals.map { args => 
                val message = args("message").head
                MsgModel.sendGeneral(username, message)
                Ok(views.html.messages2(username, 
                    MsgModel.getPersonal(username), 
                    MsgModel.getGeneral())
                )
            }.getOrElse(Ok(views.html.messages2(username, 
                MsgModel.getPersonal(username), 
                MsgModel.getGeneral())
            ))
        }.getOrElse(Ok(views.html.login2()))
    }
    
    def logout = Action {
        Redirect(routes.Messages2.load).withNewSession
    }
}
