package com.admin.mathboy.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.admin.mathboy.ui.theme.MathBoyTheme
import com.admin.mathboy.views.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MathBoyTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = {
                            Text(
                                text = "Math Boy",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                            colors = TopAppBarDefaults.largeTopAppBarColors(Color.LightGray))
                    })
                { innerPadding ->
                    MainScreen(innerPadding, mainViewModel)
                }
            }
        }
    }

    @Composable
    fun MainScreen(innerPadding: PaddingValues, mainViewModel: MainViewModel) {
        val calculatedResult by mainViewModel.uiState.collectAsState()
        var input1 by rememberSaveable { mutableStateOf("") }
        var input2 by rememberSaveable { mutableStateOf("") }
        var input3 by rememberSaveable { mutableStateOf("") }
        var isButtonClicked by rememberSaveable { mutableStateOf(false) }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                CustomOutlinedTextField(
                    value = input1,
                    onValueChange = {
                        input1 = it
                        isButtonClicked = false
                    },
                    label = "Number List 1",
                    placeholder = "Add list of comma-separated numbers",
                    maxLines = 10
                )
            }
            item {
                CustomOutlinedTextField(
                    value = input2,
                    onValueChange = {
                        input2 = it
                        isButtonClicked = false
                    },
                    label = "Number List 2",
                    placeholder = "Add list of comma-separated numbers",
                    maxLines = 10
                )
            }
            item {
                CustomOutlinedTextField(
                    value = input3,
                    onValueChange = {
                        input3 = it
                        isButtonClicked = false
                    },
                    label = "Number List 3",
                    placeholder = "Add list of comma-separated numbers",
                    maxLines = 10
                )
            }

            item {
                Button(onClick = {
                    isButtonClicked = true
                    mainViewModel.calculateResults(input1, input2, input3)
                }) {
                    Text("Calculate")
                }
            }
            item {
                if (isButtonClicked) {
                    if (listOf(input1, input2, input3).all { it.isEmpty() }) {
                        Text("Please enter at least one list")
                    } else {
                        calculatedResult?.let { result ->
                            Card(
                                modifier = Modifier.padding(16.dp),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ){
                                Text(
                                    text = "Results from 3 list",
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(16.dp),
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Serif
                                )
                                Text(
                                    text = result,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun CustomOutlinedTextField(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        placeholder: String,
        maxLines: Int
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = false,
            placeholder = {
                Text(placeholder,
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            },
            maxLines = maxLines,
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MathBoyTheme {
            MainScreen(innerPadding = PaddingValues(16.dp), mainViewModel = mainViewModel)
        }
    }
}