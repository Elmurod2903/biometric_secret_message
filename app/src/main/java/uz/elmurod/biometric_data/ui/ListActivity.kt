
package uz.elmurod.biometric_data.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.content_list.*
import uz.elmurod.biometric_data.R
import uz.elmurod.biometric_data.common.EncryptedMessage
import uz.elmurod.biometric_data.util.PreferenceUtil

class ListActivity : AppCompatActivity(), MessageListAdapter.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(findViewById(R.id.toolbar))

        fab.setOnClickListener { view ->

            ColorStateList.valueOf(Color.WHITE)
            startActivity(Intent(this, EncryptionActivity::class.java))
        }
        val colorStateList = ColorStateList.valueOf(Color.WHITE)
        fab.imageTintList = colorStateList
    }

    override fun onResume() {
        super.onResume()

        // Set Adapter
        showMessageList()
    }

    override fun onItemClick(item: EncryptedMessage, itemView: View) {
        val decryptionIntent = (Intent(this, DecryptionActivity::class.java))
        decryptionIntent.putExtra(getString(R.string.parcel_message), item)
        startActivity(decryptionIntent)
    }

    private fun showMessageList() {
        val messageList = PreferenceUtil.getMessageList(applicationContext)
        if (!messageList.isNullOrEmpty()) {
            textViewNoMessage.visibility = View.GONE
            recyclerView.adapter = MessageListAdapter(messageList, this)
        }
    }

}
