
package uz.elmurod.biometric_data.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_list_item.view.*
import uz.elmurod.biometric_data.R
import uz.elmurod.biometric_data.common.CommonUtils
import uz.elmurod.biometric_data.common.EncryptedMessage

class MessageListAdapter(
  private val items: List<EncryptedMessage>,
  private val clickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  /**
   * Notifies click on an item with attached view
   */
  interface OnItemClickListener {
    fun onItemClick(item: EncryptedMessage, itemView: View)
  }

  /**
   * Creates view for each item in the list
   */
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.layout_list_item, parent, false)
    return ViewHolder(view)
  }

  /**
   * Binds view with item info
   */
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as ViewHolder).bind(position, items[position], clickListener)
  }

  /**
   * Returns the size to item list
   */
  override fun getItemCount(): Int {
    return items.size
  }

  /**
   * View for item, sets item info and click events
   */
  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(index: Int, message: EncryptedMessage, listener: OnItemClickListener) =
        with(itemView) {
          textViewTitle.text = "Secret Message ${(index + 1)}"
          textViewTime.text = "Saved on: ${CommonUtils.longToDateString(message.savedAt)}"

          // RecyclerView on item click
          setOnClickListener {
            listener.onItemClick(message, it)
          }
        }
  }

}
