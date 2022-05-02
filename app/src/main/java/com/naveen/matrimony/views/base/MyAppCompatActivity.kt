package com.naveen.matrimony.views.base

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.snackbar.Snackbar
import com.gotech.youtube.enums.ErrorMessageType
import com.gotech.youtube.enums.LoaderStatus
import com.naveen.matrimony.R
import com.naveen.matrimony.managers.SharedPrefManager
import com.naveen.matrimony.model.RecommendedUser
import com.naveen.matrimony.model.User
import com.naveen.matrimony.viewmodel.MyBaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import java.util.*


abstract class MyAppCompatActivity : AppCompatActivity() {
    protected val TAG = this.javaClass.simpleName
    protected val WAIT_TIME: Long = 300

    protected val handler = CoroutineExceptionHandler { _, exception ->
        showToast(exception.message ?: getString(R.string.no_internet_connection))
        exception.printStackTrace()
    }

    protected var animateFinish = true
    private var mBaseView: ViewGroup? = null
    private var mLoaderView: View? = null
    private var progressShown = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initProgress()
    }

    //Initializing the progress view
    private fun initProgress() {
        mBaseView = this.findViewById(android.R.id.content)
        //    mLoaderView = View.inflate(this, R.layout.loader, null)
    }


    //To hide Keyboard
    fun hideKeyboard() {
        val inputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = this.currentFocus
        if (view != null)
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun noInternetAlert() {
        showAlertDialogOk(
            "SSOD",
            "Please check your internet connection!!",
            DialogInterface.OnClickListener { dialog, which ->
                dialog?.dismiss()
            })
    }


    //To show keyboard
    fun showKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // check if no view has focus:
        val view = this.currentFocus
        if (view != null)
            inputManager.toggleSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.SHOW_FORCED,
                0
            )
    }

    //To show snackbar
    protected fun showSnackbar(
        message: String?,
        errorMessageType: ErrorMessageType = ErrorMessageType.Snackbar
    ) {
        val updatedMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            (Html.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY))
        } else {
            (Html.fromHtml(message))
        }

        val snackbarMessage = SpannableStringBuilder(updatedMessage)
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content), snackbarMessage,
            Snackbar.LENGTH_LONG
        )
        snackbar.duration = 2000
        val snackBarView = snackbar.view
        var snackbarBg = R.color.secondaryVariant
        var snackbarTextColor = R.color.onSecondary
        if (errorMessageType == ErrorMessageType.SnackbarError) {
            snackbarBg = R.color.secondaryVariant
            snackbarTextColor = R.color.onSecondary
        }
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, snackbarBg))
        val textView =
            snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
//        textView.maxLines = 2
        textView.setTextColor(ContextCompat.getColor(this, snackbarTextColor))
        snackbar.show()
    }

    //To show toast message
//    protected fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }

    //Get the instance of sharedPreferenceManager
    val sharedPrefManager: SharedPrefManager
        get() {
            return SharedPrefManager.getInstance(this)
        }

    private val defaultDialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        dialog.dismiss()
    }

    //To show alert dialog with 'ok' button alone
    protected fun showAlertDialogOk(
        title: String,
        message: String,
        listener: DialogInterface.OnClickListener = defaultDialogClickListener
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.ok), listener)
        val mAlertDialog = builder.create()
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)

        mAlertDialog.setOnShowListener {
            mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.secondary))
        }

        mAlertDialog.show()
    }

    //To show alert dialog with Positive and Negative button with positive button listener alone
    protected fun showConfirmation(
        negativeText: String,
        positiveText: String,
        title: String,
        message: String,
        listener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveText, listener)
        builder.setNegativeButton(negativeText) { dialog, _ ->
            dialog.dismiss()
        }
        val mAlertDialog = builder.create()
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)

        mAlertDialog.setOnShowListener {
            mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.primary))
            mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.primary))
        }


        mAlertDialog.show()
    }

    protected fun showToast(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_LONG).show()

    }

    //To show alert dialog with Positive and Negative button with positive and negative button listener
    protected fun showConfirmation(
        negativeText: String,
        positiveText: String,
        title: String?,
        message: String,
        listener: DialogInterface.OnClickListener,
        negativeListener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(this)
        if (title != null)
            builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveText, listener)
        builder.setNegativeButton(negativeText, negativeListener)
        val mAlertDialog = builder.create()
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)

        mAlertDialog.setOnShowListener {
            mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.primary))
            mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.primary))
        }

        mAlertDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //To show loader progress
    open fun showProgress() {
        hideKeyboard()
        if (!progressShown) {
            mBaseView!!.addView(mLoaderView)
            progressShown = true
        }
    }

    //To hide loader progress
    open fun hideProgress() {
        if (progressShown) {
            mBaseView!!.removeView(mLoaderView)
            progressShown = false
        }
    }


    //Call this method while setting up Viewmodel to init progress
    protected fun setUpLoader(vararg viewModels: MyBaseViewModel) {

        viewModels.forEach {
            it.isLoading.observe(this) { ldr ->
                if (ldr.equals(LoaderStatus.loading))
                    showProgress()
                else
                    hideProgress()
            }

            it.errorMediatorLiveData.observe(this) {
                it?.let {
                    val updatedErrorMessage: String
                    if (it.contains("_")) {
                        updatedErrorMessage = it.replace("_", " ")

                        showSnackbar(updatedErrorMessage.lowercase(Locale.getDefault()))
                    } else {
                        updatedErrorMessage = it
                    }

                    onErrorCalled(updatedErrorMessage.lowercase())


                }
            }
        }


        initObservers()

    }

    fun Activity.changeStatusBarColor(color: Int, isLight: Boolean) {
        window.statusBarColor = color
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLight
    }

    abstract fun initObservers()

    protected abstract fun onErrorCalled(it: String?)

    protected fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    protected fun getPendingList(): ArrayList<User> {
        val list: ArrayList<User> = ArrayList()
        val naveenImgList: ArrayList<String> = ArrayList()
        val premImgList: ArrayList<String> = ArrayList()
        val praveenImgList: ArrayList<String> = ArrayList()
        val nirmalImgList: ArrayList<String> = ArrayList()


        naveenImgList.add("https://images.pexels.com/photos/842811/pexels-photo-842811.jpeg?cs=srgb&dl=pexels-andrea-piacquadio-842811.jpg&fm=jpg")
        naveenImgList.add("https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?cs=srgb&dl=pexels-pixabay-220453.jpg&fm=jpg")
        naveenImgList.add("https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg?cs=srgb&dl=pexels-italo-melo-2379004.jpg&fm=jpg")

        premImgList.add("https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg?cs=srgb&dl=pexels-italo-melo-2379004.jpg&fm=jpg")
        premImgList.add("https://images.pexels.com/photos/839011/pexels-photo-839011.jpeg?cs=srgb&dl=pexels-andrea-piacquadio-839011.jpg&fm=jpg")
        premImgList.add("https://images.pexels.com/photos/906531/pexels-photo-906531.jpeg?cs=srgb&dl=pexels-mathew-thomas-906531.jpg&fm=jpg")

        praveenImgList.add("https://images.pexels.com/photos/868113/pexels-photo-868113.jpeg?cs=srgb&dl=pexels-mostafa-sannad-868113.jpg&fm=jpg")
        praveenImgList.add("https://images.pexels.com/photos/1121796/pexels-photo-1121796.jpeg?cs=srgb&dl=pexels-daniel-xavier-1121796.jpg&fm=jpg")
        praveenImgList.add("https://images.pexels.com/photos/1182825/pexels-photo-1182825.jpeg?cs=srgb&dl=pexels-omar-l%C3%B3pez-1182825.jpg&fm=jpg")

        nirmalImgList.add("https://images.pexels.com/photos/1516680/pexels-photo-1516680.jpeg?cs=srgb&dl=pexels-nitin-khajotia-1516680.jpg&fm=jpg")
        nirmalImgList.add("https://images.pexels.com/photos/868113/pexels-photo-868113.jpeg?cs=srgb&dl=pexels-mostafa-sannad-868113.jpg&fm=jpg")
        nirmalImgList.add("https://images.pexels.com/photos/878358/pexels-photo-878358.jpeg?cs=srgb&dl=pexels-mostafa-sannad-878358.jpg&fm=jpg")



        list.add(

            User(
                1023,
                "Naveen",
                naveenImgList,
                "25, Ramanathan St, Alagappa Nagar, Kilpauk, Chennai, Tamil Nadu 600010, India\n",
                isNew = false,
                isVerified = false,
                isPremium = false
            )
        )
        list.add(
            User(
                1234,
                "Prem",
                premImgList,
                "No. 327, First Floor, Avvai Shanmugam Salai, Royapettah, Chennai, Tamil Nadu 600014, India",
                isNew = true,
                isVerified = false,
                isPremium = false
            )
        )
        list.add(
            User(
                1249,
                "Praveen",
                praveenImgList,
                "No:150, Ganga Nagar, Kodambakkam, Chennai, Tamil Nadu 600024, India",
                isNew = false,
                isVerified = true,
                isPremium = true
            )
        )
        list.add(
            User(
                4950,
                "Nirmal",
                nirmalImgList,
                "No. 327, First Floor, Avvai Shanmugam Salai, Royapettah, Chennai, Tamil Nadu 600014, India",
                isNew = true,
                isVerified = false,
                isPremium = true
            )
        )


        return list
    }

    protected fun getRecommendedList(): ArrayList<RecommendedUser> {
        val list: ArrayList<RecommendedUser> = ArrayList()
        val kannanImageList: ArrayList<String> = ArrayList()
        val muthuImageList: ArrayList<String> = ArrayList()
        val prabhuImageList: ArrayList<String> = ArrayList()
        val sumanImageList: ArrayList<String> = ArrayList()
        val vinothImageList: ArrayList<String> = ArrayList()
        val rishiImageList: ArrayList<String> = ArrayList()

        kannanImageList.add("https://images.pexels.com/photos/4897993/pexels-photo-4897993.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        kannanImageList.add("https://images.pexels.com/photos/3977441/pexels-photo-3977441.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        kannanImageList.add("https://www.pexels.com/photo/man-in-gray-and-white-sport-shirt-3152227.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")

        muthuImageList.add("https://images.pexels.com/photos/2210900/pexels-photo-2210900.jpeg")
        muthuImageList.add("https://images.pexels.com/photos/2864797/pexels-photo-2864797.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        muthuImageList.add("https://images.pexels.com/photos/2890707/pexels-photo-2890707.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")

        prabhuImageList.add("https://images.pexels.com/photos/829552/pexels-photo-829552.jpeg?cs=srgb&dl=pexels-abhishek-gaurav-829552.jpg&fm=jpg")
        prabhuImageList.add("https://images.pexels.com/photos/1081671/pexels-photo-1081671.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        prabhuImageList.add("https://images.pexels.com/photos/1211588/pexels-photo-1211588.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")

        sumanImageList.add("https://images.pexels.com/photos/1018911/pexels-photo-1018911.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        sumanImageList.add("https://images.pexels.com/photos/1081676/pexels-photo-1081676.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        sumanImageList.add("https://images.pexels.com/photos/1183266/pexels-photo-1183266.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")

        vinothImageList.add("https://images.pexels.com/photos/1121796/pexels-photo-1121796.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        vinothImageList.add("https://images.pexels.com/photos/1431283/pexels-photo-1431283.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        vinothImageList.add("https://images.pexels.com/photos/3147528/pexels-photo-3147528.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")

        rishiImageList.add("https://images.pexels.com/photos/5323352/pexels-photo-5323352.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        rishiImageList.add("https://images.pexels.com/photos/10353622/pexels-photo-10353622.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")
        rishiImageList.add("https://images.pexels.com/photos/10162995/pexels-photo-10162995.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")


        list.add(
            RecommendedUser(
                101,
                "Kannan R",
                kannanImageList,
                "25, Ramanathan St, Alagappa Nagar, Kilpauk, Chennai, Tamil Nadu 600010, India\n",
                isVerified = true,
                isPremium = false
            )
        )
        list.add(
            RecommendedUser(
                102,
                "Muthu M.K",
                muthuImageList,
                "No. 327, First Floor, Avvai Shanmugam Salai, Royapettah, Chennai, Tamil Nadu 600014, India",
                isVerified = false,
                isPremium = true
            )
        )
        list.add(
            RecommendedUser(
                103,
                "Prabhu B",
                prabhuImageList,
                "No:150, Ganga Nagar, Kodambakkam, Chennai, Tamil Nadu 600024, India",
                isVerified = true,
                isPremium = false
            )
        )
        list.add(
            RecommendedUser(
                104,
                "Suman D",
                sumanImageList,
                "No. 327, First Floor, Avvai Shanmugam Salai, Royapettah, Chennai, Tamil Nadu 600014, India",
                isVerified = true,
                isPremium = true
            )
        )

        list.add(
            RecommendedUser(
                105,
                "Vinoth D",
                vinothImageList,
                "No. 327, First Floor, Avvai Shanmugam Salai, Royapettah, Chennai, Tamil Nadu 600014, India",
                isVerified = true,
                isPremium = false
            )
        )

        list.add(
            RecommendedUser(
                105,
                "Rishi E",
                rishiImageList,
                "No. 327, First Floor, Avvai Shanmugam Salai, Royapettah, Chennai, Tamil Nadu 600014, India",
                isVerified = true,
                isPremium = false
            )
        )


        return list
    }
}