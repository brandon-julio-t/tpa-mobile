package edu.bluejack20_2.braven.pages.explore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import edu.bluejack20_2.braven.databinding.FragmentExploreBinding
import edu.bluejack20_2.braven.databinding.ItemPostBinding
import edu.bluejack20_2.braven.domains.post.PostViewHolderModule
import edu.bluejack20_2.braven.services.TimestampService
import javax.inject.Inject

class ExploreController @Inject constructor(
    private val timestampService: TimestampService,
    private val postViewHolderModule: PostViewHolderModule
) {
    private lateinit var binding: FragmentExploreBinding
    private lateinit var viewModel: ExploreViewModel

    fun bind(fragment: ExploreFragment) {
        this.binding = fragment.binding
        this.viewModel = fragment.viewModel

        handleViewModel(fragment)
        handleUI(fragment)
    }

    private fun handleViewModel(fragment: ExploreFragment) {
        binding.viewModel = viewModel

        viewModel.posts.observe(fragment.viewLifecycleOwner, {
            binding.exploreRecycleview.adapter?.notifyDataSetChanged()
        })

        viewModel.category.observe(fragment.viewLifecycleOwner, { viewModel.applyFilter() })
        viewModel.title.observe(fragment.viewLifecycleOwner, { viewModel.applyFilter() })
        viewModel.description.observe(fragment.viewLifecycleOwner, { viewModel.applyFilter() })
        viewModel.username.observe(fragment.viewLifecycleOwner, { viewModel.applyFilter() })
        viewModel.startDate.observe(fragment.viewLifecycleOwner, { viewModel.applyFilter() })
        viewModel.endDate.observe(fragment.viewLifecycleOwner, { viewModel.applyFilter() })
    }

    private fun handleUI(fragment: ExploreFragment) {
        binding.toggleFilter.setOnCheckedChangeListener { _, isChecked ->
            viewModel.resetFiler()

            listOf(
                binding.search,
                binding.category,
                binding.title,
                binding.description,
                binding.username,
                binding.startDate,
                binding.endDate
            ).forEach { it.editText?.setText("") }

            if (isChecked) {
                binding.filterContainer.visibility = View.VISIBLE
                binding.search.visibility = View.GONE
            } else {
                binding.filterContainer.visibility = View.GONE
                binding.search.visibility = View.VISIBLE
            }
        }

        binding.startDate.editText?.setOnClickListener {
            MaterialDatePicker.Builder.datePicker().build().let { picker ->
                picker.addOnPositiveButtonClickListener { viewModel.startDate.value = it }
                picker.show(fragment.requireActivity().supportFragmentManager, "start-picker")
                picker.addOnPositiveButtonClickListener {
                    binding.startDate.editText?.setText(
                        timestampService.formatMilliseconds(
                            it,
                            TimestampService.PRETTY_SHORT
                        )
                    )
                }
            }
        }

        binding.endDate.editText?.setOnClickListener {
            MaterialDatePicker.Builder.datePicker().build().let { picker ->
                picker.addOnPositiveButtonClickListener { viewModel.endDate.value = it }
                picker.show(fragment.requireActivity().supportFragmentManager, "end-picker")
                picker.addOnPositiveButtonClickListener {
                    binding.endDate.editText?.setText(
                        timestampService.formatMilliseconds(
                            it,
                            TimestampService.PRETTY_SHORT
                        )
                    )
                }
            }
        }

        binding.exploreRecycleview.layoutManager = object :
            LinearLayoutManager(fragment.requireActivity()) {
            override fun canScrollVertically() = false
        }

        binding.exploreRecycleview.adapter =
            object : RecyclerView.Adapter<PostViewHolderModule.ViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                    postViewHolderModule.ViewHolder(
                        ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                        fragment
                    )

                override fun onBindViewHolder(
                    holder: PostViewHolderModule.ViewHolder,
                    position: Int
                ) {
                    viewModel.posts.value?.get(position)?.let { holder.bind(it) }
            }

            override fun getItemCount() = viewModel.posts.value?.size ?: 0
        }
    }
}