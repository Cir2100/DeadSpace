    package com.example.deadspace.ui.start

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.deadspace.R
import com.example.deadspace.databinding.StartActivityBinding
import com.example.deadspace.ui.deadlines.main.DeadlinesActivity
import com.example.deadspace.ui.exams.ExamActivity
import com.example.deadspace.ui.schedule.main.ScheduleActivity
import java.util.*

class StartActivity : AppCompatActivity() {

    private lateinit var viewModel: StartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = StartActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Навигация"

        val date = Calendar.getInstance()
        binding.currentDateDay.text = date.get(Calendar.DATE).toString()
        binding.currentDateMonth.text = date.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale.getDefault())
        binding.currentWeekday.text = date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG_FORMAT, Locale.getDefault())

        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)

        // Show a snackbar whenever the [ViewModel.snackbar] is updated a non-null value
        viewModel.toast.observe(this) { text ->
            text?.let {
                Toast.makeText(this@StartActivity, text,
                    Toast.LENGTH_LONG).show()
                viewModel.onToastShown()
            }
        }

        viewModel.weekType.observe(this) { weekType ->
            weekType?.let {
                binding.weekTypeTextview.text = if (weekType) "верхняя" else "нижняя"
                binding.weekColor.background = if (weekType) ResourcesCompat.getDrawable(resources, R.color.suai_red, theme)
                else ResourcesCompat.getDrawable(resources, R.color.suai_dark_blue, theme)
            }
        }

        viewModel.currentPair.observe(this) { currentPair ->
            currentPair?.let {
                binding.currentPairLess.text = it.Less.toString()
                binding.currentPairTime.text = resources.getString(R.string.pair_time_counter,
                    it.StartTime,  it.EndTime)
                binding.currentPairName.text = it.Disc
                binding.currentPairBuild.text = it.Build
                binding.currentPairAuditorium.text = it.Rooms
            }
            if (currentPair == null) {
                binding.currentPairLess.text = ""
                binding.currentPairTextView.text = ""
                binding.currentPairName.text = ""
                binding.currentPairBuild.text = ""
                binding.currentPairAuditorium.text = ""
                binding.currentPairTime.text = "чилл"
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCurrentPair()
    }

    fun onClickListDeadlines(view: View) {
        val deadlinesActivityIntent = Intent(this, DeadlinesActivity::class.java)
        startActivity(deadlinesActivityIntent)
    }

    fun onClickTest(view: View) {
        val testIntent = Intent(this, ExamActivity::class.java)
        startActivity(testIntent)
    }

    fun onClickPairs(view: View) {
        val pairsIntent = Intent(this, ScheduleActivity::class.java)
        startActivity(pairsIntent)
    }
}