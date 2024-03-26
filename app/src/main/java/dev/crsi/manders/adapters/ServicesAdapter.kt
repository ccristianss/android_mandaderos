package dev.crsi.manders.adapters

//import com.bumptech.glide.Glide
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.crsi.manders.R
import dev.crsi.manders.databinding.ServicesItemBinding
import dev.crsi.manders.models.ServiceResponse
import dev.crsi.manders.ui.RequestActivity

class ServicesAdapter(private val listRequest: List<ServiceResponse>) :
    RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        return ServiceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.services_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.render(listRequest[position])
    }

    override fun getItemCount(): Int = listRequest.size


    class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ServicesItemBinding.bind(view)

        fun render(data: ServiceResponse) {
            binding.nameService.text = data.name_service
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, RequestActivity::class.java)
                intent.putExtra("id_service", data.id_service)
                intent.putExtra("name_service", data.name_service)
                intent.putExtra("detail_service", data.detail_service)
                intent.putExtra("image_service", data.image_service)
                itemView.context.startActivity(intent)
            }
            Picasso.get()
                .load(data.image_service)
                .into(binding.imageService)

            /*Glide
                .with(itemView.context)
                .load(data.image_service)
                .into(binding.imageService)*/
        }

    }
}

