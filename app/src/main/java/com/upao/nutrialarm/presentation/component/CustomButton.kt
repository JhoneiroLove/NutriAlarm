package com.upao.nutrialarm.presentation.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upao.nutrialarm.ui.theme.*

enum class ButtonType {
    PRIMARY, SECONDARY, SUCCESS, WARNING, DANGER
}

enum class ButtonSize {
    SMALL, MEDIUM, LARGE
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NutriButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.PRIMARY,
    size: ButtonSize = ButtonSize.MEDIUM,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    fillMaxWidth: Boolean = false
) {
    val colors = getButtonColors(type)
    val dimensions = getButtonDimensions(size)

    val buttonModifier = if (fillMaxWidth) {
        modifier.fillMaxWidth().height(dimensions.height)
    } else {
        modifier.height(dimensions.height)
    }

    Button(
        onClick = onClick,
        modifier = buttonModifier,
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.background,
            contentColor = colors.content,
            disabledContainerColor = NutriGray.copy(alpha = 0.3f),
            disabledContentColor = NutriGray
        ),
        shape = RoundedCornerShape(dimensions.cornerRadius),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = dimensions.elevation,
            pressedElevation = dimensions.elevation / 2
        ),
        contentPadding = PaddingValues(
            horizontal = dimensions.horizontalPadding,
            vertical = dimensions.verticalPadding
        )
    ) {
        AnimatedContent(
            targetState = isLoading,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
            }
        ) { loading ->
            if (loading) {
                LoadingButtonContent(size = size)
            } else {
                ButtonContent(
                    text = text,
                    icon = icon,
                    size = size
                )
            }
        }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    icon: ImageVector?,
    size: ButtonSize
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier.size(getButtonDimensions(size).iconSize)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = text,
            fontSize = getButtonDimensions(size).fontSize,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun LoadingButtonContent(size: ButtonSize) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(getButtonDimensions(size).iconSize),
            color = Color.White,
            strokeWidth = 2.dp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Cargando...",
            fontSize = getButtonDimensions(size).fontSize,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private data class ButtonColors(
    val background: Color,
    val content: Color
)

private data class ButtonDimensions(
    val height: Dp,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val cornerRadius: Dp,
    val elevation: Dp,
    val fontSize: androidx.compose.ui.unit.TextUnit,
    val iconSize: Dp
)

private fun getButtonColors(type: ButtonType): ButtonColors {
    return when (type) {
        ButtonType.PRIMARY -> ButtonColors(NutriBlue, Color.White)
        ButtonType.SECONDARY -> ButtonColors(NutriGray.copy(alpha = 0.2f), NutriGrayDark)
        ButtonType.SUCCESS -> ButtonColors(NutriGreen, Color.White)
        ButtonType.WARNING -> ButtonColors(NutriOrange, Color.White)
        ButtonType.DANGER -> ButtonColors(NutriRed, Color.White)
    }
}

private fun getButtonDimensions(size: ButtonSize): ButtonDimensions {
    return when (size) {
        ButtonSize.SMALL -> ButtonDimensions(
            height = 40.dp,
            horizontalPadding = 16.dp,
            verticalPadding = 8.dp,
            cornerRadius = 8.dp,
            elevation = 2.dp,
            fontSize = 14.sp,
            iconSize = 16.dp
        )
        ButtonSize.MEDIUM -> ButtonDimensions(
            height = 48.dp,
            horizontalPadding = 20.dp,
            verticalPadding = 12.dp,
            cornerRadius = 12.dp,
            elevation = 4.dp,
            fontSize = 16.sp,
            iconSize = 18.dp
        )
        ButtonSize.LARGE -> ButtonDimensions(
            height = 56.dp,
            horizontalPadding = 24.dp,
            verticalPadding = 16.dp,
            cornerRadius = 16.dp,
            elevation = 6.dp,
            fontSize = 18.sp,
            iconSize = 20.dp
        )
    }
}

// ========================================
// Floating Action Button personalizado
// ========================================
@Composable
fun NutriFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    backgroundColor: Color = NutriGreen,
    contentColor: Color = Color.White
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    FloatingActionButton(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = modifier.graphicsLayer() {
            scaleX = scale
            scaleY = scale
        },
        containerColor = backgroundColor,
        contentColor = contentColor,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

// ========================================
// Extensiones útiles para botones
// ========================================
@Composable
fun SuccessButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    fillMaxWidth: Boolean = false
) {
    NutriButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        type = ButtonType.SUCCESS,
        enabled = enabled,
        isLoading = isLoading,
        fillMaxWidth = fillMaxWidth,
        icon = if (!isLoading) Icons.Default.Check else null
    )
}

@Composable
fun DangerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    fillMaxWidth: Boolean = false
) {
    NutriButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        type = ButtonType.DANGER,
        enabled = enabled,
        isLoading = isLoading,
        fillMaxWidth = fillMaxWidth
    )
}