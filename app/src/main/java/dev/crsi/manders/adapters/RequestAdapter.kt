package dev.crsi.manders.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.crsi.manders.R
import dev.crsi.manders.databinding.RequestItemBinding
import dev.crsi.manders.models.DetailRequestResponse

class RequestAdapter(private val listRequest: List<DetailRequestResponse>) :
    RecyclerView.Adapter<RequestAdapter.DetailRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailRequestViewHolder {
        return DetailRequestViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.request_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DetailRequestViewHolder, position: Int) {
        holder.render(listRequest[position])
    }

    override fun getItemCount(): Int = listRequest.size


    class DetailRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = RequestItemBinding.bind(view)

        fun render(data: DetailRequestResponse) {
            binding.detailRequest.text = data.detail_request
            binding.statusRequest.text = data.status_request
            binding.idService.text = data.service_id_service.toString()
        }
    }
}

