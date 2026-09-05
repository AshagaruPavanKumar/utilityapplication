package com.utilityapplication.com.feature.emergency.pres.ui.scr

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.utilityapplication.com.core.prefs.AppPreferences
import com.utilityapplication.com.core.prefs.EmergencyContact
import com.utilityapplication.com.feature.emergency.data.EmergencyNumbers
import com.utilityapplication.com.feature.emergency.sos.SosFlashlight
import com.utilityapplication.com.feature.emergency.sos.SosSiren

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyScreen(
    onMedicalCard: () -> Unit = {},
    onScreenFlash: () -> Unit = {}
) {
    val context = LocalContext.current
    val prefs = remember { AppPreferences.get(context) }
    val telephony = remember { context.getSystemService(TelephonyManager::class.java) }
    val detected = (telephony?.simCountryIso ?: telephony?.networkCountryIso ?: "IN").uppercase()
    var overrideCode by remember { mutableStateOf(prefs.emergencyCountryOverride.ifBlank { detected }) }
    val services = EmergencyNumbers.forCode(overrideCode)
    var contacts by remember { mutableStateOf(prefs.getEmergencyContacts().ifEmpty { List(3) { EmergencyContact("", "") } }.toMutableList().apply {
        while (size < 3) add(EmergencyContact("", ""))
    }) }
    var flashlightOn by remember { mutableStateOf(SosFlashlight.isRunning) }
    var sirenOn by remember { mutableStateOf(SosSiren.isRunning) }
    var countryMenu by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val cameraPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            flashlightOn = SosFlashlight.toggle(context, scope)
        } else {
            Toast.makeText(context, "Camera permission needed for flashlight", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Emergency", fontWeight = FontWeight.Bold) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Emergency dial", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Text("Detected country: $detected", color = Color.Gray, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            ExposedDropdownMenuBox(expanded = countryMenu, onExpandedChange = { countryMenu = it }) {
                OutlinedTextField(
                    value = "${services.countryName} (${services.countryCode})",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Country override") },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(expanded = countryMenu, onDismissRequest = { countryMenu = false }) {
                    EmergencyNumbers.countries.forEach { country ->
                        DropdownMenuItem(
                            text = { Text("${country.countryName} (${country.countryCode})") },
                            onClick = {
                                overrideCode = country.countryCode
                                prefs.emergencyCountryOverride = country.countryCode
                                countryMenu = false
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                DialButton("Police", services.police, Modifier.weight(1f)) { dial(context, services.police) }
                DialButton("Ambulance", services.ambulance, Modifier.weight(1f)) { dial(context, services.ambulance) }
                DialButton("Fire", services.fire, Modifier.weight(1f)) { dial(context, services.fire) }
            }

            Spacer(Modifier.height(24.dp))
            Text("Emergency contacts", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            contacts.forEachIndexed { index, contact ->
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = contact.name,
                    onValueChange = { value ->
                        contacts = contacts.toMutableList().also { it[index] = it[index].copy(name = value) }
                    },
                    label = { Text("Contact ${index + 1} name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = contact.phone,
                        onValueChange = { value ->
                            contacts = contacts.toMutableList().also { it[index] = it[index].copy(phone = value) }
                        },
                        label = { Text("Phone") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = { if (contact.phone.isNotBlank()) dial(context, contact.phone) },
                        enabled = contact.phone.isNotBlank()
                    ) { Icon(Icons.Default.Call, contentDescription = "Call") }
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    prefs.setEmergencyContacts(contacts.filter { it.name.isNotBlank() || it.phone.isNotBlank() })
                    Toast.makeText(context, "Contacts saved", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save contacts") }

            Spacer(Modifier.height(24.dp))
            Text("SOS tools", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED
                    if (granted) flashlightOn = SosFlashlight.toggle(context, scope)
                    else cameraPermission.launch(Manifest.permission.CAMERA)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (flashlightOn) Color(0xFFD32F2F) else MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.FlashOn, contentDescription = null)
                Text(if (flashlightOn) "  Stop SOS flashlight" else "  SOS flashlight (Morse)")
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onScreenFlash, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.WbSunny, contentDescription = null)
                Text("  SOS screen flash")
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { sirenOn = SosSiren.toggle(context, scope) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (sirenOn) Color(0xFFD32F2F) else MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.NotificationsActive, contentDescription = null)
                Text(if (sirenOn) "  Stop siren" else "  SOS alarm siren")
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onMedicalCard, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.LocalHospital, contentDescription = null)
                Text("  Medical card")
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DialButton(label: String, number: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = modifier, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFD32F2F))
            Text(label, fontWeight = FontWeight.Bold)
            Text(number, color = Color.Gray, fontSize = 13.sp)
        }
    }
}

private fun dial(context: android.content.Context, number: String) {
    context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
}
