package co.si.landing.networking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
//@HiltViewModel
class MainViewModel : ViewModel() {
    private var _panValidationStatus = false
    private var _dateValidationStatus = false

    private val _panLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val panLiveData: LiveData<Boolean> = _panLiveData
    fun panValidation(panCardNo: String?) {
        viewModelScope.launch(Dispatchers.IO) {
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
    }

    fun dateValidation(day: String?, month: String?, year: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val formattedDate = "$day-$month-$year"
//            dateValidationDeep(formattedDate)
            val sdf: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            sdf.isLenient = false
            _dateValidationStatus = try {
                sdf.parse(formattedDate)
                true
            } catch (e: ParseException) {
                e.printStackTrace()
                false
            }
            validatePanAndDateVal()
        }
    }

    private val _validatePanAndDateValLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val validatePanAndDateValLiveData: LiveData<Boolean> = _validatePanAndDateValLiveData
    private fun validatePanAndDateVal() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_panValidationStatus && _dateValidationStatus) {
                Timber.i("pan & date both are valid: true")
                _validatePanAndDateValLiveData.postValue(true)
            } else {
                Timber.i("pan & date both are valid: false")
                _validatePanAndDateValLiveData.postValue(false)
            }
        }
    }
}