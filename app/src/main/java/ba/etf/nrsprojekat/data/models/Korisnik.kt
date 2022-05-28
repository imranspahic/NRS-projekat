package ba.etf.nrsprojekat.data.models

import java.util.*


class Korisnik {
    private var id: String
    private var email: String
    private var password:String
    private var isAdmin:Boolean
    var poslovnica: String
    var createdAt: Date
    var updatedAt: Date

    constructor(id: String,
                email: String,
                password: String,
                isAdmin:Boolean,
                poslovnica:String,
                updatedAt: Date,
                createdAt: Date) {
        this.id = id
        this.email = email
        this.password = password
        this.isAdmin = isAdmin
        this.poslovnica = poslovnica
        this.poslovnica = poslovnica
        this.updatedAt = updatedAt
        this.createdAt = createdAt
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

