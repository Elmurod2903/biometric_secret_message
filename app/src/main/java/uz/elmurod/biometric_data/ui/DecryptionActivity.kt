package uz.elmurod.biometric_data.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import uz.elmurod.biometric_data.common.BiometricAuthListener
import uz.elmurod.biometric_data.common.CommonUtils
import uz.elmurod.biometric_data.common.EncryptedMessage
import uz.elmurod.biometric_data.util.BiometricUtil
import uz.elmurod.biometric_data.util.CryptographyUtil
import kotlinx.android.synthetic.main.activity_decryption.*
import uz.elmurod.biometric_data.R
import javax.crypto.Cipher

class DecryptionActivity : AppCompatActivity(),
  BiometricAuthListener {

  var encryptedMessage: EncryptedMessage? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_decryption)

    encryptedMessage = intent.getParcelableExtra(getString(R.string.parcel_message))
  }

  @RequiresApi(Build.VERSION_CODES.M)
  fun onClickDecryptMessage(view: View) {
    showBiometricPromptToDecrypt()
  }

  @RequiresApi(Build.VERSION_CODES.M)
  private fun showBiometricPromptToDecrypt() {
    encryptedMessage?.initializationVector?.let { it ->
      // Retrieve Cryptography Object
      val cryptoObject = BiometricPrompt.CryptoObject(
          CryptographyUtil.getInitializedCipherForDecryption(it)
      )
      // Show BiometricPrompt With Cryptography Object
      BiometricUtil.showBiometricPrompt(
          activity = this,
          listener = this,
          cryptoObject = cryptoObject
      )
    }
  }

  private fun decryptAndDisplay(cipher: Cipher) {
    encryptedMessage?.cipherText?.let { it ->
      val decryptedMessage = CryptographyUtil.decryptData(it, cipher)
      textViewMessage.text = decryptedMessage
    }
  }

  override fun onBiometricAuthenticationSuccess(result: BiometricPrompt.AuthenticationResult) {
    result.cryptoObject?.cipher?.let {
      decryptAndDisplay(it)
    }
  }

  override fun onBiometricAuthenticationError(errorCode: Int, errorMessage: String) {
    CommonUtils.displayToast(this, "Biometric error: $errorMessage")
  }

}
