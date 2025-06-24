package com.feko.generictabletoprpg.common.ui.components

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.ui.viewmodel.IToastSubViewModel
import com.feko.generictabletoprpg.features.action.Action
import com.feko.generictabletoprpg.features.ammunition.Ammunition
import com.feko.generictabletoprpg.features.armor.Armor
import com.feko.generictabletoprpg.features.condition.Condition
import com.feko.generictabletoprpg.features.disease.Disease
import com.feko.generictabletoprpg.features.feat.Feat
import com.feko.generictabletoprpg.features.spell.Spell
import com.feko.generictabletoprpg.features.weapon.Weapon


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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Dropdown(
    textFieldValue: String,
    dropdownExpanded: Boolean,
    enabled: Boolean,
    onDropdownExpandedStateChanged: (Boolean) -> Unit,
    dropdownMenuContent: @Composable () -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = dropdownExpanded,
        onExpandedChange = { onDropdownExpandedStateChanged(enabled && !dropdownExpanded) },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = {},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
            readOnly = true
        )
        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { onDropdownExpandedStateChanged(false) },
            modifier = Modifier.fillMaxWidth()
        ) {
            dropdownMenuContent()
        }
    }
}

sealed interface IInputFieldValueConverter<T : Number> : (String) -> T {
    data object IntInputFieldValueConverter : IInputFieldValueConverter<Int> {
        override fun invoke(inputValue: String): Int = inputValue.toIntOrNull() ?: 0
    }

    data object FloatInputFieldValueConverter : IInputFieldValueConverter<Float> {
        override fun invoke(inputValue: String): Float = inputValue.toFloatOrNull() ?: 0f
    }
}

@Composable
fun <T : Number> NumberInputField(
    value: T,
    label: String,
    convertInputValue: IInputFieldValueConverter<T>,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    onFormSubmit: () -> Unit = {},
    canSubmitForm: (T) -> Boolean = { true },
    isInputFieldValid: (Number) -> Boolean = { true },
    focusRequester: FocusRequester = remember { FocusRequester() },
    focusManager: FocusManager = LocalFocusManager.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    focusDirection: FocusDirection = FocusDirection.Down,
    autoFocus: Boolean = false,
    maxLines: Int = 1,
    suffix: @Composable (() -> Unit) = {},
    colors: TextFieldColors = TextFieldDefaults.colors()
) {
    var inputValue by remember { mutableStateOf(if (value == 0) "" else value.toString()) }
    @Suppress("KotlinConstantConditions")
    if (value != 0 && value != convertInputValue(inputValue)) {
        inputValue = value.toString()
    }
    InputField(
        inputValue,
        label,
        onValueChange = {
            inputValue = it
            onValueChange(convertInputValue(it))
        },
        modifier,
        onFormSubmit,
        canSubmitForm = { canSubmitForm(convertInputValue(it)) },
        isInputFieldValid = { isInputFieldValid(convertInputValue(it)) },
        focusRequester,
        focusManager,
        keyboardOptions,
        focusDirection,
        autoFocus,
        maxLines,
        suffix,
        colors
    )
}

@Composable
fun InputField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFormSubmit: () -> Unit = {},
    canSubmitForm: (String) -> Boolean = { true },
    isInputFieldValid: (String) -> Boolean = { true },
    focusRequester: FocusRequester = remember { FocusRequester() },
    focusManager: FocusManager = LocalFocusManager.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    focusDirection: FocusDirection = FocusDirection.Down,
    autoFocus: Boolean = false,
    maxLines: Int = 1,
    suffix: @Composable (() -> Unit) = {},
    colors: TextFieldColors = TextFieldDefaults.colors()
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        maxLines = maxLines,
        isError = !isInputFieldValid(value),
        suffix = suffix,
        label = {
            Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        },
        trailingIcon = {
            IconButton(onClick = { onValueChange("") })
            { Icon(Icons.Default.Clear, "") }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .then(modifier),
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(focusDirection) },
            onDone = {
                if (canSubmitForm(value)) {
                    onFormSubmit()
                }
            }
        ),
        colors = colors
    )
    if (autoFocus) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFABButtonWithDropdown(
    expanded: Boolean,
    modifier: Modifier,
    onDismissRequest: () -> Unit,
    onFabClicked: () -> Unit,
    dropdownMenuContent: @Composable () -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {},
        modifier = Modifier
            .wrapContentSize()
            .then(modifier)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            content = {
                dropdownMenuContent()
            }
        )
        AddFABButton(
            Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
        ) { onFabClicked() }
    }
}

@Composable
fun AddFABButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        Modifier
            .size(48.dp)
            .then(modifier)
    ) {
        Icon(Icons.Default.Add, "")
    }
}

val appNamesByType = mapOf(
    Action::class.java to R.string.action,
    Ammunition::class.java to R.string.ammunition,
    Armor::class.java to R.string.armor,
    Condition::class.java to R.string.condition,
    Disease::class.java to R.string.disease,
    Feat::class.java to R.string.feat,
    Spell::class.java to R.string.spell,
    Weapon::class.java to R.string.weapon
)

val appTypes = appNamesByType.keys

@Composable
fun <T> getTypeName(type: Class<T>): String = stringResource(appNamesByType[type]!!)

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
    textStringResource: Int,
    onCheckChanged: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onCheckChanged(!isChecked) }
            .padding(end = 8.dp),
        Arrangement.Start,
        Alignment.CenterVertically
    ) {
        Checkbox(
            isChecked,
            { checked -> onCheckChanged(checked) }
        )
        Text(stringResource(textStringResource))
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