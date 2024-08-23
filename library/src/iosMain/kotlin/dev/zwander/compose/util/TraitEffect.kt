package dev.zwander.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.interop.LocalUIViewController
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UITraitCollection
import platform.UIKit.UIView

@Composable
fun TraitEffect(
    key: Any? = Unit,
    onTraitsChanged: () -> Unit,
) {
    val viewController = LocalUIViewController.current

    DisposableEffect(key) {
        val view: UIView = viewController.view
        val traitView = TraitView(onTraitsChanged)

        traitView.onCreate(view)

        onDispose {
            traitView.onDestroy()
        }
    }
}

// https://github.com/JetBrains/compose-multiplatform/issues/3213#issuecomment-1572378546
@ExportObjCClass
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private class TraitView(
    private val onTraitChanged: () -> Unit,
) : UIView(frame = CGRectZero.readValue()) {
    override fun traitCollectionDidChange(previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)
        onTraitChanged()
    }

    fun onCreate(parent: UIView) {
        parent.addSubview(this)
    }

    fun onDestroy() {
        removeFromSuperview()
    }
}