package ba.etf.nrsprojekat.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import ba.etf.nrsprojekat.R

class LogsSpinnerFilterAdapter(
    context: Context,
    spinnerFilters: List<String>
) :
    ArrayAdapter<String?>(context, 0, spinnerFilters) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(
        position: Int, convertView: View?,
        parent: ViewGroup
    ): View {
        // It is used to set our custom view.
        var view = convertView ?: LayoutInflater.from(context).inflate(R.layout.logs_spinner_filter_item, parent, false)
        val filterImageView = view.findViewById<ImageView>(R.id.filterImage)
        val filterTextView = view.findViewById<TextView>(R.id.filterName)
        val currentItem: String? = getItem(position)

        if (currentItem != null) {
            filterTextView.text = currentItem
            when(currentItem) {
                "LOGIN" -> {
                    filterImageView.setImageResource(R.drawable.circle_blue)
                }
                "LOGOUT" -> {
                    filterImageView.setImageResource(R.drawable.circle_light_blue)
                }
                "CREATE" -> {
                    filterImageView.setImageResource(R.drawable.circle_green)
                }
                "UPDATE" -> {
                    filterImageView.setImageResource(R.drawable.circle_orange)

                }
                "DELETE" -> {
                    filterImageView.setImageResource(R.drawable.circle_red)
                }
                else -> {
                    filterImageView.setImageResource(R.drawable.circle_grey)
                }
            }
        }
        return view
    }
}
