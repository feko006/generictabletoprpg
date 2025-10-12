package com.feko.generictabletoprpg.common.ui.components

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.shared.common.appNamesByType
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.IToastSubViewModel
import org.jetbrains.compose.resources.stringResource
import kotlin.reflect.KClass

@Composable
fun TextWithLabel(
    @StringRes labelResource: Int,
    text: String
) {
    Text(
        buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(stringResource(labelResource))
            }
            append(": $text")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFABButtonWithDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onFabClicked: () -> Unit,
    dropdownMenuContent: @Composable () -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {},
    ) {
        AddFABButton(
            Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        ) { onFabClicked() }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            matchAnchorWidth = false,
            shape = MaterialTheme.shapes.extraLarge
        ) {
            dropdownMenuContent()
        }
    }
}

@Composable
fun AddFABButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    GttrpgFloatingActionButton(
        onClick = onClick,
        Modifier
            .size(48.dp)
            .then(modifier)
    ) {
        Icon(Icons.Default.Add, "")
    }
}


@Composable
fun <T : Any> getTypeName(type: KClass<T>): String = stringResource(appNamesByType[type]!!)

@Composable
fun ToastMessage(toast: IToastSubViewModel) {
    val shouldShowToastMessage by toast.shouldShowMessage.collectAsState(false)
    if (shouldShowToastMessage) {
        Toast
            .makeText(
                LocalContext.current,
                toast.getMessage(),
                Toast.LENGTH_SHORT
            )
            .show()
        toast.messageConsumed()
    }
}

@Composable
fun CheckboxWithText(
    isChecked: Boolean,
    text: IText,
    onCheckChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onCheckChange(!isChecked) }
            .padding(end = LocalDimens.current.paddingSmall),
        Arrangement.Start,
        Alignment.CenterVertically
    ) {
        Checkbox(
            isChecked,
            { checked -> onCheckChange(checked) }
        )
        Text(text.text())
    }
}

@Composable
fun BoxWithScrollIndicator(
    scrollableState: ScrollableState,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    scrollUpArrowAlignment: Alignment = Alignment.TopCenter,
    scrollDownArrowAlignment: Alignment = Alignment.BottomCenter,
    fadeSize: Dp = 36.dp,
    arrowSize: Dp = 24.dp,
    content: @Composable (BoxScope.() -> Unit)
) {
    Box(modifier) {
        content()
        if (scrollableState.canScrollBackward) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(fadeSize)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                backgroundColor,
                                Color.Transparent
                            )
                        )
                    )
            )
            Icon(
                Icons.Default.KeyboardArrowUp,
                "",
                Modifier
                    .size(arrowSize)
                    .align(scrollUpArrowAlignment)
            )
        }
        if (scrollableState.canScrollForward) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(fadeSize)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                backgroundColor
                            )
                        )
                    )
            )
            Icon(
                Icons.Default.KeyboardArrowDown,
                "",
                Modifier
                    .size(arrowSize)
                    .align(scrollDownArrowAlignment)
            )
        }
    }
}

@Composable
fun StableAnimatedVisibility(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible,
        enter = fadeIn(),
        exit = fadeOut(),
        content = content
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GttrpgTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    colors: TextFieldColors = TextFieldDefaults.colors(),
) {
    TextField(
        value,
        onValueChange,
        modifier,
        enabled,
        readOnly,
        textStyle,
        label,
        placeholder,
        leadingIcon,
        trailingIcon,
        prefix,
        suffix,
        supportingText,
        isError,
        visualTransformation,
        keyboardOptions,
        keyboardActions,
        singleLine,
        maxLines,
        minLines,
        interactionSource,
        MaterialTheme.shapes.extraLarge.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        colors
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GttrpgOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    OutlinedTextField(
        value,
        onValueChange,
        modifier,
        enabled,
        readOnly,
        textStyle,
        label,
        placeholder,
        leadingIcon,
        trailingIcon,
        prefix,
        suffix,
        supportingText,
        isError,
        visualTransformation,
        keyboardOptions,
        keyboardActions,
        singleLine,
        maxLines,
        minLines,
        interactionSource,
        MaterialTheme.shapes.extraLarge.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        colors
    )
}

@Composable
fun GttrpgFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: androidx.compose.material3.FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        interactionSource = interactionSource,
        content = content
    )
}