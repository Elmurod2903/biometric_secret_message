
package uz.elmurod.biometric_data.ui

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import kotlinx.android.synthetic.main.activity_encryption.*
import uz.elmurod.biometric_data.R
import uz.elmurod.biometric_data.common.BiometricAuthListener
import uz.elmurod.biometric_data.common.CommonUtils
import uz.elmurod.biometric_data.util.BiometricUtil
import uz.elmurod.biometric_data.util.CryptographyUtil
import uz.elmurod.biometric_data.util.PreferenceUtil
import javax.crypto.Cipher

class EncryptionActivity : AppCompatActivity(),
  BiometricAuthListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_encryption)
  }

  @RequiresApi(Build.VERSION_CODES.M)
  fun onClickEncryptMessage(view: View) {
    val message = textInputMessage.editText?.text.toString().trim()
    if (!TextUtils.isEmpty(message)) {
      if (BiometricUtil.isBiometricReady(this)) {
        showBiometricPromptToEncrypt()
      } else {
        showAlertToSetupBiometric()
      }
    }
  }

  private fun confirmInput() {
    textInputMessage.editText?.text = null
    CommonUtils.displayToast(this, getString(R.string.message_saved))
  }

  private fun showAlertToSetupBiometric() {
    CommonUtils.displayMessage(
        this,
        getString(R.string.message_encryption_failed),
        getString(R.string.message_no_biometric),
    ) { dialog: DialogInterface, index: Int ->
      BiometricUtil.lunchBiometricSettings(this)
    }
  }

  @RequiresApi(Build.VERSION_CODES.M)
  private fun showBiometricPromptToEncrypt() {
    // Create Cryptography Object
    val cryptoObject = BiometricPrompt.CryptoObject(
        CryptographyUtil.getInitializedCipherForEncryption()
    )
    // Show BiometricPrompt
    BiometricUtil.showBiometricPrompt(
        activity = this,
        listener = this,
        cryptoObject = cryptoObject
    )
  }

  override fun onBiometricAuthenticationSuccess(result: BiometricPrompt.AuthenticationResult) {
    result.cryptoObject?.cipher?.let {
      val message = textInputMessage.editText?.text.toString().trim()
      if (!TextUtils.isEmpty(message)) {
        encryptAndSave(message, it)
        confirmInput()
      }
    }
  }

  override fun onBiometricAuthenticationError(errorCode: Int, errorMessage: String) {
    CommonUtils.displayToast(this, "Biometric error: $errorMessage")
  }

  private fun encryptAndSave(plainTextMessage: String, cipher: Cipher) {
    val encryptedMessage = CryptographyUtil.encryptData(plainTextMessage, cipher)
    // Save Encrypted Message
    PreferenceUtil.storeEncryptedMessage(
        applicationContext,
        prefKey = encryptedMessage.savedAt.toString(),
        encryptedMessage = encryptedMessage
    )
  }

}
