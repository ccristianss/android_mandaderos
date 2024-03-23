package dev.crsi.manders.adapters.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.crsi.manders.databinding.MandersItemBinding
import dev.crsi.manders.models.DetailRequestResponse

class DetailMandersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    //MandersItemBinding: Este sale de layouts para el diseño  de cada item
    private val binding = MandersItemBinding.bind(view)

    fun render(data: DetailRequestResponse) {
        binding.idDetailRequest.text = data.detail_request
        binding.idStatusRequest.text = data.status_request
        binding.idDateRegisterRequest.text = data.dateregister_request
    }

}