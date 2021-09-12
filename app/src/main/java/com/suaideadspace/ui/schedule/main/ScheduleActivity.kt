package com.suaideadspace.ui.schedule.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.suaideadspace.R
import com.suaideadspace.databinding.ScheduleActivityBinding
import com.suaideadspace.ui.schedule.add.AddPairActivity
import java.text.SimpleDateFormat
import java.util.*
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.suaideadspace.data.database.PairData
import kotlinx.serialization.json.Json

class ScheduleActivity : AppCompatActivity() {

    private val deletePairClickListener = PairClickListener()

    private lateinit var prefs: SharedPreferences

    private lateinit var viewModel: ScheduleViewModel
    private lateinit var binding : ScheduleActivityBinding

    private lateinit var mAdapter: SimpleCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionbar = supportActionBar
        actionbar!!.title = "Расписание занятий"
        actionbar.setDisplayHomeAsUpEnabled(true)

        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        viewModel = ViewModelProvider(this).get(ScheduleViewModel::class.java)

        loadPreferences()

        binding = DataBindingUtil.setContentView(this, R.layout.schedule_activity)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Show a snackbar whenever the [ViewModel.snackbar] is updated a non-null value
        viewModel.toast.observe(this) { text ->
            text?.let {
                Toast.makeText(this@ScheduleActivity, text,
                    Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }

        //current group
        binding.nameGroupInput.setQuery(viewModel.currentGroup, true)

        //Current date
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd MMMM")
        val formattedDate = formatter.format(date)
        binding.currentDateTextview.text = formattedDate

        //List
        val adapter = ScheduleListAdapter(deletePairClickListener)
        binding.pairList.adapter = adapter
        binding.pairList.layoutManager = LinearLayoutManager(this)

        viewModel.myPairList.observe(this) { value ->
            value?.let {
                adapter.updateItems(value)
            }
        }
        mAdapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_1,
            null,
            arrayOf("Name"),
            intArrayOf(android.R.id.text1),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )

        mAdapter.filterQueryProvider =
            FilterQueryProvider { constraint -> updateCursor(constraint as String) }

        binding.nameGroupInput.suggestionsAdapter = mAdapter

        binding.nameGroupInput.setOnSuggestionListener(
            object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    return false
                }

                override fun onSuggestionClick(position: Int): Boolean {
                    val item = binding.nameGroupInput.suggestionsAdapter.getItem(position) as Cursor
                    val text = item.getString(item.getColumnIndex("Name"))
                    binding.nameGroupInput.setQuery(text, true)
                    return true
                }
            }
        )

        binding.nameGroupInput.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.onSearch(query)
                return false
            }

        })

        // show the spinner when [MainViewModel.spinner] is true
        viewModel.spinner.observe(this) { value ->
            value.let { show ->
                binding.loadSpinner.visibility = if (show) View.VISIBLE else View.GONE
            }
        }

        viewModel.pairsCount.observe(this) {
                value ->
            value.let { size ->
                binding.isNothingTextview.visibility = if (size == 0) View.VISIBLE else View.GONE
                binding.isNothingImageview.visibility = if (size == 0) View.VISIBLE else View.GONE
                binding.pairList.visibility = if (size != 0) View.VISIBLE else View.GONE
            }
        }

        viewModel.weekType.observe(this) { value ->
            value.let { type ->
                binding.typeOfWeek.text = if (type) resources.getString(R.string.upperWeek)
                else resources.getString(R.string.lowerWeek)
                binding.typeOfWeek.background = if (type) ResourcesCompat.getDrawable(resources, R.drawable.oval_button_red, theme)
                else ResourcesCompat.getDrawable(resources, R.drawable.oval_button_blue, theme)
            }
        }

        binding.isUsersSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onChangeIsUser(isChecked)
        }

        viewModel.isIgnoreLectures.observe(this) { value ->
            value.let { type ->
                binding.ignoreLecturesTextView.setTextColor( if (type) ResourcesCompat.getColor(resources, R.color.suai_red, theme)
                else ResourcesCompat.getColor(resources, R.color.black, theme))
            }
        }

        binding.ignoreLecturesTextView.setOnClickListener {
            viewModel.onChangeIsIgnoreLectures()
        }

        viewModel.isUsers.observe(this) { value ->
            value.let { value ->
                binding.isUsersSwitcher.isChecked = value
            }
        }

        binding.deleteUsersScheduleButton.setOnClickListener {
            createDeleteDialogFragment(true)
        }

    }

    override fun onPause() {
        super.onPause()

        // Запоминаем данные
        val editor = prefs.edit()
        editor.putString("APP_PREFERENCES_GROUP_SCHEDULE", viewModel.currentGroup)
        editor.putBoolean("APP_PREFERENCES_IS_USER", viewModel.isUsers.value!!)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        loadPreferences()
        viewModel.loadDaySchedule(500)
    }

    private fun loadPreferences() {
        if(prefs.contains("APP_PREFERENCES_GROUP_SCHEDULE")){
            viewModel.currentGroup = prefs.getString("APP_PREFERENCES_GROUP_SCHEDULE", "").toString()
        }
        if(prefs.contains("APP_PREFERENCES_IS_USER")){
            viewModel.setIsUsers(prefs.getBoolean("APP_PREFERENCES_IS_USER", false))
        }
    }

    fun onClickAddRasp(view: View)
    {
        if (viewModel.isScheduleLoaded()) {
            val addScheduleIntent = Intent(this, AddPairActivity::class.java)
            addScheduleIntent.putExtra("group", viewModel.currentGroup)
            addScheduleIntent.putExtra("weekDay", viewModel.weekDay)
            addScheduleIntent.putExtra("weekType", viewModel.getWeekType())
            startActivity(addScheduleIntent)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun updateCursor(text : String) : Cursor {
        val cursor = MatrixCursor(arrayOf(BaseColumns._ID, "Name"))
        for (i in viewModel.querySuggestions.indices) {
            if (viewModel.querySuggestions[i].Name.lowercase().startsWith(text.lowercase()))
                cursor.addRow(arrayOf<Any>(i, viewModel.querySuggestions[i].Name))
        }
        return cursor
    }

    fun createDeleteDialogFragment(type : Boolean) {
        val myDialogFragment = ScheduleDeleteDialogFragment()
        val manager = supportFragmentManager
        val args = Bundle()
        args.putBoolean("type", type)
        myDialogFragment.arguments = args
        myDialogFragment.show(manager, "myDialog")
    }

    private fun createPairInfoDialogFragment(
    item: PairData
    ) {
        val myDialogFragment = PairDialogFragment()
        val manager = supportFragmentManager
        val args = Bundle()
        args.putInt("less", item.Less)
        args.putString("time", resources.getString(R.string.pair_time_counter,
            item.StartTime,  item.EndTime))
        args.putString("type", item.Type)
        args.putString("disc", item.Disc)
        args.putString("auditorium", item.Rooms)
        args.putString("build", item.Build)
        args.putString("groups", item.GroupsText)
        args.putString("teachers", item.TeachersText)
        myDialogFragment.arguments = args
        myDialogFragment.show(manager, "myDialog")
    }

    fun okClickedDeleteUsersSchedule() {
        viewModel.deleteUserSchedule()
    }

    fun okClickedDeletePair() {
        viewModel.onDeletePair(deletePairClickListener.getItem())
    }

    inner class PairClickListener {

        private lateinit var item: PairData

        fun onClickDeletePair(item : PairData) {
            this.item = item
            createDeleteDialogFragment(false)
        }

        fun onClickPairItem(item : PairData) {
            val format = Json { coerceInputValues = true }
            createPairInfoDialogFragment(item)
        }

        fun getItem() : PairData = item
    }

}