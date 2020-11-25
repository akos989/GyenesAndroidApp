package hu.gyenes.paintball.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import hu.gyenes.paintball.app.R
import hu.gyenes.paintball.app.model.Reservation
import kotlinx.android.synthetic.main.item_reservation_preview.view.*

class ReservationAdapter : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {
    inner class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Reservation>() {
        override fun areItemsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
            return oldItem.reservationId == newItem.reservationId
        }

        override fun areContentsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        return ReservationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_reservation_preview,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = differ.currentList[position]
        holder.itemView.apply {
            tvName.text = reservation.name
            tvPlayerNumber.text = context.getString(R.string.player_number_value, reservation.playerNumber)
            tvDate.text = reservation.date.toString()
            setOnClickListener {
                onItemClickListener?.let { it(reservation) }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClickListener: ((Reservation) -> Unit)? = null

    fun setOnClickListener(listener: (Reservation) -> Unit) {
        onItemClickListener = listener
    }
}