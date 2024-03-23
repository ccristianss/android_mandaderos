package dev.crsi.manders.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.crsi.manders.R
import dev.crsi.manders.databinding.RequestManderItemBinding
import dev.crsi.manders.models.DetailRequestResponse

class RequestManderAdapter(private val listRequest: List<DetailRequestResponse>) :
    RecyclerView.Adapter<RequestManderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestManderViewHolder {
        return RequestManderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.request_mander_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RequestManderViewHolder, position: Int) {
        holder.render(listRequest[position])
    }

    override fun getItemCount(): Int = listRequest.size
}

class RequestManderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = RequestManderItemBinding.bind(view)

    fun render(data: DetailRequestResponse) {
        binding.detailRequest.text = data.detail_request
    }

}

