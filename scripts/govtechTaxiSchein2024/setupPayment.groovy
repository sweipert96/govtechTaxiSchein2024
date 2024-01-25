package govtechTaxiSchein2024

import de.seitenbau.serviceportal.scripting.api.v1.ScriptingApiV1
import de.seitenbau.serviceportal.scripting.api.v1.payment.*
import groovy.json.JsonOutput

ScriptingApiV1 api = apiV1 // Variable is automatically set by Serviceportal process engine

// Note: You probably want to read most of these values from process parameters.
// Use `api.getStartParameter().parameters.get(parameterName)` to do so.

// Setup paymentConfig epaybl
// See https://doku.pmp.seitenbau.com/display/DFO/paymentConfig for more information

// provider
String provider = api.getStartParameter().parameters.get("paymentProvider")

switch (provider) {
  case "EpayBl":
    PaymentConfigV1 paymentConfig = new EpayBlPaymentConfigV1()
    paymentConfig.instanz = api.getStartParameter().parameters.get("paymentInstanz")
    paymentConfig.keystore = api.getStartParameter().parameters.get("paymentKeystore")
    paymentConfig.keystoreType = api.getStartParameter().parameters.get("paymentKeystoreType")
    paymentConfig.keystorePassword = api.getStartParameter().parameters.get("paymentKeystorePassword")
    paymentConfig.mandant = api.getStartParameter().parameters.get("paymentMandant")
    paymentConfig.bewirtschafter = api.getStartParameter().parameters.get("paymentBewirtschafter")
    paymentConfig.haushaltsstelle = api.getStartParameter().parameters.get("paymentHaushaltsstelle")
    paymentConfig.objektnummer = api.getStartParameter().parameters.get("paymentObjektnummer")
    paymentConfig.kennzeichenMahnverfahren = api.getStartParameter().parameters.get("paymentKennzeichenMahnverfahren")
    api.setVariable("paymentConfig", JsonOutput.prettyPrint(JsonOutput.toJson(paymentConfig)))
    break
  case "BerlinPayment":
    PaymentConfigV1 paymentConfig = new BerlinPaymentConfigV1()
    paymentConfig.username = api.getStartParameter().parameters.get("paymentUsername")
    paymentConfig.password = api.getStartParameter().parameters.get("paymentPassword")
    paymentConfig.authId = api.getStartParameter().parameters.get("paymentAuthId")
    paymentConfig.authPassword = api.getStartParameter().parameters.get("paymentAuthPassword")
    api.setVariable("paymentConfig", JsonOutput.prettyPrint(JsonOutput.toJson(paymentConfig)))
    break
  default:
    throw new IllegalArgumentException("Failed to recognize payment provider. The provider $provider is not configured yet.")
}

// Setup transactionConfig
// See https://doku.pmp.seitenbau.com/display/DFO/transactionConfig for expected values and other optional parameters
TransactionConfigV1 transactionConfig = new TransactionConfigV1()
transactionConfig.betrag = api.getStartParameter().parameters.get("costInCent") as Integer
transactionConfig.verwendungszweck = "Taxi-Schein"
api.setVariable("transactionConfig", JsonOutput.prettyPrint(JsonOutput.toJson(transactionConfig)))

// Setup displayConfig
DisplayConfigV1 displayConfig = new DisplayConfigV1()
displayConfig.skipSuccessPage = true // Success page is not necessary, as the process provides it's own "last page"
api.setVariable("displayConfig", JsonOutput.prettyPrint(JsonOutput.toJson(displayConfig)))
