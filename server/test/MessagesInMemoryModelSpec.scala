import org.scalatestplus.play._
import models._

// There is no analogous file for the rest of the Task 6 testing per your email "Testing Issues" sent on 3/27/24

class MessagesInMemoryModel extends PlaySpec {
    "MessagesInMemoryModel" must {
        "do valid login for default users " in {
            MessagesInMemoryModel.validateUser("mlewis", "prof") mustBe (true)
            MessagesInMemoryModel.validateUser("web", "apps") mustBe (true)  
        }

        "reject login with wrong password" in {
            MessagesInMemoryModel.validateUser("mlewis", "student") mustBe (false)
            MessagesInMemoryModel.validateUser("web", "electronapps") mustBe (false)
        }

        "reject login with wrong username" in {
            MessagesInMemoryModel.validateUser("lseelig", "prof") mustBe (false)
            MessagesInMemoryModel.validateUser("java", "apps") mustBe (false)
        }
        
        "reject login with wrong username and password" in {
            MessagesInMemoryModel.validateUser("lseelig", "student") mustBe (false)
        }

        "get correct default personal messages" in {
            MessagesInMemoryModel.getPersonal("mlewis") mustBe (Nil)
            MessagesInMemoryModel.getPersonal("web") mustBe (List("mlewis: Test message from mlewis to web"))
        }

        "get correct default general messages" in {
            MessagesInMemoryModel.getGeneral() mustBe (List("web: Test message from web to all"))
        }

        "create new users with no personal messages" in {
            MessagesInMemoryModel.createUser("lseelig", "student") mustBe (true)
            MessagesInMemoryModel.getPersonal("lseelig") mustBe (Nil)
        }

        "create new user with existing name" in {
            MessagesInMemoryModel.createUser("mlewis", "student") mustBe (false)
        }

        "send new general messages from default users" in {
            MessagesInMemoryModel.sendGeneral("mlewis", "New general message")
            MessagesInMemoryModel.getGeneral() mustBe (List("web: Test message from web to all", "mlewis: New general message"))
            MessagesInMemoryModel.sendGeneral("web", "New general message")
            MessagesInMemoryModel.getGeneral() mustBe (List("web: Test message from web to all", "mlewis: New general message", "web: New general message"))
        }
        
        "send new general messages from new user" in {
            MessagesInMemoryModel.sendGeneral("lseelig", "New general message")
            MessagesInMemoryModel.getGeneral() mustBe (List("web: Test message from web to all", "mlewis: New general message", "web: New general message", "lseelig: New general message"))
        }
        
        "send new personal messages between default users" in {
            MessagesInMemoryModel.sendPersonal("mlewis", "New personal message", "web") mustBe (true)
            MessagesInMemoryModel.getPersonal("web") mustBe (List("mlewis: Test message from mlewis to web", "mlewis: New personal message"))
            MessagesInMemoryModel.sendPersonal("web", "New personal message", "mlewis") mustBe (true)
            MessagesInMemoryModel.getPersonal("mlewis") mustBe (List("web: New personal message"))
        }

        "send new personal messages from new user" in {
            MessagesInMemoryModel.sendPersonal("lseelig", "New personal message", "mlewis") mustBe (true)
            MessagesInMemoryModel.getPersonal("mlewis") mustBe (List("web: New personal message", "lseelig: New personal message"))
        }

        "send new personal messages to new user" in {
            MessagesInMemoryModel.sendPersonal("web", "New personal message", "lseelig") mustBe (true)
            MessagesInMemoryModel.getPersonal("lseelig") mustBe (List("web: New personal message"))
        }

        "reject personal messages sent from default users to non-existent users" in {
            MessagesInMemoryModel.sendPersonal("mlewis", "New personal message", "java") mustBe (false)
            MessagesInMemoryModel.sendPersonal("web", "New personal message", "java") mustBe (false)
        }

        "reject personal messages sent from new user to non-existent users" in {
            MessagesInMemoryModel.sendPersonal("lseelig", "New personal message", "java") mustBe (false)
        }
    }
}