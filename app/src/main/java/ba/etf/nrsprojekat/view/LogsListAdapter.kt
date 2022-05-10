package ba.etf.nrsprojekat.view

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.R
import ba.etf.nrsprojekat.data.enums.LogAction
import ba.etf.nrsprojekat.data.models.LogItem
import java.text.SimpleDateFormat

class LogsListAdapter(
    private var logs: List<LogItem>,
    private var emptyView: TextView
) : RecyclerView.Adapter<LogsListAdapter.LogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.logs_list_item, parent, false)
        return LogViewHolder(view)
    }
    override fun getItemCount(): Int = logs.size

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log: LogItem = logs[position]
        holder.logTekst.text = log.tekst
        holder.logActionText.text = log.logAction.toString().uppercase()
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy  HH:mm")
        holder.logDateText.text = simpleDateFormat.format(log.createdAt)

        when (log.logAction) {
            LogAction.LOGIN -> {
                holder.logDivider.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.blue))
                holder.logActionText.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.blue)))
            }
            LogAction.LOGOUT -> {
                holder.logDivider.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.lightBlue))
                holder.logActionText.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.lightBlue)))
            }
            LogAction.CREATE -> {
                holder.logDivider.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.main_green))
                holder.logActionText.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.main_green)))
            }
            LogAction.UPDATE -> {
                holder.logDivider.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.orange))
                holder.logActionText.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.orange)))
            }
            LogAction.DELETE -> {
                holder.logDivider.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
                holder.logActionText.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.red)))
            }
        }

    }
    fun updateLogs(logs: List<LogItem>) {
        this.logs = logs.sortedWith(compareBy<LogItem> { it.createdAt }.reversed())
        if(this.logs.isEmpty()) emptyView.visibility = View.VISIBLE
        else emptyView.visibility = View.GONE
        notifyDataSetChanged()
    }

    inner class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var logDivider: View = itemView.findViewById(R.id.logDivider)
        var logTekst: TextView = itemView.findViewById(R.id.logText)
        var logActionText: TextView = itemView.findViewById(R.id.logActionText)
        var logDateText: TextView = itemView.findViewById(R.id.logDate)
    }
}