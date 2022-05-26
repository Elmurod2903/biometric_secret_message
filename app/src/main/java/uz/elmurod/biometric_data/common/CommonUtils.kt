
package uz.elmurod.biometric_data.common

import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper class with common utility functions
 */
object CommonUtils {

  /**
   * Displays a Toast message
   */
  fun displayToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
  }

  /**
   * Displays a Dialog message
   */
  fun displayMessage(context: Context,
                     title: String,
                     message: String,
                     onClick: DialogInterface.OnClickListener) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("Ok", onClick)
        .setNegativeButton("Cancel", null)
        .setCancelable(false)
        .show()
  }

  /**
   * Converts Time in Milliseconds to Date-String
   */
  fun longToDateString(timeInMillis: Long): String {
    val outputDateFormat = DateFormat.getDateTimeInstance()
    return outputDateFormat.format(Date(timeInMillis))
  }

}
