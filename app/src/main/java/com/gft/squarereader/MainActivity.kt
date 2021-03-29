package com.gft.squarereader

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gft.squarereader.databinding.ActivityMainBinding
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val SCAN_CARD_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scanCardButton.setOnClickListener { onScanCardClick() }
        binding.scanIdButton.setOnClickListener { onScanIdClick() }
    }

    private fun onScanCardClick() {
        val scanIntent = Intent(this, CardIOActivity::class.java)

        // Customization options
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false)
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, false)
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false)
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false)
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true)
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false)
        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, false)
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, false)
        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_INSTRUCTIONS, false)

        // set this to TRUE to retrieve card image, not only card data
        scanIntent.putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true)

        startActivityForResult(scanIntent, SCAN_CARD_REQUEST_CODE)
    }

    private fun onScanIdClick() {
        // TODO: implement scanning for other kinds of documents.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SCAN_CARD_REQUEST_CODE -> handleCardScanResult(data)
            // handle other request codes here
        }
    }

    private fun handleCardScanResult(data: Intent?) {
        if (data == null) return

        // here parsed card data can be used
        if (data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            val scanResult = data.getParcelableExtra<CreditCard>(CardIOActivity.EXTRA_SCAN_RESULT)
            Toast.makeText(this, scanResult.toString(), Toast.LENGTH_SHORT).show()
            Log.i("handleScanResult", scanResult.cardNumber)
            Log.i("handleScanResult", scanResult.cardType.name)
        }

        // here captured image can be retrieved as a JPEG formatted byte array
        if (data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            val cardImage = data.getByteArrayExtra(CardIOActivity.EXTRA_CAPTURED_CARD_IMAGE)
            val targetFile = File(Environment.getExternalStorageDirectory(), "cardImage.jpg")
            try {
                val fos = FileOutputStream(targetFile.path)
                fos.write(cardImage)
                fos.close()
            } catch (e: Exception) {
                Log.e("handleScanResult", "Exception saving file", e)
            }
        }
    }
}