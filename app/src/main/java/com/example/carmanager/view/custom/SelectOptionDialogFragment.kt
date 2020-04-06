package com.example.carmanager.view.custom

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

import com.example.carmanager.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SelectOptionDialogFragment : BottomSheetDialogFragment() {

    private var listener: SelectOptionDialogFragmentListener? = null

    interface SelectOptionDialogFragmentListener {
        fun onCameraSelect()

        fun onGallerySelect()
    }

    fun setSelectOptionDialogFragmentListener(listener: SelectOptionDialogFragmentListener) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(context!!, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (dialog != null && dialog!!.window != null) {
            dialog!!.setCanceledOnTouchOutside(true)
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return inflater.inflate(R.layout.bottom_sheet_select_option, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)
        val tvCamera = view.findViewById<TextView>(R.id.tv_camera)
        val tvGallery = view.findViewById<TextView>(R.id.tv_gallery)

        tvCamera.setOnClickListener {
            if (listener != null) {
                listener!!.onCameraSelect()
            }
        }

        tvGallery.setOnClickListener {
            if (listener != null) {
                listener!!.onGallerySelect()
            }
        }

        btnCancel.setOnClickListener { dismiss() }
    }
}
