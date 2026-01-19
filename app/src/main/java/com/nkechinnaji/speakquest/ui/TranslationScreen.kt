package com.nkechinnaji.speakquest.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.ui.res.painterResource
import com.nkechinnaji.speakquest.R
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nkechinnaji.speakquest.api.Language
import com.nkechinnaji.speakquest.ui.theme.CardPink
import com.nkechinnaji.speakquest.ui.theme.GradientEnd
import com.nkechinnaji.speakquest.ui.theme.GradientMid
import com.nkechinnaji.speakquest.ui.theme.GradientStart
import com.nkechinnaji.speakquest.ui.theme.OliveGray
import com.nkechinnaji.speakquest.ui.theme.Pink40
import com.nkechinnaji.speakquest.ui.theme.Pink90
import com.nkechinnaji.speakquest.viewmodel.TranslationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationScreen(
    viewModel: TranslationViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager = LocalClipboardManager.current

    var inputText by rememberSaveable { mutableStateOf("") }
    var sourceLanguage by rememberSaveable { mutableStateOf("en") }
    var targetLanguage by rememberSaveable { mutableStateOf("es") }

    // Swap animation
    var isSwapping by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isSwapping) 180f else 0f,
        animationSpec = tween(300),
        finishedListener = { isSwapping = false },
        label = "swap_rotation"
    )

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header with pink gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(GradientStart, GradientMid, GradientEnd)
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Custom SQ Logo
                    Image(
                        painter = painterResource(id = R.drawable.ic_speakquest_logo),
                        contentDescription = "SpeakQuest Logo",
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "SpeakQuest",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Language Selection Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // From language
                        Text(
                            text = "From",
                            style = MaterialTheme.typography.labelMedium,
                            color = Pink40,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LanguageSelector(
                            languages = uiState.languages,
                            selectedCode = sourceLanguage,
                            onSelect = { sourceLanguage = it }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Swap button
                        IconButton(
                            onClick = {
                                isSwapping = true
                                val temp = sourceLanguage
                                sourceLanguage = targetLanguage
                                targetLanguage = temp
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Swap languages",
                                tint = Pink40.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .size(24.dp)
                                    .rotate(rotation)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // To language
                        Text(
                            text = "To",
                            style = MaterialTheme.typography.labelMedium,
                            color = Pink40,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LanguageSelector(
                            languages = uiState.languages,
                            selectedCode = targetLanguage,
                            onSelect = { targetLanguage = it }
                        )
                    }
                }

                // Original Text Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Original Text",
                            style = MaterialTheme.typography.titleMedium,
                            color = Pink40,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = {
                                inputText = it
                                viewModel.clearTranslation()
                            },
                            placeholder = {
                                Text(
                                    "Enter text to translate...",
                                    color = Pink40.copy(alpha = 0.4f)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Pink40,
                                unfocusedBorderColor = Pink40.copy(alpha = 0.3f),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                cursorColor = Pink40
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${inputText.length} characters",
                            style = MaterialTheme.typography.labelSmall,
                            color = Pink40.copy(alpha = 0.6f)
                        )
                    }
                }

                // Translation Result Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Translation",
                                style = MaterialTheme.typography.titleMedium,
                                color = Pink40,
                                fontWeight = FontWeight.Bold
                            )
                            if (uiState.translatedText.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(uiState.translatedText))
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        modifier = Modifier.size(18.dp),
                                        tint = Pink40
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(CardPink)
                                .padding(16.dp)
                        ) {
                            if (uiState.translatedText.isNotEmpty()) {
                                Text(
                                    text = uiState.translatedText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Pink40
                                )
                            } else {
                                Text(
                                    text = "Translation will appear here...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Pink40.copy(alpha = 0.4f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${uiState.translatedText.length} characters",
                            style = MaterialTheme.typography.labelSmall,
                            color = Pink40.copy(alpha = 0.6f)
                        )
                    }
                }

                // Translate Button
                Button(
                    onClick = {
                        viewModel.translate(inputText, sourceLanguage, targetLanguage)
                    },
                    enabled = !uiState.isLoading && inputText.isNotBlank(),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OliveGray,
                        disabledContainerColor = OliveGray.copy(alpha = 0.4f)
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Translating...", fontWeight = FontWeight.SemiBold)
                    } else {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Translate", fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageSelector(
    languages: List<Language>,
    selectedCode: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLanguage = languages.find { it.code == selectedCode }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedLanguage?.name ?: selectedCode,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Pink40,
                unfocusedBorderColor = Pink40,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Pink40,
                unfocusedTextColor = Pink40,
                focusedTrailingIconColor = Pink40,
                unfocusedTrailingIconColor = Pink40
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = {
                        Text(
                            language.name,
                            color = Pink40,
                            fontWeight = if (language.code == selectedCode) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onSelect(language.code)
                        expanded = false
                    }
                )
            }
        }
    }
}
