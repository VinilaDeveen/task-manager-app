package com.example.taskmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.databinding.FragmentSecondBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Screen that shows the list of tasks and allows add/edit/complete/delete.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        adapter = TaskAdapter(
            onTaskClicked = { task -> showAddEditDialog(task) },
            onTaskDelete = { task -> viewModel.deleteTask(task) },
            onTaskCompletedChanged = { task, isCompleted -> viewModel.toggleCompleted(task, isCompleted) }
        )

        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTasks.adapter = adapter

        binding.fabAddTask.setOnClickListener {
            showAddEditDialog(null)
        }

        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            adapter.submitList(tasks)
        }
    }

    private fun showAddEditDialog(task: Task?) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_task, null)
        val titleInput: EditText = dialogView.findViewById(R.id.editTextTitle)
        val descriptionInput: EditText = dialogView.findViewById(R.id.editTextDescription)

        if (task != null) {
            titleInput.setText(task.title)
            descriptionInput.setText(task.description)
        }

        val dialogTitle = if (task == null) "Add Task" else "Edit Task"

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(dialogTitle)
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val titleText = titleInput.text?.toString().orEmpty()
                val descText = descriptionInput.text?.toString()

                if (task == null) {
                    viewModel.addTask(titleText, descText)
                } else {
                    viewModel.updateTask(
                        task.copy(
                            title = titleText,
                            description = descText
                        )
                    )
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}