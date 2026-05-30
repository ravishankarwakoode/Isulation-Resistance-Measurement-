package com.example.irm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.irm.ui.theme.IRMTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IRMTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("IRM Calculator", fontWeight = FontWeight.Bold) },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                ) { innerPadding ->
                    IRMCalculator(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun IRMCalculator(modifier: Modifier = Modifier) {
    // Default select 100
    var testType by remember { mutableStateOf("100") }

    // User manual inputs
    var roInput by remember { mutableStateOf("") }
    var vbInput by remember { mutableStateOf("") }
    var v1Input by remember { mutableStateOf("") }
    var v2Input by remember { mutableStateOf("") }
    var v1PInput by remember { mutableStateOf("") }
    var v2PInput by remember { mutableStateOf("") }

    // Error states for validation (used for visual cues)
    var roError by remember { mutableStateOf(false) }
    var vbError by remember { mutableStateOf(false) }
    var v1Error by remember { mutableStateOf(false) }
    var v2Error by remember { mutableStateOf(false) }
    var v1PError by remember { mutableStateOf(false) }
    var v2PError by remember { mutableStateOf(false) }

    // Result states
    var isolateResistance by remember { mutableStateOf("") }
    var resistanceResult by remember { mutableStateOf("") }
    var resultStatus by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        // --- Section: Test Type ---
        Surface(
            tonalElevation = 2.dp,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Select Test Type",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                TestTypeOption("After test of 100 Ω/v", testType == "100") { testType = "100" }
                TestTypeOption("Before test 500 Ω/v", testType == "500") { testType = "500" }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- Section: Input Parameters ---
        Text(
            text = "Enter Parameters",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))

        ParameterInput(
            label = "Ro Value",
            placeholder = "20000 - 30000",
            value = roInput,
            isError = roError,
            errorText = "Range: 20k - 30k",
            onValueChange = { input ->
                val value = input.toDoubleOrNull()
                if (input.isEmpty() || (value != null && value <= 30000)) {
                    roInput = input
                    roError = value != null && value < 20000
                }
            }
        )

        ParameterInput(
            label = "Vb Value",
            placeholder = "45v - 60v ",
            value = vbInput,
            isError = vbError,
            errorText = "Range: 45 - 60",
            onValueChange = { input ->
                val value = input.toDoubleOrNull()
                if (input.isEmpty() || (value != null && value <= 60)) {
                    vbInput = input
                    vbError = value != null && value < 45
                }
            }
        )

        Row(Modifier.fillMaxWidth()) {
            Box(Modifier.weight(1f)) {
                ParameterInput(
                    label = "V1",
                    placeholder = "0 - 50",
                    value = v1Input,
                    isError = v1Error,
                    errorText = "Max 50",
                    onValueChange = { input ->
                        val value = input.toDoubleOrNull()
                        if (input.isEmpty() || (value != null && value <= 50)) {
                            v1Input = input
                            v1Error = value != null && value < 0
                        }
                    }
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(Modifier.weight(1f)) {
                ParameterInput(
                    label = "V1'",
                    placeholder = "0 - 50",
                    value = v1PInput,
                    isError = v1PError,
                    errorText = "Max 50",
                    onValueChange = { input ->
                        val value = input.toDoubleOrNull()
                        if (input.isEmpty() || (value != null && value <= 50)) {
                            v1PInput = input
                            v1PError = value != null && value < 0
                        }
                    }
                )
            }
        }

        Row(Modifier.fillMaxWidth()) {
            Box(Modifier.weight(1f)) {
                ParameterInput(
                    label = "V2",
                    placeholder = "0 - 50",
                    value = v2Input,
                    isError = v2Error,
                    errorText = "Max 50",
                    onValueChange = { input ->
                        val value = input.toDoubleOrNull()
                        if (input.isEmpty() || (value != null && value <= 50)) {
                            v2Input = input
                            v2Error = value != null && value < 0
                        }
                    }
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(Modifier.weight(1f)) {
                ParameterInput(
                    label = "V2'",
                    placeholder = "0 - 50",
                    value = v2PInput,
                    isError = v2PError,
                    errorText = "Max 50",
                    onValueChange = { input ->
                        val value = input.toDoubleOrNull()
                        if (input.isEmpty() || (value != null && value <= 50)) {
                            v2PInput = input
                            v2PError = value != null && value < 0
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Section: Results ---
        Text(
            text = "Calculation Results",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        ResultField("Isolate Resistance (Ω/v)", isolateResistance)
        Spacer(modifier = Modifier.height(8.dp))
        ResultField("Resistance (V)", resistanceResult)

        Spacer(modifier = Modifier.height(24.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                val ro = roInput.toDoubleOrNull()
                val vb = vbInput.toDoubleOrNull()
                val v1 = v1Input.toDoubleOrNull()
                val v2 = v2Input.toDoubleOrNull()
                val v1P = v1PInput.toDoubleOrNull()
                val v2P = v2PInput.toDoubleOrNull()

                // Basic check for empty fields or invalid numbers
                if (ro == null || vb == null || v1 == null || v2 == null || v1P == null || v2P == null) {
                    errorMessage = "Please enter numbers for all parameters"
                    isolateResistance = ""
                    resistanceResult = ""
                    resultStatus = ""
                    return@Button
                }

                // Mathematical safety and Conditional Logic
                errorMessage = ""
                try {
                    val ir: Double
                    if (v1 > v2) {
                        if (v1 == 0.0 || v1P == 0.0) {
                            errorMessage = "V1 and V1' cannot be zero"
                            return@Button
                        }
                        // Formula for V1 > V2: Ro * vb * ( (1/v1') - (1/v1) )
                        ir = ro * vb * ( (1.0 / v1P) - (1.0 / v1) )
                    } else if (v2 > v1) {
                        if (v2 == 0.0 || v2P == 0.0) {
                            errorMessage = "V2 and V2' cannot be zero"
                            return@Button
                        }
                        // Formula for V2 > V1: Ro * vb * ( (1/v2') - (1/v2) )
                        ir = ro * vb * ( (1.0 / v2P) - (1.0 / v2) )
                    } else {
                        // V1 == V2 case
                        errorMessage = "V1 and V2 are equal. Please check measurements."
                        return@Button
                    }

                    isolateResistance = String.format("%.2f", ir)

                    // Resistance Result (Formula: Isolate Resistance / 51.2)
                    val res = ir / 51.2
                    resistanceResult = String.format("%.2f", res)

                    // Pass/Fail Logic: Compare Isolate Resistance directly to Test Type threshold
                    val threshold = testType.toDouble()
                    resultStatus = if (ir >= threshold) "PASS" else "FAIL"

                    // Add a warning if they are out of range but we still showed result
                    if (roError || vbError || v1Error || v2Error || v1PError || v2PError) {
                        errorMessage = "Warning: Some values are outside recommended ranges."
                    }
                } catch (e: Exception) {
                    errorMessage = "Error in calculation"
                    isolateResistance = "Error"
                    resistanceResult = "Error"
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Calculate IRM Formula", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        if (resultStatus.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (resultStatus == "PASS") Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.padding(24.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "RESULT: $resultStatus",
                        color = if (resultStatus == "PASS") Color(0xFF2E7D32) else Color(0xFFC62828),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun TestTypeOption(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelect)
            .padding(vertical = 4.dp)
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ParameterInput(
    label: String,
    placeholder: String,
    value: String,
    isError: Boolean = false,
    errorText: String = "",
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        isError = isError,
        supportingText = { if (isError) Text(errorText) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun ResultField(label: String, value: String) {
    OutlinedTextField(
        value = value,
        onValueChange = { },
        label = { Text(label) },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.outline,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedContainerColor = Color(0xFFF9F9F9),
            unfocusedContainerColor = Color(0xFFF9F9F9)
        )
    )
}
