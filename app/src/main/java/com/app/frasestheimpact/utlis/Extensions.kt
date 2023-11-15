package com.app.frasestheimpact.utlis

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun numberCurrency(
    number: String
): String? {
    val decimalFormat = DecimalFormat("###,###,##0.00")
    return decimalFormat.format(java.lang.Double.parseDouble(number))
}

fun View.disable() {
    isEnabled = false
}

fun View.enabled() {
    isEnabled = true
}

fun Fragment.toast(msg: String?) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
}
fun Activity.toast(msg: String?) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}


fun Long.toPrettyDate(): String {
    val nowTime = Calendar.getInstance()
    val neededTime = Calendar.getInstance()
    neededTime.timeInMillis = this

    return if (neededTime[Calendar.YEAR] == nowTime[Calendar.YEAR]) {
        if (neededTime[Calendar.MONTH] == nowTime[Calendar.MONTH]) {
            when {
                neededTime[Calendar.DATE] - nowTime[Calendar.DATE] == 1 -> {
                    //here return like "Tomorrow at 12:00"
                    "Amanhã às " + SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this))
                }

                nowTime[Calendar.DATE] == neededTime[Calendar.DATE] -> {
                    //here return like "Today at 12:00"
                    " Hoje às " + SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this))
                }

                nowTime[Calendar.DATE] - neededTime[Calendar.DATE] == 1 -> {
                    //here return like "Yesterday at 12:00"
                    "Ontem às " + SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this))
                }

                else -> {
                    //here return like "May 31, 12:00"
                    SimpleDateFormat("MMMM d", Locale.getDefault()).format(Date(this))
                }
            }
        } else {
            //here return like "May 31, 12:00"
            SimpleDateFormat("MMMM d", Locale.getDefault()).format(Date(this))
        }
    } else {
        //here return like "May 31 2022, 12:00" - it's a different year we need to show it
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(this))
    }
}

fun copyText(string: String, context: Context){
    val  clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipdata = ClipData.newPlainText("Text copied", string)
    clipboardManager.setPrimaryClip(clipdata)
}

fun spinnerFunText(
    context: Context,
    spinner: AutoCompleteTextView,
    list: List<Any>
) {
    val adapterSpinner = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)
    adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner.setAdapter(adapterSpinner)
}

fun generateRandomNumbers(
    nextInt: Int,
    mais: Int,
    numeroEscolhido: Int
): Array<String> {

    val random = Random()

    val numbers = mutableListOf<String>()
    // Gerar 6 números únicos aleatórios de 01 a 60
    while (numbers.size < numeroEscolhido) {
        val randomNumber = random.nextInt(nextInt) + mais
        val formattedNumber = String.format(Locale.US, "%02d", randomNumber)

        if (!numbers.contains(formattedNumber)) {
            numbers.add(formattedNumber)
        }
    }

    return numbers.toTypedArray()
}



fun verifiqueConexao(context: Context):Boolean{
    val connectivityManneger = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        val networkCapabilities = connectivityManneger.activeNetwork ?: return false
        val activeNetwork =
            connectivityManneger.getNetworkCapabilities(networkCapabilities) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }else{
        val activeNetworkInfo = connectivityManneger.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}

fun dataFormat():String{
    val calendar = Calendar.getInstance()
    val dataAtual = calendar.time

    val formato = SimpleDateFormat("dd/MM/yyyy")
    val dataFormatada = formato.format(dataAtual)

    return dataFormatada
}


fun spinnerFun(context: Context, spinner: Spinner, list: List<Any>) {
    val adapterSpinner = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)
    adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner.adapter = adapterSpinner
}

fun Context.createDialog(layout: Int, cancelable: Boolean): Dialog {
    val dialog = Dialog(this, android.R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(layout)
    dialog.window?.setGravity(Gravity.CENTER)
    dialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(cancelable)
    return dialog
}

val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun TextView.setColouredSpan(
    word: String,
    start: Int, end: Int,
    color: Int,
) {
    this.text = word
    val spannableString = SpannableString(text)
    try {
        spannableString.setSpan(
            ForegroundColorSpan(color),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text = spannableString
    } catch (e: IndexOutOfBoundsException) {
        println("$word was not not found in TextView text")
    }
}
