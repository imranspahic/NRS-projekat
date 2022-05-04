package ba.etf.nrsprojekat.data.models

class Korisnik {
    private var id: String
    private var email: String
    private var password:String
    private var isAdmin:Boolean

    constructor(ID: String, Email: String, Password: String, IsAdmin:Boolean) {
        email = Email
        password = Password
        isAdmin = IsAdmin
        id = ID
    }

    fun getEmail():String { return email }
    fun getPassword():String { return password }
    fun getID():String {return id}
    fun setPassword(Password: String) { password = Password }
    fun setEmail(Email: String) { email = Email }
    fun isAdmin():Boolean { return isAdmin }
    fun setAdmin(IsAdmin: Boolean) { isAdmin = IsAdmin }
}

