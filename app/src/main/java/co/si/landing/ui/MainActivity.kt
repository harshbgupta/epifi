package co.si.landing.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import co.si.R
import co.si.databinding.MainActivityBinding
import co.si.landing.networking.MainViewModel

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel
    private var mDay: String? = null
    private var mMonth: String? = null
    private var mYear: String? = null

    /**
     * Lifecycle Method
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListener()
        dataObserver()
        restOfCoding()
    }

    /**
     * Put all click listener
     */
    private fun setClickListener() {
        binding.button.setOnClickListener(this)
        binding.noPan.setOnClickListener(this)
    }

    /**
     * on click interface method
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.button -> {
                Toast.makeText(this, getString(R.string.suceess_submitted), Toast.LENGTH_LONG).show()
            }
            R.id.noPan -> {
                finish()
            }
        }
    }

    /**
     * Put all data observer here
     */
    private fun dataObserver() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //Pan Validation
        viewModel.panLiveData.observe(this) { status ->
            if (!status) {
                val pan = binding.pan.text.toString()
                if (pan.isNotEmpty() && pan.length == 10) {
                    binding.pan.error = getString(R.string.valid_pan_warning)
                }
            }
        }
        //Complete Validation
        viewModel.validatePanAndDateValLiveData.observe(this) { status ->
            if (status) {
                binding.button.isClickable = true
                binding.button.isEnabled = true
            } else {
                binding.button.isClickable = false
                binding.button.isEnabled = false
            }
        }
    }

    /**
     * Rest of Coding
     */
    private fun restOfCoding() {
        binding.apply {
            pan.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val mPan = pan.text.toString()
                    viewModel.panValidation(mPan)
                }
            })
            day.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    mDay = day.text.toString()
                    try {
                        if (mDay!!.isNotEmpty()) {
                            val valueInt = mDay!!.toInt()
                            if (valueInt !in 1..31) {
                                day.error = getString(R.string.error)
                            }
                        }
                    } catch (e: Exception) {
                        day.error = getString(R.string.error)
                        e.printStackTrace()
                    }
                    viewModel.dateValidation(mDay, mMonth, mYear)
                }
            })
            month.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    mMonth = month.text.toString()
                    try {
                        if (mMonth!!.isNotEmpty()) {
                            val valueInt = mMonth!!.toInt()
                            if (valueInt !in 1..12) {
                                month.error = getString(R.string.error)
                            }
                        }
                    } catch (e: Exception) {
                        month.error = getString(R.string.error)
                        e.printStackTrace()
                    }
                    viewModel.dateValidation(mDay, mMonth, mYear)
                }
            })
            year.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    mYear = year.text.toString()
                    try {
                        if (mYear!!.isNotEmpty()) {
                            val valueInt = mYear!!.toInt()
                            if (valueInt !in 1..2022) {
                                year.error = getString(R.string.error)
                            }
                        }
                    } catch (e: Exception) {
                        year.error = getString(R.string.error)
                        e.printStackTrace()
                    }
                    viewModel.dateValidation(mDay, mMonth, mYear)
                }
            })
        }
    }
}