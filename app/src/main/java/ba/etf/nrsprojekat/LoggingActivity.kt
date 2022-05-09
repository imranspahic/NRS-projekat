package ba.etf.nrsprojekat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.nrsprojekat.services.LoggingService
import ba.etf.nrsprojekat.view.LogsListAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class LoggingActivity : AppCompatActivity() {
    private lateinit var logsRecyclerView: RecyclerView
    private lateinit var logsListAdapter: LogsListAdapter
    private lateinit var refreshLogsDugme: MaterialButton
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logging)

        logsRecyclerView = findViewById(R.id.logsRecyclerView)
        refreshLogsDugme = findViewById(R.id.refreshLogsDugme)
        toolbar = findViewById(R.id.logToolbar)

        logsListAdapter = LogsListAdapter(LoggingService.logItems)
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
                    logsListAdapter.updateLogs(LoggingService.logItems)
                }
            }
        }

        toolbar.setNavigationOnClickListener {
            onToolbarBackButton()
        }
    }

    private fun onToolbarBackButton() {
        finish()
    }
}