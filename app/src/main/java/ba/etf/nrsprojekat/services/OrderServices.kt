package ba.etf.nrsprojekat.services

import android.content.ContentValues
import android.util.Log
import android.util.Xml
import ba.etf.nrsprojekat.data.models.Narudzba
import ba.etf.nrsprojekat.data.models.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamWriter
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

object OrderServices {
    private val db = Firebase.firestore
    var imeTrenutneNarudzbe: String? = null
    var mapaZaNarudzbu = mutableMapOf<String, Any>()
    var lokacija: String? = null
    var mjesto: String? = null

    fun getOrders(id: String, callback: (result: MutableList<Narudzba>) -> Unit) {
        var lista: MutableList<Narudzba> = mutableListOf()
        db.collection("orders")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(!document.data["isDeleted"].toString().toBoolean() && document.data["idKupca"] == id)
                        lista.add(
                            Narudzba(
                                document.data["id"].toString(),
                                document.data["nazivNarudzbe"].toString(),
                                document.data["statusNarudzbe"].toString(),
                                document.data["idKupca"].toString(),
                                document.data["listaProizvoda"] as List<MutableMap<String, Any>>,
                                (document.data["datumNarudzbe"] as com.google.firebase.Timestamp).toDate(),
                                document.data["lokacija"].toString(),
                                document.data["mjesto"].toString(),
                                (document.data["datumRacuna"] as? com.google.firebase.Timestamp)?.toDate(),
                                document.data["brojRacuna"]?.toString()?.toInt(),
                            )
                        )
                }
                callback(lista)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting orders.", exception)
            }
    }

    fun addOrder(nazivNarudzbe: String, status: String, idKupca: String, mapa: MutableMap<String, Any>) {
        val documentReference = db.collection("orders").document()

        val itemMapList = mapa.map { m ->
            val proizvod: Product = ProductsService.products.first { p -> p.id == m.key }
            hashMapOf(
                "productID" to m.key,
                "quantity" to m.value,
                "productName" to proizvod.name,
                "productPrice" to proizvod.price,
                "productPdvCategory" to proizvod.pdvCategoryName,
                "productUnit" to proizvod.mjernaJedinica,
                "productPoslovnica" to proizvod.poslovnicaName
            )
        }
        val order = hashMapOf(
            "id" to documentReference.id,
            "datumNarudzbe" to Date(),
            "nazivNarudzbe" to nazivNarudzbe,
            "statusNarudzbe" to status,
            "isDeleted" to false,
            "idKupca" to idKupca,
            "listaProizvoda" to itemMapList,
            "lokacija" to lokacija,
            "mjesto" to mjesto,
            "datumRacuna" to null,
            "brojRacuna" to null
        )
        documentReference.set(order)
    }
    fun updateOrder(id: String) { // staviti id
        val updateOrder = mapOf(
            "isDeleted" to true
        )
        db.collection("orders").document(id).update(updateOrder).addOnSuccessListener {
        }

    }

    fun getOrder(id: String?, callback: (narudzba: Narudzba, ukupnaCijena: Double) -> Unit,
                 failureCallback: () -> Unit
    )  {
        var finalPrice : Double = 0.0
        lateinit var narudzba: Narudzba
        db.collection("orders").document(id!!)
            .get()
            .addOnSuccessListener { document ->
                if(document == null) {
                    failureCallback()
                    return@addOnSuccessListener
                }
                    val mapa: MutableList<MutableMap<String, Any>> = document["listaProizvoda"] as MutableList<MutableMap<String, Any>>
                narudzba = Narudzba(
                                document["id"].toString(),
                                document["nazivNarudzbe"].toString(),
                                document["statusNarudzbe"].toString(),
                                document["idKupca"].toString(),
                                document["listaProizvoda"] as List<MutableMap<String, Any>>,
                                (document["datumNarudzbe"] as com.google.firebase.Timestamp).toDate(),
                                document["lokacija"].toString(),
                                document["mjesto"].toString(),
                                (document["datumRacuna"] as? com.google.firebase.Timestamp)?.toDate(),
                                document["brojRacuna"]?.toString()?.toInt(),
                                )

                        var brojac=0

                        while(brojac <= narudzba.proizvodi.size-1) {
                            finalPrice += narudzba.proizvodi[brojac].get("productPrice").toString().toDouble() * narudzba.proizvodi[brojac].get("quantity").toString().toDouble()
                            brojac++
                        }
                callback(narudzba, finalPrice)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting orders.", exception)
                failureCallback()
            }
    }

    fun setMapa() {
        for(item in ProductsService.products)
            if(item.quantity != 0)
                mapaZaNarudzbu.put(item.id, 0)
    }

    fun resetKolicinaProducts() {
        for(item in ProductsService.products)
            if(item.kolicinaNarudzbe != 0) item.kolicinaNarudzbe = 0
    }

    fun resetData() {
        imeTrenutneNarudzbe = null
        mapaZaNarudzbu = mutableMapOf()
        lokacija = null
        mjesto = null
    }

    fun fiskalizirajRacun(narudzba: Narudzba) {

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val sadrzajRacuna = generisiXmlNarudzbe(narudzba)
                val url = URL("http://192.168.100.9:8085/stampatifiskalniracun")
                val postData = """<?xml version="1.0" encoding="UTF-8"?>
<RacunZahtjev xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <VrstaZahtjeva>0</VrstaZahtjeva>
    <NoviObjekat>
        <StavkeRacuna>
            $sadrzajRacuna
        </StavkeRacuna>
        <VrstePlacanja>
            <VrstaPlacanja>
                <Oznaka>Gotovina</Oznaka>
                <Iznos>50</Iznos>
            </VrstaPlacanja>
        </VrstePlacanja>
    </NoviObjekat>
</RacunZahtjev>"""
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "text/xml")
                conn.useCaches = false
                var result = ""
                DataOutputStream(conn.outputStream).use { it.writeBytes(postData) }
                BufferedReader(InputStreamReader(conn.inputStream)).use { br ->
                    result = br.readText()
                    Log.d("orders", "result = ${result}")
                    val dbFactory = DocumentBuilderFactory.newInstance()
                    val dBuilder = dbFactory.newDocumentBuilder()
                    val xmlInput = result.byteInputStream(charset("utf-8"))
                    InputSource(StringReader(br.readText()))
                    val doc = dBuilder.parse(xmlInput)

                    val nVrstaOdgovoraList = doc.getElementsByTagName("VrstaOdgovora")

                    if(nVrstaOdgovoraList.length == 0) {
                        //ERROR
                        //RETURN
                    }
                    else {
                        val textContent = nVrstaOdgovoraList.item(0).textContent
                        Log.d("orders", "Vrsta odgovora = ${textContent}")
                        if(textContent == "GRESKA") {
                            //ERROR
                            //RETURN
                        }
                    }

                    val odgovoriLista = doc.getElementsByTagName("Odgovori")
                    Log.d("orders", "Odgovori element length = ${odgovoriLista.length}")
                    if(odgovoriLista.length == 0) {
                        //ERROR
                        //RETURN
                    }
                        val element = odgovoriLista.item(0) as Element
                        val odgovorLista = element.getElementsByTagName("Odgovor")
                        Log.d("orders", "Odgovor element lista = ${odgovorLista.length}")

                    for(i in 0..odgovorLista.length-1) {
                        val odgovorElement = odgovorLista.item(i) as Element

                        val nazivElement = odgovorElement.getElementsByTagName("Naziv").item(0) as Element
                        val vrijednostElement = odgovorElement.getElementsByTagName("Vrijednost").item(0) as Element

                        val naziv = nazivElement.textContent
                        val vrijednost = vrijednostElement.textContent

                        Log.d("orders", "Odgovor elemement i=${i}, naziv=$naziv, vrijednost=$vrijednost")
                    }

                }
                Log.d("orders: fiskalizirajRacun", "success result")
            } catch(e: Exception) {
                Log.d("orders: fiskalizirajRacun", "error = ${e.message}")
                Log.d("orders: fiskalizirajRacun", "error stacktrace = ${e.stackTrace.toString()}")
            }

        }
    }

    private fun generisiXmlNarudzbe(narudzba: Narudzba): String {
        var artikliString = ""

       narudzba.proizvodi.forEach {
           val artikalString = """
        <RacunStavka>
        <artikal>
        <Sifra>${it.get("productID")}</Sifra>
        <Naziv>${it.get("productName")}</Naziv>
        <JM>ko</JM>
        <Cijena>${it.get("productPrice")}</Cijena>
        <Stopa>E</Stopa>
        <Grupa>0</Grupa>
        <PLU>0</PLU>
        </artikal>
        <Kolicina>${it.get("quantity")}</Kolicina>
        <Rabat>0</Rabat>
        </RacunStavka>
        """
           artikliString = artikliString.plus(artikalString)
       }
        Log.d("orders", "Artikli string = $artikliString")
        return artikliString

    }
}