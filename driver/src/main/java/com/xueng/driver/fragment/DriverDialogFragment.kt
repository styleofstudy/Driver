/*
 * Copyright (C) 2018 Drake, Inc. https://github.com/liangjingkanji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xueng.driver.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

abstract class DriverDialogFragment<B : ViewDataBinding>(@LayoutRes contentLayoutId: Int = 0) : DialogFragment(contentLayoutId), OnClickListener {

    lateinit var binding: B
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.bind(view)!!
        try {
            initContent()
        } catch (e: Exception) {
            Log.e("Engine", "Initializing failure", e)
        }
    }

    abstract fun initContent()

    override fun onClick(v: View) {}

    private var onDismissListener: DialogInterface.OnDismissListener? = null
    private var onCancelListener: DialogInterface.OnCancelListener? = null

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss(dialog)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onCancelListener?.onCancel(dialog)
    }
}
