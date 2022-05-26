package ba.etf.nrsprojekat.data.models

import java.util.*


class Korisnik {
    private var id: String
    private var email: String
    private var password:String
    private var isAdmin:Boolean
    private var poslovnica:String
    var updatedAt: Date

    constructor(ID: String, Email: String, Password: String, IsAdmin:Boolean, poslovnica:String,updatedAt: Date) {
        email = Email
        password = Password
        isAdmin = IsAdmin
        id = ID
        this.poslovnica = poslovnica
        this.updatedAt = updatedAt
    }

    fun getEmail():String { return email }
    fun getPassword():String { return password }
    fun getID():String {return id}
    fun setPassword(Password: String) { password = Password }
    fun setEmail(Email: String) { email = Email }
    fun isAdmin():Boolean { return isAdmin }
    fun poslovnica():String { return poslovnica }
    fun setAdmin(IsAdmin: Boolean) { isAdmin = IsAdmin }
}

