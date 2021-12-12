[![](https://jitpack.io/v/verdimsdk/android-msdk.svg)](https://jitpack.io/#verdi/android-msdk)

# Verdi Mobile SDK

This SDK can be used to identify a user via passport or Id Card. 

<img src="/screenshots/step1_policy.jpg" width="20%" height="90%"> <img src="/screenshots/step2_document.jpg" width="20%"  height="90%"> <img src="/screenshots/step3_identification.jpg" width="20%"  height="90%"> 

In this repository, you can find the library itself and the Sample app which implements it.

* [Library](https://gitlab.com/islomov49/verdimsdk/-/tree/master/myverdisdk)
* [Sample app, which integrates Digid Mobile SDK](https://gitlab.com/islomov49/verdimsdk/-/tree/master/app)


### Implementation

build.gradle:

```groovy

dependencies {
	implementation ':${last.version}'
}
```

### Required Permissions
Internet permission should be provided in the `AndroidManifest` 

```xml
  <uses-permission android:name="android.permission.INTERNET" />
```

### Usage

In order to start using the SDK, `Verdi` class should be initialized with `Context` and `VerdiUserConfig` , this action preferably should be done in the Application class.

Required fields in the `VerdiUserConfig`

    - appId - Id which can be provided by DIGID company

Optional fields in the `VerdiUserConfig`

    - locale - The language of the SDK, by default it is Russian `ru` language

Example : 

```kotlin
    val config = VerdiUserConfig
            .Builder()
            .locale("uz") // uz, ru, en
            .appId("Your App Id")
            .build()
        Verdi.init(applicationContext, config)
```

`HttpLoggingInterceptor` can be enabled 
```kotlin
   Verdi.logs = BuildConfig.DEBUG

```

### Basically the SDK has 2 main features. **Identification** and **Authorization**. 

### Identification

1. The user should provide the Passport or Id Card info:

    Required fields

        - Document Number - (e.g AA1234567)
        - Date of Birth - (e.g 31.12.1990 with this format`dd.MM.yyyy`)
        - Date of Expiry - (e.g 31.12.2024 with this format`dd.MM.yyyy`)

    - `DocumentInputValidation` class can be used to validate the correctness of the document info, if user types them manually. 

    **Example**  
    ```kotlin
        val isEachFieldValid =
            DocumentInputValidation.isInputValid(DocumentInputType.PASSPORT(passportSeries)) &&
                DocumentInputValidation.isInputValid(DocumentInputType.BIRTHDAY(dateOfBirth)) &&
                    DocumentInputValidation.isInputValid(DocumentInputType.EXPIRATION(dateOfExpiry))
    ```

    - SDK Provides Passport and ID card scan tool, which will read the required info in the valid format. If the scan reads the document successfully, `Verdi.user` object holds the required scanned info. `Verdi.openDocumentScanActivity` is used to scan the document.     
    **Example** Document Scan
      ```kotlin
            /**
            * This method will scan Passport or Id Card
            * Inside the activity Camera permission is required
            * @param activity It will start ScanActivity
            * @param verdiListener It will be callback of the results
            * @param isQrCodeScan by default It will scan Passport. To change it to scan ID Card Pass true
            */
           Verdi.openDocumentScanActivity(requireActivity(), object : VerdiListener{
                override fun onSuccess() {
                    binding.etDocumentNumber.setText(Verdi.user.serialNumber)
                    binding.etDateOfBirth.setText(Verdi.user.birthDate)
                    binding.etDateOfExpiry.setText(Verdi.user.dateOfExpiry)
                }

                override fun onError(exception: Exception) {
                    //show Error
                }
            })
    ```

2. Scan Document with NFC (if NFC is not supported, then SDK skips this step)
3. Take a selfie: 

Above 2 steps taken sequentially by calling  `Verdi.proceedNfcAndSelfie`. The document info from the step 1 should be passed as paramaters for this method. 

`VerdiRegisterListener.onRegisterSuccess` method is called, if successfully passed all the steps. This method has `serialNumber` parameter passed, so it should be stored locally. When doing the Authorization part, it can be sent together.

**Example**
  ```kotlin
  Verdi.proceedNfcAndSelfie(
                    requireActivity(),
                    viewModel.passportSeries,
                    viewModel.dateOfBirth,
                    viewModel.dateOfExpiry,
                    object:VerdiRegisterListener{
                        override fun onRegisterSuccess(serialNumber: String) {
                            AppPreferences.scannerSerialNumber = serialNumber
                        }

                        override fun onRegisterError(exception: Exception) {
                        }
                    }
                )
```
<img src="/screenshots/nfc.jpg" width="20%">

### 2.Authorization
Authorization happens only after Identification process. The User should only take a selfie, and sent it with the `serialNumber` from the **Identification** process. 

**Example**

```kotlin
    Verdi.openSelfieActivity(
        requireActivity(), 
        object:VerdiListener {
            override fun onSuccess() {

            }

            override fun onError(exception: Exception) {
            
            }
        },
        AppPreferences.scannerSerialNumber
    )
```

In the end, `Verdi.finalResult` object holds all the required final results:

**Example**

```kotlin
data class FinalResult(
    var livenessScore: Double = 0.0, // if livenessScore > 0.79, then the user is identified
    var similarityScore: Double = 0.0, // if similarityScore > 0.46 then the user is identified
    var passportPhoto: Bitmap? = null, // passportPhoto, if NFC used during Identification Process
    var selfiePhoto: Bitmap? = null,
    var personPairList : List<Pair<String, String>>? = ArrayList(), // Key - data name, Value - User Info
    var addressPairList : List<Pair<String, String>>? = ArrayList() // Key - data name, Value - Address Info
)
```







