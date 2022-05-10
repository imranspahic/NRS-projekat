package ba.etf.nrsprojekat

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.data.models.LogItem
import ba.etf.nrsprojekat.services.LoggingService
import ba.etf.nrsprojekat.view.LogsListAdapter
import ba.etf.nrsprojekat.view.LogsSpinnerFilterAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton


class LoggingActivity : AppCompatActivity() {
    private lateinit var logsRecyclerView: RecyclerView
    private lateinit var logsEmptyView: TextView
    private lateinit var logsListAdapter: LogsListAdapter
    private lateinit var refreshLogsDugme: MaterialButton
    private lateinit var toolbar: MaterialToolbar
    private lateinit var logsSpinnerFilter: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logging)

        logsRecyclerView = findViewById(R.id.logsRecyclerView)
        refreshLogsDugme = findViewById(R.id.refreshLogsDugme)
        logsEmptyView = findViewById(R.id.logsEmptyView)
        toolbar = findViewById(R.id.logToolbar)
        logsSpinnerFilter = findViewById(R.id.logsFilterSpinner)

        val filters = listOf("SVE", "LOGIN", "LOGOUT", "CREATE", "UPDATE", "DELETE")
        val spinnerAdapter = LogsSpinnerFilterAdapter(this, filters)
        logsSpinnerFilter.adapter = spinnerAdapter

        logsListAdapter = LogsListAdapter(LoggingService.logItems, logsEmptyView)
        logsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        logsRecyclerView.adapter = logsListAdapter
        LoggingService.fetchLogs() { result ->
            if(result) {
                logsListAdapter.updateLogs(LoggingService.logItems)
            }
        }

        refreshLogsDugme.setOnClickListener {
            LoggingService.fetchLogs() { result ->
                if(result) {
                    filterLogs(logsSpinnerFilter.selectedItemPosition)
                }
            }
        }

        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }

        logsSpinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.i("spinnerPosition", position.toString())
                filterLogs(position)
            }
        }
    }

    private fun onToolbarBackButton() {
        finish()
    }

    private fun filterLogs(position: Int) {
        var logs: List<LogItem> = listOf()
        when(position) {
            0-> logs = LoggingService.logItems
            1-> logs = LoggingService.getLoginLogs()
            2-> logs = LoggingService.getLogoutLogs()
            3-> logs = LoggingService.getCreateLogs()
            4-> logs = LoggingService.getUpdateLogs()
            5-> logs = LoggingService.getDeleteLogs()
        }
        logsListAdapter.updateLogs(logs)
    }
}