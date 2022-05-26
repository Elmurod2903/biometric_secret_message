package uz.elmurod.biometric_data.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import uz.elmurod.biometric_data.R
import uz.elmurod.biometric_data.common.BiometricAuthListener
import uz.elmurod.biometric_data.databinding.ActivityMainBinding
import uz.elmurod.biometric_data.util.BiometricUtil
import java.util.concurrent.Executor

@RequiresApi(Build.VERSION_CODES.R)
class MainActivity : AppCompatActivity(), BiometricAuthListener {
    private lateinit var binding: ActivityMainBinding



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showTitleDevice()

        binding.biometricBtn.setOnClickListener {
            BiometricUtil.showBiometricPrompt(
                activity = this,
                listener = this,
                cryptoObject = null,
                allowDeviceCredential = true
            )
        }

    }

    override fun onBiometricAuthenticationSuccess(result: BiometricPrompt.AuthenticationResult) {
        Toast.makeText(
            this,
            "Tekshirish muvaffaqiyatli amalga oshirildi!", Toast.LENGTH_SHORT
        )
            .show()
        navigateToListActivity()

    }

    override fun onBiometricAuthenticationError(errorCode: Int, errorMessage: String) {
        Toast.makeText(
            this,
            "Tekshirish xatosi: $errorMessage", Toast.LENGTH_SHORT
        )
            .show()
    }

    fun navigateToListActivity() {
        startActivity(Intent(this, ListActivity::class.java))
        finish()
    }

    private fun showTitleDevice() {
        val biometricManager = BiometricManager.from(this)

        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                binding.tvBiometricInfo.text = "Ilova biometrik ma'lumotlardan foydalanishi mumkin."
                binding.biometricBtn.visibility = View.VISIBLE
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                binding.tvBiometricInfo.text = "Ushbu qurilmada biometrik funksiyalar mavjud emas."
                binding.biometricBtn.visibility = View.GONE
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                binding.tvBiometricInfo.text = "Biometri" +
                        "k xususiyatlar hozirda mavjud emas."
                binding.biometricBtn.visibility = View.GONE

            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                binding.tvBiometricInfo.text = "Biometrik xususiyatlar hozirda mavjud emas."
                binding.biometricBtn.visibility = View.GONE
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                startActivityForResult(enrollIntent, 1011)
            }

        }

    }


}