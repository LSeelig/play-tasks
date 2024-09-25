package models

import collection.mutable

object  MessagesInMemoryModel {
    private val users = mutable.Map[String, String]()
    private val messages = mutable.Map[String, List[String]]()
    private var general = List[String]()
    
    // Predefined Accounts and Messages
    createUser("web", "apps")
    createUser("mlewis", "prof")
    sendPersonal("mlewis", "Test message from mlewis to web", "web")
    sendGeneral("web", "Test message from web to all")

    def validateUser(username: String, password: String): Boolean = {
        users.get(username).map(_ == password).getOrElse(false)
    }

    def createUser(username: String, password: String): Boolean = {
        if (users.contains(username)) false else {
            users(username) = password
            true
        }
    }

    def getPersonal(username: String): Seq[String] = {
        messages.get(username).getOrElse(Nil)
    }

    def getGeneral(): Seq[String] = general

    def sendGeneral(username: String, message: String): Unit = {
        general = general ::: List(s"$username: $message")
    }
    
    def sendPersonal(username: String, message: String, recipient: String): Boolean = {
        if (users.contains(recipient)) {
            messages(recipient) = messages.get(recipient).getOrElse(Nil) ::: List(s"$username: $message")
            true
        } else false
    }
}