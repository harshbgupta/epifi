package co.si.landing.networking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Copyright © 2022 Hell Corporation. All rights reserved.
 *
 * @author Harsh Gupta aka Lucifer 😈
 * @since January 14, 2022
 */
class MainViewModel : ViewModel() {

    private var _panValidationStatus = false
    private var _dateValidationStatus = false

    private val _panLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val panLiveData: LiveData<Boolean> = _panLiveData
    fun panValidation(panCardNo: String?) {
        if (panCardNo == null) {
            _panValidationStatus = false
        }

        // Regex to check valid PAN Card number.
        val regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}"
        val p: Pattern = Pattern.compile(regex)
        val m: Matcher = p.matcher(panCardNo)
        _panValidationStatus = m.matches()
        _panLiveData.postValue(_panValidationStatus)
        Timber.i("pan is valid: $_panValidationStatus")
        validatePanAndDateVal()
    }

    private val _validatePanAndDateValLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val validatePanAndDateValLiveData: LiveData<Boolean> = _validatePanAndDateValLiveData
    private fun validatePanAndDateVal() {
        if (_panValidationStatus && _dateValidationStatus) {
            Timber.i("pan & date both are valid: true")
            _validatePanAndDateValLiveData.postValue(true)
        } else {
            Timber.i("pan & date both are valid: false")
            _validatePanAndDateValLiveData.postValue(false)
        }

    }

    fun dateValidation(day: String?, month: String?, year: String?) {
        val formattedDate = "$day-$month-$year"
        Timber.i("formattedDate -> $formattedDate")
        dateValidationDeep(formattedDate)
    }

    private fun dateValidationDeep(date: String) {
        val sdf: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        sdf.isLenient = false
        try {
            sdf.parse(date)
            dateStatusSetter(true)
        } catch (e: ParseException) {
            dateStatusSetter(false)
        }
    }

    private fun dateStatusSetter(status: Boolean) {
        _dateValidationStatus = status
        Timber.i("date is valid: $_dateValidationStatus")
        validatePanAndDateVal()
    }
}