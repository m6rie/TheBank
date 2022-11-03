package TheBankApp

import Database.AccountsMethods.{addingMoneyDB, balanceDB, createUserAccountDB, deleteDBAccount, userCheckingPersonalInfo, withdrawnBalanceDB}
import Database.LoginMethods

import scala.io.StdIn.{readChar, readDouble, readInt, readLine}
import io.AnsiColor._
import scala.util.control.Breaks.break
import scala.util.Random

abstract class Account {
  val firstName: String
  val lastName: String
  val DOB: String
  val client_username: String
  val client_password: String
  var balance: Double
  val interestRate: Double
  val creditCard: String
  val the_bank_id: Int = 1
}

object AccountMethods {
  def createNewAccount(): Unit = {
    val dateRegex = """([0-9]{4}-[0-9]{2}-[0-9]{2})"""
    val DateOnly = dateRegex.r

    def getDate(s: String) = s match {
      case DateOnly(d) => d
    }

    def checkBirthdateFormat(): String = {
      var birth = readLine()
      while (!birth.matches(dateRegex)) {
        try {
          getDate(birth)
        } catch {
          case e: MatchError => println("Wrong format. Please try again with the format YYYY-MM-DD:")
            birth = readLine()
        }
      }
      birth
    }

    println("------------------------------------\n")
    var newAccount = new Account {
      println("Please enter your first name: ")
      override val firstName: String = readLine()
      println("Please enter your last name: ")
      override val lastName: String = readLine()
      println("Please enter your date of birth with the format YYYY-MM-DD: ")
      override val DOB: String = checkBirthdateFormat()
      println("Please enter your username: ")
      override val client_username: String = readLine()
      println("Please enter your password: ")
      override val client_password: String = readLine()
      println("How much money are you transferring to your account? ")
      var balance: Double = readDouble()
      override val creditCard: String = Random.between(1000000000000000L, 9999999999999999L).toString
      override val interestRate: Double = interestRateCal(balance: Double)

      createUserAccountDB(firstName: String, lastName: String, DOB: String, client_username: String, client_password: String, balance: Double, creditCard: String, interestRate: Double)

      println(s"We are very happy to count you among us $firstName!\n")
      println("\n------------------------------------\n")

      accountMenuDB(firstName)
    }
  }


  def interestRateCal(balance: Double): Double = {
    if (balance < 10000) {
      0
    } else if (balance > 10000 & balance < 50000) {
      0.5
    } else if (balance > 50000 & balance < 100000) {
      1
    } else if (balance > 100000 & balance < 500000) {
      1.5
    } else if (balance > 500000 & balance < 1000000) {
      2
    } else {
      2.5
    }
  }

  def userLoginDB():Unit = {
    println(s"\n------------------------------------\n" +
      s"\nEnter your ${BOLD}username${RESET}: ")
    var usernameInput: String = readLine()
    println(s"Enter your ${BOLD}password${RESET}: ")
    var passwordInput: String = readLine()
    println("\n------------------------------------\n")
        while(LoginMethods.userLoginDB(usernameInput, passwordInput) == false) {
          println(s"\n------------------------------------\n" +
            s"Wrong credentials. Try again.\n" +
            s"\nEnter your ${BOLD}username${RESET}: ")
          usernameInput = readLine()
          println(s"Enter your ${BOLD}password${RESET}: ")
          passwordInput = readLine()
          println("\n------------------------------------\n")
        }

    /*
    accountMenuDB(usernameInput)
    var connection: Connection = null
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"SELECT * FROM accounts")
      while (resultSet.next()) {
        val usernameDB = resultSet.getString("client_username")
        val passwordDB = resultSet.getString("client_password")
        val first_name = resultSet.getString("client_first_name")

        if (usernameInput == usernameDB && passwordInput == passwordDB) {

          val currentUser: String = first_name
          accountMenuDB(first_name)
          return true
        }
      }
    }
    false
     */
  }

  def accountMenuDB(user: String): Unit = {
    println(s"\n------------------------------------\n" +
      s"Hi ${user}. What would you like to do today? Select your choice and press enter: \n" +
      "\bTo check your balance, press 1\n " +
      "\bTo add money to your account, press 2\n " +
      "\bTo withdraw money from your account, press 3\n " +
      "\bTo check your personal information, press 4\n" +
//      "\bTo change your password, press 5\n" +
      "\bTo delete your account, press 6\n " +
      "\bTo log out, press 7")
    var userMenuChoice = readInt()
    println("\n------------------------------------\n")

    userMenuChoice match {
      case 1 => checkBalanceDB(user: String)
      case 2 => addMoneyDB(user: String, amountToAddToAccountDB(user: String))
      case 3 => withdrawMoneyDB(user: String)
      case 4 => checkDetailsDB(user: String)
//      case 5 => updatePasswordDB(user: String)
      case 6 => deleteAccountDB(user: String)
      case 7 => loggingOutDB(user: String)
      case _ => println("Invalid choice. Try again: ")
    }
  }

  // METHOD TO CHECK BALANCE
  def checkBalanceDB(user: String): Unit = {
    println("\n------------------------------------\n")
    balanceDB(user:String)
    println("\n------------------------------------\n")
    accountMenuDB(user: String)
  }

  def checkDetailsDB(user: String):Unit = {
    // for a customer to check their personal information
    println("\n------------------------------------\n")
    userCheckingPersonalInfo(user: String)
    println("\n------------------------------------\n")
    accountMenuDB(user: String)
  }

  def updatePasswordDB(user: String):Unit = {
    // for a customer to change their password
  }

  // WE CAN USE A HIGHER ORDER FUNCTION TO ADD MONEY TO AN ACCOUNT
  def amountToAddToAccountDB(user: String): Double = {
    println("\n------------------------------------\n")

    balanceDB(user: String)
    println("How much money would you like to add?\n")
    var amountAdded: Double = readDouble()
    amountAdded
  }

  def addMoneyDB(user: String, amount: Double): Unit = {
    addingMoneyDB(user:String, amount: Double)
    println("\n------------------------------------\n")
    accountMenuDB(user: String)
  }

  // THE METHOD TO WITHDRAW MONEY
  def withdrawMoneyDB(user: String): Unit = {
    println("\n------------------------------------\n")
    withdrawnBalanceDB(user:String)
    println("\n------------------------------------\n")
  }

  def deleteAccountDB(user: String): Unit = {
    var deletingAccount: Boolean = false
    println(s"\n------------------------------------\n" +
      s"${RED}Are you sure you want to delete your account?${RESET} Press ${RED}\"Y\"${RESET} to confirm or ${GREEN}\"N\"${RESET} to go back to the menu: ")
    while (!deletingAccount) {
      var accountDeletion: Char = readChar().toLower
      if (accountDeletion != 'n' && accountDeletion != 'y') {
        println("Invalid choice. Try again: ")
      } else if (accountDeletion == 'n') {
        accountMenuDB(user: String)
      } else {
        println(s"Sorry to see you go ${user}!\n" +
        "-----------------------------------------")
        deleteDBAccount(user:String)
        deletingAccount = true
        break()
      }
    }
  }

  def loggingOutDB(user: String): Unit = {
    var logOut: Boolean = false
    println(s"\n------------------------------------\n" +
      s"${RED}Are you sure you want to log out?${RESET} Press ${RED}\"Y\"${RESET} to confirm or ${GREEN}\"N\"${RESET} to go back to the menu: ")
    while (!logOut) {
      var logOutVerifying: Char = readChar().toLower
      if (logOutVerifying != 'n' && logOutVerifying != 'y') {
        println("Invalid choice. Try again: ")
      } else if (logOutVerifying == 'n') {
        println("\n------------------------------------\n")
        accountMenuDB(user: String)
      } else {
        println(s"Thank you for your visit ${user}!\n")
        logOut = true
        break()
      }
    }
  }

}