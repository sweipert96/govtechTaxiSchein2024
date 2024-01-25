package govtechTaxiSchein2024

import de.seitenbau.serviceportal.scripting.api.v1.ScriptingApiV1

ScriptingApiV1 api = apiV1 // Variable is automatically set by Serviceportal process engine

// Allow users to stop/delete the currently running process instance
// See https://doku.pmp.seitenbau.com/pages/viewpage.action?pageId=394836
api.setVariable("killable", true)

// Out of convenience let's read process parameters here

final List<String> expectedParameters = [
        "costInCent",
        "paymentProvider"
]
checkProcessParameters(expectedParameters, api)

String provider = api.getStartParameter().parameters.get("paymentProvider")
List<String> paymentParameters

switch (provider) {
  case "EpayBl":
    paymentParameters = [
            "paymentInstanz",
            "paymentKeystore",
            "paymentKeystoreType",
            "paymentKeystorePassword",
            "paymentMandant",
            "paymentBewirtschafter",
            "paymentHaushaltsstelle",
            "paymentObjektnummer",
            "paymentKennzeichenMahnverfahren"
    ]
    checkProcessParameters(paymentParameters, api)
    break
  case "BerlinPayment":
    paymentParameters = [
            "paymentUsername",
            "paymentPassword",
            "paymentAuthId",
            "paymentAuthPassword"
    ]
    checkProcessParameters(paymentParameters, api)
    break
  default:
    throw new IllegalArgumentException("Failed to recognize payment provider. The provider $provider is not configured yet.")
}


static void checkProcessParameters(List<String> expectedParameters, ScriptingApiV1 api){
  expectedParameters.each { parameterName ->
    def parameterValue = api.getStartParameter().parameters.get(parameterName)
    if (parameterValue == null) {
      throw new IllegalStateException("Failed to initialize process. Mandatory parameter '$parameterName' is not " +
              "available. Please add this parameter to the Jesaja system.")
    }
  }
}
