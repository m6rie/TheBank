package TheBankApp

import AccountMethods._
import Database.AccountsMethods.createUserAccountDB

import scala.io.StdIn.{readChar, readDouble, readInt, readLine}
import io.AnsiColor._
import scala.util.Random
import scala.util.control.Breaks.break
import Database.AdminsMethods.{adminDeleteDBAccount, allCustomersDB, selectCustomerByIdDB, selectCustomerByLastnameDB}
import Database.LoginMethods.adminLoginDB


class TheBankApp(name:String) {
  // This function is the homepage, what everybody will see at the beginning when entering the "website"
  def homePage(): Unit = {
    println(s"\n------------------------------------\n" +
      s"\nHello, Welcome to ${YELLOW}The Bank${RESET}. What would you like to do today?\n")
    println("Select your choice and press enter: \n" +
      "To log in, press 1\n" +
      "To create an account, press 2\n" +
      "For administrator access, press 3")

    /*
    "Press 1 to log in\n" +
    "Press 2 to create an account\n" +
    "Press 3 for administrator access")
     */
    var mainMenuChoice = readInt() // to take user's input
    while (mainMenuChoice != 1 || mainMenuChoice != 2 || mainMenuChoice != 3) {
      if (mainMenuChoice == 1) { // redirect to customer login page
        println("\n------------------------------------\n")
        userLoginDB()
      } else if (mainMenuChoice == 2) { // redirect to the account creation function
        println("\n------------------------------------\n")
        createNewAccount()
      } else if (mainMenuChoice == 3) { // redirect to the administrator login page
        println("\n------------------------------------\n")
        adminLoggingInDB()
      } else {
        println("Invalid choice. Try again: ")
        mainMenuChoice = readInt()
      }
    }
  }
  homePage()

  // The administrator logs-in thanks to this function
  def adminLoggingInDB() {
    var existingAdminUsername = false
    var correctAdminPassword = false

    while (!existingAdminUsername && !correctAdminPassword) {
      println("\n------------------------------------\n")
      println(s"${BOLD}Admin log in${RESET}\nEnter your ${BOLD}username${RESET}: ")
      var registeredAdmin: String = readLine()
      println(s"Enter your ${BOLD}password${RESET}: ")
      var registeredAdminPassword: String = readLine()
      if (adminLoginDB(registeredAdmin: String, registeredAdminPassword: String) == true) {
        var existingAdminUsername = true
        var correctAdminPassword = true
        println("\n------------------------------------\n")
        adminMenu()
      }
    }
  }

  def adminMenu(): Unit = {
    println("\n------------------------------------\n")
    println(s"Hi. What would you like to do today? Select your choice and press enter: \n" +
      "\bTo see all clients details, press 1\n " +
      "\bTo search clients using their account number, press 2\n " +
      "\bTo search clients using their last name, press 3\n " +
      "\bTo create a user account, press 4\n " +
      "\bTo delete a user account, press 5\n " +
      "\bTo log out, press 6")
    var adminMenuChoice = readInt()
    println("\n------------------------------------\n")

    adminMenuChoice match {
      case 1 => listAllCustomers()
      case 2 => searchClientById()
      case 3 => searchClientByLastName()
      case 4 => adminCreateNewAccount()
      case 5 => deleteUser()
      case 6 => adminLoggingOut()
      case _ => println("Invalid choice. Try again: ")
    }
  }

  // ALL ACCOUNT DISPLAY LIST
  def listAllCustomers(): Unit = {
    println("\n------------------------------------\n")
    allCustomersDB()
    println("\n------------------------------------\n")
    adminMenu()
  }

  def searchClientById(): Unit = {
    println("\n------------------------------------\n")
    println("Search a client using their account ID: ")
    var searchedID: Int = readInt()
    selectCustomerByIdDB(searchedID)
    println("\n------------------------------------\n")

    // ADD AN IF STATEMENT TO CHECK IF ACCOUNT ID EXISTS
//    if (checkingExistingID(searchedID: Int) == true) {
//      selectCustomerByIdDB(searchedID)
//    } else {
//      println("This account number does not exists.\n")
//    }
    adminMenu()
  }

  def searchClientByLastName(): Unit = {
    println("\n------------------------------------\n")
    println("Search a client using their last name: ")
    val searchedByLastName: String = readLine().toLowerCase.capitalize

    // ADD AN IF STATEMENT TO CHECK IF ACCOUNT name EXISTS
    selectCustomerByLastnameDB(searchedByLastName)
    println("\n------------------------------------\n")
    adminMenu()
  }

  def adminCreateNewAccount(): Unit = {
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

    println("\n------------------------------------\n")
    var newAccount = new Account {
      println("Please enter the client first name: ")
      override val firstName: String = readLine()
      println("Please enter the client last name: ")
      override val lastName: String = readLine()
      println("Please enter the client date of birth with the format YYYY/MM/DD: ")
      override val DOB: String = checkBirthdateFormat()
      println("Please enter the client username: ")
      override val client_username: String = readLine()
      println("Please enter the client password: ")
      override val client_password: String = readLine()
      println("How much money are they transferring to their account? ")
      var balance: Double = readDouble()
      override val creditCard: String = Random.between(1000000000000000L,9999999999999999L).toString
      override val interestRate: Double = interestRateCal(balance:Double)

      createUserAccountDB(firstName: String, lastName: String, DOB: String, client_username: String, client_password: String, balance: Double, creditCard: String, interestRate: Double)

      println(s"We are very happy to count $firstName among us!\n")
      println("\n------------------------------------\n")
    }
    adminMenu()
  }

  def deleteUser(): Unit = {
    println("\n------------------------------------\n")
    println("What is the account number you wish to delete?")
    var userAccountDeletion = readInt()
    adminDeleteDBAccount(userAccountDeletion:Int)
    println("\n------------------------------------\n")
    adminMenu()
  }

  def adminLoggingOut(): Unit = {
    var logOut: Boolean = false
    println("\n------------------------------------\n")
    println(s"${RED}Are you sure you want to log out?${RESET} Press ${RED}\"Y\"${RESET} to confirm or ${GREEN}\"N\"${RESET} to go back to the menu: ")
    while (!logOut) {
      var logOutVerifying: Char = readChar().toLower
      if (logOutVerifying != 'n' && logOutVerifying != 'y') {
        println("Invalid choice. Try again: ")
      } else if (logOutVerifying == 'n') {
        println("\n------------------------------------\n")
        adminMenu()
      } else {
        println(s"See you later!\n")
        println("\n------------------------------------\n")
        logOut = true
        break()
      }
    }
  }
}
object TheBankApp extends App {
  var bankingApp = new TheBankApp("The Bank")

  bankingApp.homePage()
}

