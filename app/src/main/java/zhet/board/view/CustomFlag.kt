package zhet.board.view

import android.content.Context
import android.view.View
import android.widget.TextView
import com.skydoves.colorpickerpreference.ColorEnvelope
import com.skydoves.colorpickerpreference.FlagView
import zhet.board.R

class CustomFlag(context: Context, layout: Int) : FlagView(context, layout) {

    private var textView: TextView = findViewById(R.id.flag_color_code)

    private var view: View = findViewById(R.id.flag_color_layout)

    override fun onRefresh(colorEnvelope: ColorEnvelope) {
        textView.text = "#" + String.format("%06X", 0xFFFFFF and colorEnvelope.color)
        view.setBackgroundColor(colorEnvelope.color)
    }

}
