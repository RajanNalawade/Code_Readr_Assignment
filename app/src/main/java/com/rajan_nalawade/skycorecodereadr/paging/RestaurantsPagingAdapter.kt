package com.rajan_nalawade.skycorecodereadr.paging


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rajan_nalawade.skycorecodereadr.databinding.RowItemRestaurantsBinding
import com.rajan_nalawade.skycorecodereadr.models.Businesse

class RestaurantsPagingAdapter :
    PagingDataAdapter<Businesse, RestaurantsPagingAdapter.RestaurantViewHolder>(COMPARATOR) {

    inner class RestaurantViewHolder(val vhBinding: RowItemRestaurantsBinding) :
        RecyclerView.ViewHolder(vhBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = RowItemRestaurantsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                vhBinding.txtRestaurantName.text = this?.name?.toString() ?: ""
                vhBinding.txtDistance.text =
                    "${String.format("%.1f", this?.distance)} M" ?: ""
                vhBinding.txtAddress.text = this?.location?.displayAddress?.get(0)?.toString() ?: ""
                vhBinding.btnRatings.text = this?.rating?.toString() ?: "0.0"
                vhBinding.txtCurrentOpen.text =
                    "Currently ${if (this?.isClosed!! == true) "Closed" else "Open"}"

                Glide.with(holder.itemView.context)
                    .load(this?.imageUrl?.toString() ?: "").into(vhBinding.imgRestaurant)
            }
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Businesse>() {
            override fun areItemsTheSame(
                oldItem: Businesse,
                newItem: Businesse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Businesse,
                newItem: Businesse
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}