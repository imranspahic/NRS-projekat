package ba.etf.nrsprojekat.data.models

class Korisnik {
    private var email: String
    private var password:String
    private var isAdmin:Boolean

    constructor(Email: String, Password: String, IsAdmin:Boolean) {
        email = Email
        password = Password
        isAdmin = IsAdmin
    }

    fun getEmail():String { return email }
    fun getPassword():String { return password }
    fun setPassword(Password: String) { password = Password }
    fun setEmail(Email: String) { email = Email }
    fun isAdmin():Boolean { return isAdmin }
    fun setAdmin(IsAdmin: Boolean) { isAdmin = IsAdmin }
}


