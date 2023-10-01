package com.app.dayplan.advertise

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView
import com.app.dayplan.R
import kotlin.math.roundToInt

class ViewPagerAdapter(private val items: List<Int>) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    class ViewHolder(val cardView: CardView, val imageView: ImageView) : RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

//        val cardView = CardView(parent.context)
//        cardView.layoutParams = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
//
        // 모서리 반경을 설정합니다.
//        cardView.radius = 16.0f
//
//        val imageView = ImageView(parent.context)
//        cardView.addView(imageView)
//

        val cardView = CardView(parent.context)
        val imageView = ImageView(parent.context)

        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // 여기서 마진을 설정하여 카드 사이의 간격을 조정합니다.
        val margin = parent.context.resources.getDimensionPixelSize(R.dimen.card_margin)
        layoutParams.setMargins(margin, 0, margin, 0)
        cardView.layoutParams = layoutParams

        // 모서리 반경을 설정합니다.
        cardView.radius = 16.0f
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        cardView.addView(imageView)

        return ViewHolder(cardView, imageView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageResource(items[position])
    }

    override fun getItemCount() = items.size
}