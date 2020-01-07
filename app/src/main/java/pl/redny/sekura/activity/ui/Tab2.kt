package pl.redny.sekura.activity.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.tab2.*
import org.koin.android.ext.android.inject
import pl.redny.sekura.R
import pl.redny.sekura.activity.ViewModel
import pl.redny.sekura.activity.view.filePicker.FilePicker
import pl.redny.sekura.remoteControl.feature.DeleteFile
import pl.redny.sekura.remoteControl.feature.DeleteSMS
import pl.redny.sekura.remoteControl.feature.SharePhoneLocation

class Tab2 : Fragment() {
    private val filePicker: FilePicker by inject()

    private val viewModel: ViewModel by inject()

    private var toDelete: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.tab2, container, false)
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences: SharedPreferences = context!!.getSharedPreferences(
            "Preferences",
            Context.MODE_PRIVATE
        )
        val phoneNumber = sharedPreferences.getString("phoneNumber", "")
        sms_control_number.text = SpannableStringBuilder(phoneNumber)

        val sentence1 = sharedPreferences.getString("sentence1", "")
        remote_location_sentence.text = SpannableStringBuilder(sentence1)

        val sentence2 = sharedPreferences.getString("sentence2", "")
        remote_sms_erasure_sentence.text = SpannableStringBuilder(sentence2)

        val sentence3 = sharedPreferences.getString("sentence3", "")
        remote_file_erasure_sentence.text = SpannableStringBuilder(sentence3)

        if (sharedPreferences.getBoolean("feature1", true)) {
            remote_location_gps.isChecked = true
        }

        if (sharedPreferences.getBoolean("feature2", true)) {
            remote_sms_erasure.isChecked = true
        }

        if (sharedPreferences.getBoolean("feature3", true)) {
            remote_file_erasure.isChecked = true
        }

        val editor = sharedPreferences.edit()
        remote_location_gps.setOnCheckedChangeListener { compoundButton, b -> feature1Change(editor) }
        remote_sms_erasure.setOnCheckedChangeListener { compoundButton, b -> feature2Change(editor) }
        remote_file_erasure.setOnCheckedChangeListener { compoundButton, b -> feature3Change(editor) }
        remote_button_add.setOnClickListener { addToSet(editor, sharedPreferences) }
        remote_button_delete.setOnClickListener { deleteElement(editor, sharedPreferences) }
        remote_path_file_picker.setOnClickListener { filePicker.openFilePicker(activity!!, 2139) }
        remote_location_sentence.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onFeature1SentenceChange(editor)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        remote_sms_erasure_sentence.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onFeature2SentenceChange(editor)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        remote_file_erasure_sentence.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onFeature3SentenceChange(editor)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        sms_control_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onPhoneNumberChange(editor)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val listViewAdapter = ArrayAdapter<String>(
            context!!,
            R.layout.phone_list_element,
            sharedPreferences.getStringSet("fileSet", setOf())!!.toMutableList()
        )
        remote_file_erasure_list_view.adapter = listViewAdapter

        remote_file_erasure_list_view.setOnItemClickListener { parent, view, position, id -> onListClick(position) }

    }

    private fun onListClick(position: Int) {
        toDelete = remote_file_erasure_list_view.adapter.getItem(position) as String
    }


    private fun feature1Change(editor: SharedPreferences.Editor) {
        editor.putBoolean("feature1", remote_location_gps.isChecked)
        editor.apply()
    }

    private fun feature2Change(editor: SharedPreferences.Editor) {
        editor.putBoolean("feature2", remote_sms_erasure.isChecked)
        editor.apply()
    }

    private fun feature3Change(editor: SharedPreferences.Editor) {
        editor.putBoolean("feature3", remote_file_erasure.isChecked)
        editor.apply()
    }

    private fun onPhoneNumberChange(editor: SharedPreferences.Editor) {
        editor.putString("phoneNumber", sms_control_number.text.toString())
        editor.apply()
    }

    private fun onFeature1SentenceChange(editor: SharedPreferences.Editor) {
        editor.putString("sentence1", remote_location_sentence.text.toString())
        editor.apply()
    }

    private fun onFeature2SentenceChange(editor: SharedPreferences.Editor) {
        editor.putString("sentence2", remote_sms_erasure_sentence.text.toString())
        editor.apply()
    }

    private fun onFeature3SentenceChange(editor: SharedPreferences.Editor) {
        editor.putString("sentence3", remote_file_erasure_sentence.text.toString())
        editor.apply()
    }

    private fun deleteElement(editor: SharedPreferences.Editor, sharedPreferences: SharedPreferences) {
        val listView = sharedPreferences.getStringSet("fileSet", setOf())!!.toMutableList()
        listView.remove(toDelete)
        editor.putStringSet("fileSet", listView.toSet())
        editor.apply()
        val listViewAdapter = ArrayAdapter<String>(
            context!!,
            R.layout.phone_list_element,
            sharedPreferences.getStringSet("fileSet", setOf())!!.toMutableList()
        )
        remote_file_erasure_list_view.adapter = listViewAdapter

        remote_file_erasure_list_view.invalidateViews()
    }

    private fun addToSet(editor: SharedPreferences.Editor, sharedPreferences: SharedPreferences) {
        val listView = sharedPreferences.getStringSet("fileSet", setOf())!!.toMutableList()
        listView.add(viewModel.filePath.toString())
        editor.putStringSet("fileSet", listView.toSet())
        editor.apply()
        val listViewAdapter = ArrayAdapter<String>(
            context!!,
            R.layout.phone_list_element,
            sharedPreferences.getStringSet("fileSet", setOf())!!.toMutableList()
        )
        remote_file_erasure_list_view.adapter = listViewAdapter

        remote_file_erasure_list_view.invalidateViews()
    }
}